package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class PairakabeGameState extends CellsGameState<PairakabeGame, PairakabeGameMove, PairakabeGameState> {
    public PairakabeObject[] objArray;

    public PairakabeGameState(PairakabeGame game) {
        super(game);
        objArray = new PairakabeObject[rows() * cols()];
        Arrays.fill(objArray, new PairakabeEmptyObject());
        for (Position p : game.pos2hint.keySet())
            set(p, new PairakabeHintObject());
    }

    public PairakabeObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public PairakabeObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, PairakabeObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, PairakabeObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(PairakabeGameMove move) {
        Position p = move.p;
        PairakabeObject objOld = get(p);
        PairakabeObject objNew = move.obj;
        if (objOld instanceof PairakabeHintObject || objOld.toString().equals(objNew.toString())) return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(PairakabeGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<PairakabeObject, PairakabeObject> f = obj -> {
            if (obj instanceof PairakabeEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new PairakabeMarkerObject() : new PairakabeWallObject();
            if (obj instanceof PairakabeWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new PairakabeMarkerObject() : new PairakabeEmptyObject();
            if (obj instanceof PairakabeMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new PairakabeWallObject() : new PairakabeEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 12/Pairakabe

        Summary
        Just to confuse things a bit more

        Description
        1. Plays like Nurikabe, with an interesting twist.
        2. Instead of just one number, each 'garden' contains two numbers and
           the area of the garden is given by the sum of both.
    */
    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows() - 1; r++)
            rule2x2:
                    for (int c = 0; c < cols() - 1; c++) {
                        Position p = new Position(r, c);
                        for (Position os : PairakabeGame.offset2)
                            if (!(get(p.add(os)) instanceof PairakabeWallObject))
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
                if (get(p) instanceof PairakabeWallObject)
                    rngWalls.add(p);
                else
                    rngEmpty.add(p);
            }
        for (Position p : rngWalls)
            for (Position os : PairakabeGame.offset) {
                Position p2 = p.add(os);
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        for (Position p : rngEmpty)
            for (Position os : PairakabeGame.offset) {
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
        while (!rngEmpty.isEmpty()) {
            Node node = pos2node.get(rngEmpty.get(0));
            g.setRootNode(node);
            List<Node> nodeList = g.bfs();
            rngEmpty = iterableList(rngEmpty).removeAll(p -> nodeList.contains(pos2node.get(p))).toJavaList();
            int n2 = nodeList.size();
            List<Position> rng = new ArrayList<>();
            for (Position p : game.pos2hint.keySet())
                if (nodeList.contains(pos2node.get(p)))
                    rng.add(p.plus());
            switch (rng.size()) {
                case 0:
                    isSolved = false;
                    break;
                case 1:
                    ((PairakabeHintObject) get(rng.get(0))).state = HintState.Error;
                    break;
                case 2:
                {
                    Position p1 = rng.get(0), p2 = rng.get(1);
                    int n1 = game.pos2hint.get(p1) + game.pos2hint.get(p2);
                    HintState s = n1 == n2 ? HintState.Complete : HintState.Error;
                    ((PairakabeHintObject) get(p1)).state = s;
                    ((PairakabeHintObject) get(p2)).state = s;
                    if (s != HintState.Complete) isSolved = false;
                }
                break;
                default:
                    for (Position p : rng)
                        ((PairakabeHintObject) get(p)).state = HintState.Normal;
                    isSolved = false;
                    break;
            }
        }
    }
}
