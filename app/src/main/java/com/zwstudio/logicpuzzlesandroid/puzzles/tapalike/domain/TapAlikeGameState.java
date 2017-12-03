package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGame;

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
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TapAlikeGameState extends CellsGameState<TapAlikeGame, TapAlikeGameMove, TapAlikeGameState> {
    public TapAlikeObject[] objArray;

    public TapAlikeGameState(TapAlikeGame game) {
        super(game);
        objArray = new TapAlikeObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new TapAlikeEmptyObject();
        for (Position p : game.pos2hint.keySet())
            set(p, new TapAlikeHintObject());
    }

    public TapAlikeObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public TapAlikeObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, TapAlikeObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, TapAlikeObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(TapAlikeGameMove move) {
        Position p = move.p;
        TapAlikeObject objOld = get(p);
        TapAlikeObject objNew = move.obj;
        if (objOld instanceof TapAlikeHintObject || objOld.equals(objNew))
            return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TapAlikeGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<TapAlikeObject, TapAlikeObject> f = obj -> {
            if (obj instanceof TapAlikeEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TapAlikeMarkerObject() : new TapAlikeWallObject();
            if (obj instanceof TapAlikeWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new TapAlikeMarkerObject() : new TapAlikeEmptyObject();
            if (obj instanceof TapAlikeMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TapAlikeWallObject() : new TapAlikeEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Tap-Alike

        Summary
        Dr. Jekyll and Mr. Tapa

        Description
        1. Plays with the same rules as Tapa with these variations:
        2. At the end of the solution, the filled tiles will form an identical
           pattern to the one formed by the empty tiles.
        3. It's basically like having the same figure rotated or reversed in the
           opposite colour. The two figures will have the same exact shape.
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
            List<Integer> filled = Stream.range(0, 8).filter(i -> {
                Position p2 = p.add(TapAlikeGame.offset[i]);
                return isValid(p2) && get(p2) instanceof TapAlikeWallObject;
            }).toJavaList();
            List<Integer> arr = computeHint.f(filled);
            HintState s = arr.size() == 1 && arr.get(0) == 0 ? HintState.Normal :
                    isCompatible.f(arr, arr2) ? HintState.Complete : HintState.Error;
            TapAlikeHintObject o = new TapAlikeHintObject();
            o.state = s;
            set(p, o);
            if (s != HintState.Complete) isSolved = false;
        });
        if (!isSolved) return;
        // Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
        // Tiles with numbers can be considered 'empty'.
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                if (array(TapAlikeGame.offset2).forall(os -> {
                    TapAlikeObject o = get(p.add(os));
                    return o instanceof TapAlikeWallObject;
                })) {
                    isSolved = false; return;
                }
            }
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) instanceof TapAlikeWallObject) {
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (Position os : TapaGame.offset) {
                Position p2 = p.add(os);
                Node node2 = pos2node.get(p2);
                if (node2 != null) g.connectNode(node, node2);
            }
        }
        // The goal is to fill some tiles forming a single orthogonally continuous
        // path. Just like Nurikabe.
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;
        // 2. At the end of the solution, the filled tiles will form an identical
        // pattern to the one formed by the empty tiles.
        // 3. It's basically like having the same figure rotated or reversed in the
        // opposite colour. The two figures will have the same exact shape.
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                TapAlikeObject o1 = get(r, c), o2 = get(rows() - 1 - r, cols() - 1 - c);
                if ((o1 instanceof TapAlikeWallObject) == (o2 instanceof TapAlikeWallObject)) {
                    isSolved = false; return;
                }
            }
    }
}
