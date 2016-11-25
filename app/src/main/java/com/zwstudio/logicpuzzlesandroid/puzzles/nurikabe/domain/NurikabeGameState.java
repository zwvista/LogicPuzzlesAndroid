package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class NurikabeGameState extends CellsGameState<NurikabeGame, NurikabeGameMove, NurikabeGameState> {
    public NurikabeObject[] objArray;

    public NurikabeGameState(NurikabeGame game) {
        super(game);
        objArray = new NurikabeObject[rows() * cols()];
        Arrays.fill(objArray, new NurikabeEmptyObject());
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            NurikabeHintObject o = new NurikabeHintObject();
            o.state = n <= 0 ? HintState.Complete : HintState.Normal;
            set(p, o);
        }
    }

    public NurikabeObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public NurikabeObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, NurikabeObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, NurikabeObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows() - 1; r++)
            rule2x2:
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                for (Position os : NurikabeGame.offset2)
                    if (!(get(p.add(os)) instanceof NurikabeWallObject))
                        continue rule2x2;
                isSolved = false;
            }
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        List<Position> rngWalls = new ArrayList<>();
        List<Position> rngEmpty = new ArrayList<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
                if (get(p) instanceof NurikabeWallObject)
                    rngWalls.add(p);
                else
                    rngEmpty.add(p);
            }
        for (Position p : rngWalls)
            for (Position os : NurikabeGame.offset) {
                Position p2 = p.add(os);
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        for (Position p : rngEmpty)
            for (Position os : NurikabeGame.offset) {
                Position p2 = p.add(os);
                if (rngEmpty.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        if (rngWalls.isEmpty())
            isSolved = false;
        else {
            g.setRootNode(pos2node.get(rngWalls.get(0)));
            List<Node> nodeList = g.bfs();
            if (rngWalls.size() != nodeList.size()) isSolved = false;
        }
        int m = 0;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n1 = entry.getValue();
            Node node = pos2node.get(p);
            if (node.visited) continue;
            g.setRootNode(node);
            List<Node> nodeList = g.bfs();
            int n2 = nodeList.size();
            List<Position> rng = new ArrayList<>();
            for (Position p2 : game.pos2hint.keySet())
                if (nodeList.contains(pos2node.get(p2)))
                    rng.add(p2);
            if (rng.size() > 1)
                for (Position p2 : rng)
                    ((NurikabeHintObject) get(p2)).state = HintState.Normal;
            else {
                HintState s = n1 == n2 ? HintState.Complete : HintState.Error;
                ((NurikabeHintObject) get(p)).state = HintState.Normal;
                if (s != HintState.Complete) isSolved = false;
            }
            m += rng.size();
        }
        if (m != rngEmpty.size()) isSolved = false;
    }

    public boolean setObject(NurikabeGameMove move) {
        Position p = move.p;
        NurikabeObject objOld = get(p);
        NurikabeObject objNew = move.obj;
        if (objOld instanceof NurikabeHintObject || objOld.toString().equals(objNew.toString())) return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(NurikabeMarkerOptions markerOption, NurikabeGameMove move) {
        F<NurikabeObject, NurikabeObject> f = obj -> {
            if (obj instanceof NurikabeEmptyObject)
                return markerOption == NurikabeMarkerOptions.MarkerBeforeWall ?
                        new NurikabeMarkerObject() : new NurikabeWallObject();
            if (obj instanceof NurikabeWallObject)
                return markerOption == NurikabeMarkerOptions.MarkerAfterWall ?
                        new NurikabeMarkerObject() : new NurikabeEmptyObject();
            if (obj instanceof NurikabeMarkerObject)
                return markerOption == NurikabeMarkerOptions.MarkerBeforeWall ?
                        new NurikabeWallObject() : new NurikabeEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }
}
