package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fj.F;
import fj.F2;

import static fj.data.Array.array;
import static fj.data.HashMap.fromMap;
import static fj.data.Stream.range;

public class BWTapaGameState extends CellsGameState<BWTapaGame, BWTapaGameMove, BWTapaGameState> {
    public BWTapaObject[] objArray;

    public BWTapaGameState(BWTapaGame game) {
        super(game);
        objArray = new BWTapaObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new BWTapaEmptyObject();
        for (Position p : game.pos2hint.keySet())
            set(p, new BWTapaHintObject());
    }

    public BWTapaObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public BWTapaObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, BWTapaObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, BWTapaObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(BWTapaGameMove move) {
        Position p = move.p;
        BWTapaObject objOld = get(p);
        BWTapaObject objNew = move.obj;
        if (objOld instanceof BWTapaHintObject || objOld.equals(objNew))
            return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(BWTapaGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<BWTapaObject, BWTapaObject> f = obj -> {
            if (obj instanceof BWTapaEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new BWTapaMarkerObject() : new BWTapaWallObject();
            if (obj instanceof BWTapaWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new BWTapaMarkerObject() : new BWTapaEmptyObject();
            if (obj instanceof BWTapaMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new BWTapaWallObject() : new BWTapaEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/B&W Tapa

        Summary
        Black and white Tapas

        Description
        1. Play with the same rules as Tapa with these variations:
        2. Both Black and White cells must form a single continuous region.
        3. There can't be any 2*2 of white or black cells.
    */
    private void updateIsSolved() {
        isSolved = true;
        // A number indicates how many of the surrounding tiles are filled. If a
        // tile has more than one number, it hints at multiple separated groups
        // of filled tiles.
        F<List<Integer>, List<Integer>> computeHint = filled -> {
            List<Integer> hint = new ArrayList<>();
            if (filled.isEmpty())
                hint.add(0);
            else {
                for (int j = 0; j < filled.size(); j++)
                    if (j == 0 || filled.get(j) - filled.get(j - 1) != 1)
                        hint.add(1);
                    else
                        hint.set(hint.size() - 1, hint.get(hint.size() - 1) + 1);
                if (filled.size() > 1 && hint.size() > 1 && filled.get(filled.size() - 1) - filled.get(0) == 7) {
                    hint.set(0, hint.get(0) + hint.get(hint.size() - 1));
                    hint.remove(hint.size() - 1);
                }
                Collections.sort(hint);
                // hint.sort(Comparator.naturalOrder());
            }
            return hint;
        };
        F2<List<Integer>, List<Integer>, Boolean> isCompatible = (computedHint, givenHint) -> {
            if (computedHint.equals(givenHint)) return true;
            if (computedHint.size() != givenHint.size()) return false;
            Set<Integer> h1 = new HashSet<>(computedHint);
            Set<Integer> h2 = new HashSet<>(givenHint);
            h2.remove(-1);
            return h1.containsAll(h2);
        };
        fromMap(game.pos2hint).foreachDoEffect(kv -> {
            Position p = kv._1();
            List<Integer> arr2 = kv._2();
            List<Integer> filled = range(0, 8).filter(i -> {
                Position p2 = p.add(BWTapaGame.offset[i]);
                return isValid(p2) && get(p2) instanceof BWTapaWallObject;
            }).toJavaList();
            List<Integer> arr = computeHint.f(filled);
            HintState s = arr.size() == 1 && arr.get(0) == 0 ? HintState.Normal :
                    isCompatible.f(arr, arr2) ? HintState.Complete : HintState.Error;
            BWTapaHintObject o = new BWTapaHintObject();
            o.state = s;
            set(p, o);
            if (s != HintState.Complete) isSolved = false;
        });
        if (!isSolved) return;
        // 3. There can't be any 2*2 of white or black cells.
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                if (array(BWTapaGame.offset2).forall(os -> {
                    BWTapaObject o = get(p.add(os));
                    return o instanceof BWTapaWallObject;
                }) || array(BWTapaGame.offset2).forall(os -> {
                    BWTapaObject o = get(p.add(os));
                    return o instanceof BWTapaEmptyObject || o instanceof BWTapaHintObject;
                })) {
                    isSolved = false; return;
                }
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
                if (get(p) instanceof BWTapaWallObject)
                    rngWalls.add(p);
                else
                    rngEmpty.add(p);
            }
        for (Position p : rngWalls)
            for (Position os : BWTapaGame.offset) {
                Position p2 = p.add(os);
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        for (Position p : rngEmpty)
            for (Position os : BWTapaGame.offset) {
                Position p2 = p.add(os);
                if (rngEmpty.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        // 2. Both Black and White cells must form a single continuous region.
        g.setRootNode(pos2node.get(rngWalls.get(0)));
        List<Node> nodeList = g.bfs();
        if (rngWalls.size() != nodeList.size()) {isSolved = false; return;}
        g.setRootNode(pos2node.get(rngEmpty.get(0)));
        nodeList = g.bfs();
        if (rngEmpty.size() != nodeList.size()) isSolved = false;
    }
}
