package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F0;
import fj.P2;
import fj.function.Effect0;
import fj.function.Effect2;

import static fj.data.HashMap.fromMap;
import static fj.data.List.arrayList;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class GardenerGameState extends CellsGameState<GardenerGame, GardenerGameMove, GardenerGameState> {
    public GardenerObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public GardenerGameState(GardenerGame game) {
        super(game);
        objArray = new GardenerObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new GardenerEmptyObject();
        updateIsSolved();
    }

    public GardenerObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public GardenerObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, GardenerObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, GardenerObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(GardenerGameMove move) {
        if (!isValid(move.p) || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(GardenerGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<GardenerObject, GardenerObject> f = obj -> {
            if (obj instanceof GardenerEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new GardenerMarkerObject() : new GardenerTreeObject();
            if (obj instanceof GardenerTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new GardenerMarkerObject() : new GardenerEmptyObject();
            if (obj instanceof GardenerMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new GardenerTreeObject() : new GardenerEmptyObject();
            return obj;
        };
        GardenerObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 7/Gardener

        Summary
        Hitori Flower Planting

        Description
        1. The Board represents a Garden, divided in many rectangular Flowerbeds.
        2. The owner of the Garden wants you to plant Flowers according to these
           rules.
        3. A number tells you how many Flowers you must plant in that Flowerbed.
           A Flowerbed without number can have any quantity of Flowers.
        4. Flowers can't be horizontally or vertically touching.
        5. All the remaining Garden space where there are no Flowers must be
           interconnected (horizontally or vertically), as he wants to be able
           to reach every part of the Garden without treading over Flowers.
        6. Lastly, there must be enough balance in the Garden, so a straight
           line (horizontally or vertically) of non-planted tiles can't span
           for more than two Flowerbeds.
        7. In other words, a straight path of empty space can't pass through
           three or more Flowerbeds.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                GardenerObject o = get(p);
                if (o instanceof GardenerTreeObject)
                    ((GardenerTreeObject)o).state = AllowedObjectState.Normal;
                else {
                    if (o instanceof GardenerForbiddenObject)
                        set(p, new GardenerEmptyObject());
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (Position os : TierraDelFuegoGame.offset) {
                Position p2 = p.add(os);
                Node node2 = pos2node.get(p2);
                if (node2 == null) continue;
                g.connectNode(node, node2);
            }
        }
        g.setRootNode(fromMap(pos2node).values().head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;

        for (Map.Entry<Position, P2<Integer, Integer>> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue()._1();
            int i = entry.getValue()._2();
            List<Position> area = game.areas.get(i);
            int n1 = 0;
            for (Position p2 : area)
                if (get(p2) instanceof GardenerTreeObject)
                    n1++;
            HintState s = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            pos2state.put(p, s);
            if (s != HintState.Complete) isSolved = false;
            if (s != HintState.Normal && allowedObjectsOnly)
                for (Position p2 : area) {
                    GardenerObject o = get(p2);
                    if (o instanceof GardenerEmptyObject || o instanceof GardenerMarkerObject)
                        set(p2, new GardenerForbiddenObject());
                }
        }

        List<Position> trees = new ArrayList<>();
        F0<Boolean> areTreesInvalid = () -> {
            return new HashSet<Integer>(iterableList(trees).map(p -> game.pos2area.get(p)).toJavaList()).size() > 2;
        };
        Effect0 checkTrees = () -> {
            if (areTreesInvalid.f()) {
                isSolved = false;
                for (Position p : trees)
                    ((GardenerTreeObject)get(p)).state = AllowedObjectState.Error;
            }
            trees.clear();
        };
        Effect2<Position, List<Integer>> checkForbidden = (p, indexes) -> {
            if (!allowedObjectsOnly) return;
            for (int i : indexes) {
                Position os = GardenerGame.offset[i];
                for (Position p2 = p.add(os); isValid(p2) && get(p2) instanceof GardenerTreeObject; p2.addBy(os))
                    trees.add(p2);
            }
            if (areTreesInvalid.f()) set(p, new GardenerForbiddenObject());
            trees.clear();
        };
        for (int r = 0; r < rows(); r++) {
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                GardenerObject o = get(p);
                if (o instanceof GardenerTreeObject)
                    trees.add(p);
                else {
                    checkTrees.f();
                    if (o instanceof GardenerEmptyObject || o instanceof GardenerMarkerObject)
                        checkForbidden.f(p, arrayList(1, 3).toJavaList());
                }
            }
            checkTrees.f();
        }
        for (int c = 0; c < cols(); c++) {
            for (int r = 0; r < rows(); r++) {
                Position p = new Position(r, c);
                GardenerObject o = get(p);
                if (o instanceof GardenerTreeObject)
                    trees.add(p);
                else {
                    checkTrees.f();
                    if (o instanceof GardenerEmptyObject || o instanceof GardenerMarkerObject)
                        checkForbidden.f(p, arrayList(0, 2).toJavaList());
                }
            }
            checkTrees.f();
        }
    }
}
