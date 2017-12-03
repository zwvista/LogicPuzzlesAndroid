package com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain;

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
import fj.data.Stream;

import static fj.data.Array.array;
import static fj.data.HashMap.fromMap;

/**
 * Created by zwvista on 2016/09/29.
 */

public class PataGameState extends CellsGameState<PataGame, PataGameMove, PataGameState> {
    public PataObject[] objArray;

    public PataGameState(PataGame game) {
        super(game);
        objArray = new PataObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new PataEmptyObject();
        for (Position p : game.pos2hint.keySet())
            set(p, new PataHintObject());
    }

    public PataObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public PataObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, PataObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, PataObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(PataGameMove move) {
        Position p = move.p;
        PataObject objOld = get(p);
        PataObject objNew = move.obj;
        if (objOld instanceof PataHintObject || objOld.equals(objNew))
            return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(PataGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<PataObject, PataObject> f = obj -> {
            if (obj instanceof PataEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new PataMarkerObject() : new PataWallObject();
            if (obj instanceof PataWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new PataMarkerObject() : new PataEmptyObject();
            if (obj instanceof PataMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new PataWallObject() : new PataEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/Pata

        Summary
        Yes, it's the opposite of Tapa

        Description
        1. Plays the opposite of Tapa, regarding the hints:
        2. A number indicates the groups of connected empty tiles that are around
           it, instead of filled ones.
        3. Different groups of empty tiles are separated by at least one filled cell.
        4. Same as Tapa:
        5. The filled tiles are continuous.
        6. You can't have a 2*2 space of filled tiles.
    */
    private void updateIsSolved() {
        isSolved = true;
        // 2. A number indicates the groups of connected empty tiles that are around
        // it, instead of filled ones.
        F<List<Integer>, List<Integer>> computeHint = emptied -> {
            List<Integer> hint = new ArrayList<>();
            if (emptied.isEmpty())
                hint.add(0);
            else {
                for (int j = 0; j < emptied.size(); j++)
                    if (j == 0 || emptied.get(j) - emptied.get(j - 1) != 1)
                        hint.add(1);
                    else
                        hint.set(hint.size() - 1, hint.get(hint.size() - 1) + 1);
                if (emptied.size() > 1 && hint.size() > 1 && emptied.get(emptied.size() - 1) - emptied.get(0) == 7) {
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
            List<Integer> emptied = Stream.range(0, 8).filter(i -> {
                Position p2 = p.add(PataGame.offset[i]);
                if (!isValid(p2)) return false;
                PataObject o = get(p2);
                return o instanceof PataEmptyObject || o instanceof PataHintObject;
            }).toJavaList();
            List<Integer> arr = computeHint.f(emptied);
            List<Integer> filled = Stream.range(0, 8).filter(i -> {
                Position p2 = p.add(PataGame.offset[i]);
                return isValid(p2) && get(p2) instanceof PataWallObject;
            }).toJavaList();
            List<Integer> arr3 = computeHint.f(filled);
            HintState s = arr3.size() == 1 && arr3.get(0) == 0 ? HintState.Normal :
                    isCompatible.f(arr, arr2) ? HintState.Complete : HintState.Error;
            PataHintObject o = new PataHintObject();
            o.state = s;
            set(p, o);
            if (s != HintState.Complete) isSolved = false;
        });
        if (!isSolved) return;
        // 6. You can't have a 2*2 space of filled tiles.
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                if (array(PataGame.offset2).forall(os -> {
                    PataObject o = get(p.add(os));
                    return o instanceof PataWallObject;
                })) {
                    isSolved = false; return;
                }
            }
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        List<Position> rngWalls = new ArrayList<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
                if (get(p) instanceof PataWallObject)
                    rngWalls.add(p);
            }
        for (Position p : rngWalls)
            for (Position os : PataGame.offset) {
                Position p2 = p.add(os);
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        // 5. The filled tiles are continuous.
        g.setRootNode(pos2node.get(rngWalls.get(0)));
        List<Node> nodeList = g.bfs();
        if (rngWalls.size() != nodeList.size()) isSolved = false;
    }
}
