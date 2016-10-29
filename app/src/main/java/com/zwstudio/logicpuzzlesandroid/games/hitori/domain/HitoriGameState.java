package com.zwstudio.logicpuzzlesandroid.games.hitori.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fj.F;

import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class HitoriGameState extends CellsGameState<HitoriGame, HitoriGameMove, HitoriGameState> {
    public HitoriObject[] objArray;

    public HitoriGameState(HitoriGame game) {
        super(game);
        objArray = new HitoriObject[rows() * cols()];
        Arrays.fill(objArray, HitoriObject.Normal);
    }

    public HitoriObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public HitoriObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, HitoriObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, HitoriObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        Set<Character> chars = new HashSet<>();
        for (int r = 0; r < rows(); r++) {
            chars.clear();
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) == HitoriObject.Darken) continue;
                char ch = game.get(r, c);
                if (chars.contains(ch)) { isSolved = false; return; }
                chars.add(ch);
            }
        }
        for (int c = 0; c < cols(); c++) {
            chars.clear();
            for (int r = 0; r < rows(); r++) {
                Position p = new Position(r, c);
                if (get(p) == HitoriObject.Darken) continue;
                char ch = game.get(r, c);
                if (chars.contains(ch)) { isSolved = false; return; }
                chars.add(ch);
            }
        }
        Graph g = new Graph();
        Map<Position, Node> pos2Node = new HashMap<>();
        List<Position> rngDarken = new ArrayList<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) == HitoriObject.Darken)
                    rngDarken.add(p);
                else{
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2Node.put(p, node);
                }
            }
        for (Position p : rngDarken)
            for (Position os : HitoriGame.offset) {
                Position p2 = p.add(os);
                if (rngDarken.contains(p2)) {
                    isSolved = false;
                    return;
                }
            }
        for (Position p : pos2Node.keySet()) {
            for (Position os : HitoriGame.offset) {
                Position p2 = p.add(os);
                if (pos2Node.containsKey(p2))
                    g.connectNode(pos2Node.get(p), pos2Node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2Node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2Node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(HitoriGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(HitoriMarkerOptions markerOption, HitoriGameMove move) {
        F<HitoriObject, HitoriObject> f = obj -> {
            switch (obj) {
            case Normal:
                return markerOption == HitoriMarkerOptions.MarkerBeforeDarken ?
                        HitoriObject.Marker : HitoriObject.Darken;
            case Darken:
                return markerOption == HitoriMarkerOptions.MarkerAfterDarken ?
                        HitoriObject.Marker : HitoriObject.Normal;
            case Marker:
                return markerOption == HitoriMarkerOptions.MarkerBeforeDarken ?
                        HitoriObject.Darken : HitoriObject.Normal;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }
}
