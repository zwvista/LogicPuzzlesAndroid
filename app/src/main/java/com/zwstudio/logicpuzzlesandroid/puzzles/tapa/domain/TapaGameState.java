package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain;

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

public class TapaGameState extends CellsGameState<TapaGame, TapaGameMove, TapaGameState> {
    public TapaObject[] objArray;

    public TapaGameState(TapaGame game) {
        super(game);
        objArray = new TapaObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new TapaEmptyObject();
        for (Position p : game.pos2hint.keySet())
            set(p, new TapaHintObject());
    }

    public TapaObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public TapaObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, TapaObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, TapaObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(TapaGameMove move) {
        Position p = move.p;
        TapaObject objOld = get(p);
        TapaObject objNew = move.obj;
        if (objOld instanceof TapaHintObject || objOld.toString().equals(objNew.toString()))
            return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TapaGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<TapaObject, TapaObject> f = obj -> {
            if (obj instanceof TapaEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TapaMarkerObject() : new TapaWallObject();
            if (obj instanceof TapaWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new TapaMarkerObject() : new TapaEmptyObject();
            if (obj instanceof TapaMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TapaWallObject() : new TapaEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/Tapa

        Summary
        Turkish art of PAint(TAPA)

        Description
        1. The goal is to fill some tiles forming a single orthogonally continuous
           path. Just like Nurikabe.
        2. A number indicates how many of the surrounding tiles are filled. If a
           tile has more than one number, it hints at multiple separated groups
           of filled tiles.
        3. For example, a cell with a 1 and 3 means there is a continuous group
           of 3 filled cells around it and one more single filled cell, separated
           from the other 3. The order of the numbers in this case is irrelevant.
        4. Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
           Tiles with numbers can be considered 'empty'.

        Variations
        5. Tapa has plenty of variations. Some are available in the levels of this
           game. Stronger variations are B-W Tapa, Island Tapa and Pata and have
           their own game.
        6. Equal Tapa - The board contains an equal number of white and black tiles.
           Tiles with numbers or question marks are NOT counted as empty or filled
           for this rule (i.e. they're left out of the count).
        7. Four-Me-Tapa - Four-Me-Not rule apply: you can't have more than three
           filled tiles in line.
        8. No Square Tapa - No 2*2 area of the board can be left empty.
    */
    private void updateIsSolved() {
        isSolved = true;
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
            List<Integer> filled = Stream.range(0, 7).filter(i -> {
                Position p2 = kv._1().add(TapaGame.offset[i]);
                return isValid(p2) && get(p2).toString().equals(new TapaWallObject().toString());
            }).toJavaList();
            List<Integer> arr = computeHint.f(filled);
            HintState s = arr.size() == 1 && arr.get(0) == 0 ? HintState.Normal :
                    isCompatible.f(arr, kv._2()) ? HintState.Complete : HintState.Error;
            TapaHintObject o = new TapaHintObject();
            o.state = s;
            set(kv._1(), o);
            if (s != HintState.Complete) isSolved = false;
        });
        if (!isSolved) return;
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                if (array(TapaGame.offset2).forall(os -> {
                    TapaObject o = get(p.add(os));
                    return o instanceof TapaWallObject;
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
                if (get(p) instanceof TapaWallObject)
                    rngWalls.add(p);
            }
        for (Position p : rngWalls)
            for (Position os : TapaGame.offset) {
                Position p2 = p.add(os);
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        g.setRootNode(pos2node.get(rngWalls.get(0)));
        List<Node> nodeList = g.bfs();
        if (rngWalls.size() != nodeList.size()) isSolved = false;
    }
}
