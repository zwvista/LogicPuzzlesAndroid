package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.List.iterableList;

public class HitoriGameState extends CellsGameState<HitoriGame, HitoriGameMove, HitoriGameState> {
    private HitoriObject[] objArray;
    public String[] row2hint;
    public String[] col2hint;

    public HitoriGameState(HitoriGame game) {
        super(game);
        objArray = new HitoriObject[rows() * cols()];
        Arrays.fill(objArray, HitoriObject.Normal);
        row2hint = new String[rows()];
        col2hint = new String[cols()];
        updateIsSolved();
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

    public boolean setObject(HitoriGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(HitoriGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<HitoriObject, HitoriObject> f = obj -> {
            switch (obj) {
            case Normal:
                return markerOption == MarkerOptions.MarkerFirst ?
                        HitoriObject.Marker : HitoriObject.Darken;
            case Darken:
                return markerOption == MarkerOptions.MarkerLast ?
                        HitoriObject.Marker : HitoriObject.Normal;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        HitoriObject.Darken : HitoriObject.Normal;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Hitori

        Summary
        Darken some tiles so no number appears in a row or column more than once

        Description
        1. The goal is to shade squares so that a number appears only once in a
           row or column.
        2. While doing that, you must take care that shaded squares don't touch
           horizontally or vertically between them.
        3. In the end all the un-shaded squares must form a single continuous area.
    */
    private void updateIsSolved() {
        isSolved = true;
        String chars;
        // 1. The goal is to shade squares so that a number appears only once in a
        // row.
        for (int r = 0; r < rows(); r++) {
            chars = row2hint[r] = "";
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) == HitoriObject.Darken) continue;
                char ch = game.get(r, c);
                if (chars.contains(String.valueOf(ch))) {
                    isSolved = false;
                    row2hint[r] += ch;
                } else
                    chars += ch;
            }
        }
        // 1. The goal is to shade squares so that a number appears only once in a
        // column.
        for (int c = 0; c < cols(); c++) {
            chars = col2hint[c] = "";
            for (int r = 0; r < rows(); r++) {
                Position p = new Position(r, c);
                if (get(p) == HitoriObject.Darken) continue;
                char ch = game.get(r, c);
                if (chars.contains(String.valueOf(ch))) {
                    isSolved = false;
                    col2hint[c] += ch;
                } else
                    chars += ch;
            }
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        List<Position> rngDarken = new ArrayList<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) == HitoriObject.Darken)
                    rngDarken.add(p);
                else{
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        // 2. While doing that, you must take care that shaded squares don't touch
        // horizontally or vertically between them.
        for (Position p : rngDarken)
            for (Position os : HitoriGame.offset) {
                Position p2 = p.add(os);
                if (rngDarken.contains(p2)) {
                    isSolved = false;
                    return;
                }
            }
        for (Position p : pos2node.keySet()) {
            for (Position os : HitoriGame.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        }
        // 3. In the end all the un-shaded squares must form a single continuous area.
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;
    }
}
