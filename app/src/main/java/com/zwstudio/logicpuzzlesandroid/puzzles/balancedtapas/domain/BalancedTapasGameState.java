package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain;

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

public class BalancedTapasGameState extends CellsGameState<BalancedTapasGame, BalancedTapasGameMove, BalancedTapasGameState> {
    public BalancedTapasObject[] objArray;

    public BalancedTapasGameState(BalancedTapasGame game) {
        super(game);
        objArray = new BalancedTapasObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new BalancedTapasEmptyObject();
        for (Position p : game.pos2hint.keySet())
            set(p, new BalancedTapasHintObject());
    }

    public BalancedTapasObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public BalancedTapasObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, BalancedTapasObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, BalancedTapasObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(BalancedTapasGameMove move) {
        Position p = move.p;
        BalancedTapasObject objOld = get(p);
        BalancedTapasObject objNew = move.obj;
        if (objOld instanceof BalancedTapasHintObject || objOld.equals(objNew))
            return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(BalancedTapasGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<BalancedTapasObject, BalancedTapasObject> f = obj -> {
            if (obj instanceof BalancedTapasEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new BalancedTapasMarkerObject() : new BalancedTapasWallObject();
            if (obj instanceof BalancedTapasWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new BalancedTapasMarkerObject() : new BalancedTapasEmptyObject();
            if (obj instanceof BalancedTapasMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new BalancedTapasWallObject() : new BalancedTapasEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/BalancedTapas

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
        5. BalancedTapas has plenty of variations. Some are available in the levels of this
           game. Stronger variations are B-W BalancedTapas, Island BalancedTapas and Pata and have
           their own game.
        6. Equal BalancedTapas - The board contains an equal number of white and black tiles.
           Tiles with numbers or question marks are NOT counted as empty or filled
           for this rule (i.e. they're left out of the count).
        7. Four-Me-BalancedTapas - Four-Me-Not rule apply: you can't have more than three
           filled tiles in line.
        8. No Square BalancedTapas - No 2*2 area of the board can be left empty.
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
            Position p = kv._1();
            List<Integer> arr2 = kv._2();
            List<Integer> filled = Stream.range(0, 8).filter(i -> {
                Position p2 = p.add(BalancedTapasGame.offset[i]);
                return isValid(p2) && get(p2) instanceof BalancedTapasWallObject;
            }).toJavaList();
            List<Integer> arr = computeHint.f(filled);
            HintState s = arr.size() == 1 && arr.get(0) == 0 ? HintState.Normal :
                    isCompatible.f(arr, arr2) ? HintState.Complete : HintState.Error;
            BalancedTapasHintObject o = new BalancedTapasHintObject();
            o.state = s;
            set(p, o);
            if (s != HintState.Complete) isSolved = false;
        });
        if (!isSolved) return;
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                if (array(BalancedTapasGame.offset2).forall(os -> {
                    BalancedTapasObject o = get(p.add(os));
                    return o instanceof BalancedTapasWallObject;
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
                if (get(p) instanceof BalancedTapasWallObject)
                    rngWalls.add(p);
            }
        for (Position p : rngWalls)
            for (Position os : BalancedTapasGame.offset) {
                Position p2 = p.add(os);
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        g.setRootNode(pos2node.get(rngWalls.get(0)));
        List<Node> nodeList = g.bfs();
        if (rngWalls.size() != nodeList.size()) isSolved = false;
    }
}
