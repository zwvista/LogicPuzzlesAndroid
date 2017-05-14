package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain;

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

public class NurikabeGameState extends CellsGameState<NurikabeGame, NurikabeGameMove, NurikabeGameState> {
    public NurikabeObject[] objArray;

    public NurikabeGameState(NurikabeGame game) {
        super(game);
        objArray = new NurikabeObject[rows() * cols()];
        Arrays.fill(objArray, new NurikabeEmptyObject());
        for (Position p : game.pos2hint.keySet())
            set(p, new NurikabeHintObject());
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

    public boolean setObject(NurikabeGameMove move) {
        Position p = move.p;
        NurikabeObject objOld = get(p);
        NurikabeObject objNew = move.obj;
        if (objOld instanceof NurikabeHintObject || objOld.toString().equals(objNew.toString())) return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(NurikabeGameMove move, MarkerOptions markerOption) {
        F<NurikabeObject, NurikabeObject> f = obj -> {
            if (obj instanceof NurikabeEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new NurikabeMarkerObject() : new NurikabeWallObject();
            if (obj instanceof NurikabeWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new NurikabeMarkerObject() : new NurikabeEmptyObject();
            if (obj instanceof NurikabeMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new NurikabeWallObject() : new NurikabeEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Nurikabe

        Summary
        Draw a continuous wall that divides gardens as big as the numbers

        Description
        1. Each number on the grid indicates a garden, occupying as many tiles
           as the number itself.
        2. Gardens can have any form, extending horizontally and vertically, but
           can't extend diagonally.
        3. The garden is separated by a single continuous wall. This means all
           wall tiles on the board must be connected horizontally or vertically.
           There can't be isolated walls.
        4. You must find and mark the wall following these rules:
        5. All the gardens in the puzzle are numbered at the start, there are no
           hidden gardens.
        6. A wall can't go over numbered squares.
        7. The wall can't form 2*2 squares.
    */
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
        while (!rngEmpty.isEmpty()) {
            Node node = pos2node.get(rngEmpty.get(0));
            g.setRootNode(node);
            List<Node> nodeList = g.bfs();
            rngEmpty = iterableList(rngEmpty).removeAll(p -> nodeList.contains(pos2node.get(p))).toJavaList();
            int n2 = nodeList.size();
            List<Position> rng = new ArrayList<>();
            for (Position p : game.pos2hint.keySet())
                if (nodeList.contains(pos2node.get(p)))
                    rng.add(p);
            switch (rng.size()) {
                case 0:
                    isSolved = false;
                    break;
                case 1:
                {
                    Position p = rng.get(0);
                    int n1 = game.pos2hint.get(p);
                    HintState s = n1 == n2 ? HintState.Complete : HintState.Error;
                    ((NurikabeHintObject) get(p)).state = s;
                    if (s != HintState.Complete) isSolved = false;
                }
                break;
                default:
                    for (Position p : rng)
                        ((NurikabeHintObject) get(p)).state = HintState.Normal;
                    isSolved = false;
                    break;
            }
        }
    }
}
