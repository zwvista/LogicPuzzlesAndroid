package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F0;
import fj.function.Effect0;
import fj.function.Effect2;

import static fj.data.HashMap.fromMap;
import static fj.data.List.arrayList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class FourMeNotGameState extends CellsGameState<FourMeNotGame, FourMeNotGameMove, FourMeNotGameState> {
    public FourMeNotObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public FourMeNotGameState(FourMeNotGame game) {
        super(game);
        objArray = new FourMeNotObject[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
        updateIsSolved();
    }

    public FourMeNotObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public FourMeNotObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, FourMeNotObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, FourMeNotObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(FourMeNotGameMove move) {
        if (!isValid(move.p) || !(game.get(move.p) instanceof FourMeNotEmptyObject) || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(FourMeNotGameMove move) {
        if (!isValid(move.p) || !(game.get(move.p) instanceof FourMeNotEmptyObject)) return false;
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<FourMeNotObject, FourMeNotObject> f = obj -> {
            if (obj instanceof FourMeNotEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new FourMeNotMarkerObject() : new FourMeNotTreeObject();
            if (obj instanceof FourMeNotTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new FourMeNotMarkerObject() : new FourMeNotEmptyObject();
            if (obj instanceof FourMeNotMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new FourMeNotTreeObject() : new FourMeNotEmptyObject();
            return obj;
        };
        FourMeNotObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/Four-Me-Not

        Summary
        It seems we do a lot of gardening in this game!

        Description
        1. In Four-Me-Not (or Forbidden Four) you need to create a continuous
           flower bed without putting four flowers in a row.
        2. More exactly, you have to join the existing flowers by adding more of
           them, creating a single path of flowers touching horizontally or
           vertically.
        3. At the same time, you can't line up horizontally or vertically more
           than 3 flowers (thus Forbidden Four).
        4. Some tiles are marked as squares and are just fixed blocks.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                FourMeNotObject o = get(p);
                if (o instanceof FourMeNotForbiddenObject)
                    set(p, new FourMeNotEmptyObject());
                else if (o instanceof FourMeNotTreeObject) {
                    ((FourMeNotTreeObject)o).state = AllowedObjectState.Normal;
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (Position os : FourMeNotGame.offset) {
                Position p2 = p.add(os);
                Node node2 = pos2node.get(p2);
                if (node2 != null) g.connectNode(node, node2);
            }
        }
        // 2. More exactly, you have to join the existing flowers by adding more of
        // them, creating a single path of flowers touching horizontally or
        // vertically.
        g.setRootNode(fromMap(pos2node).values().head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;

        List<Position> trees = new ArrayList<>();
        // 3. At the same time, you can't line up horizontally or vertically more
        // than 3 flowers (thus Forbidden Four).
        F0<Boolean> areTreesInvalid = () -> {
            return trees.size() > 3;
        };
        Effect0 checkTrees = () -> {
            if (areTreesInvalid.f()) {
                isSolved = false;
                for (Position p : trees)
                    ((FourMeNotTreeObject)get(p)).state = AllowedObjectState.Error;
            }
            trees.clear();
        };
        Effect2<Position, List<Integer>> checkForbidden = (p, indexes) -> {
            if (!allowedObjectsOnly) return;
            for (int i : indexes) {
                Position os = FourMeNotGame.offset[i];
                for (Position p2 = p.add(os); isValid(p2) && get(p2) instanceof FourMeNotTreeObject; p2.addBy(os))
                    trees.add(p2);
            }
            if (areTreesInvalid.f()) set(p, new FourMeNotForbiddenObject());
            trees.clear();
        };
        for (int r = 0; r < rows(); r++) {
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                FourMeNotObject o = get(p);
                if (o instanceof FourMeNotTreeObject)
                    trees.add(p);
                else {
                    checkTrees.f();
                    if (o instanceof FourMeNotEmptyObject || o instanceof FourMeNotMarkerObject)
                        checkForbidden.f(p, arrayList(1, 3).toJavaList());
                }
            }
            checkTrees.f();
        }
        for (int c = 0; c < cols(); c++) {
            for (int r = 0; r < rows(); r++) {
                Position p = new Position(r, c);
                FourMeNotObject o = get(p);
                if (o instanceof FourMeNotTreeObject)
                    trees.add(p);
                else {
                    checkTrees.f();
                    if (o instanceof FourMeNotEmptyObject || o instanceof FourMeNotMarkerObject)
                        checkForbidden.f(p, arrayList(0, 2).toJavaList());
                }
            }
            checkTrees.f();
        }
    }
}
