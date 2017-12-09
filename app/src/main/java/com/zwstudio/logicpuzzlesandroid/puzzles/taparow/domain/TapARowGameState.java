package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain;

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
import fj.function.Integers;

import static fj.data.Array.array;
import static fj.data.Array.iterableArray;
import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TapARowGameState extends CellsGameState<TapARowGame, TapARowGameMove, TapARowGameState> {
    public TapARowObject[] objArray;

    public TapARowGameState(TapARowGame game) {
        super(game);
        objArray = new TapARowObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new TapARowEmptyObject();
        for (Position p : game.pos2hint.keySet())
            set(p, new TapARowHintObject());
    }

    public TapARowObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public TapARowObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, TapARowObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, TapARowObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(TapARowGameMove move) {
        Position p = move.p;
        TapARowObject objOld = get(p);
        TapARowObject objNew = move.obj;
        if (objOld instanceof TapARowHintObject || objOld.equals(objNew))
            return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TapARowGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<TapARowObject, TapARowObject> f = obj -> {
            if (obj instanceof TapARowEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TapARowMarkerObject() : new TapARowWallObject();
            if (obj instanceof TapARowWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new TapARowMarkerObject() : new TapARowEmptyObject();
            if (obj instanceof TapARowMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TapARowWallObject() : new TapARowEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Tap-A-Row

        Summary
        Tap me a row, please

        Description
        1. Plays with the same rules as Tapa with these variations:
        2. The number also tells you the filled cell count for that row.
        3. In other words, the sum of the digits in that row equals the number
           of that row.
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
                Position p2 = p.add(TapARowGame.offset[i]);
                return isValid(p2) && get(p2) instanceof TapARowWallObject;
            }).toJavaList();
            List<Integer> arr = computeHint.f(filled);
            HintState s = arr.size() == 1 && arr.get(0) == 0 ? HintState.Normal :
                    isCompatible.f(arr, arr2) ? HintState.Complete : HintState.Error;
            TapARowHintObject o = new TapARowHintObject();
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
                if (array(TapARowGame.offset2).forall(os -> {
                    TapARowObject o = get(p.add(os));
                    return o instanceof TapARowWallObject;
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
                if (get(p) instanceof TapARowWallObject) {
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
        // 2. The number also tells you the filled cell count for that row.
        // 3. In other words, the sum of the digits in that row equals the number
        // of that row.
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = 0;
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                TapARowObject o = get(p);
                if (o instanceof TapARowWallObject)
                    n1++;
                else if (o instanceof TapARowHintObject) {
                    List<Integer> arr = game.pos2hint.get(p);
                    n2 += iterableArray(arr).foldLeft(Integers.add, 0);
                }
            }
            if (n2 != 0 && n1 != n2) {isSolved = false; return;}
        }
    }
}
