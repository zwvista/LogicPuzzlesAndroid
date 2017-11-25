package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain;

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
import static fj.data.List.iterableList;
import static fj.data.Stream.range;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TapaIslandsGameState extends CellsGameState<TapaIslandsGame, TapaIslandsGameMove, TapaIslandsGameState> {
    public TapaIslandsObject[] objArray;

    public TapaIslandsGameState(TapaIslandsGame game) {
        super(game);
        objArray = new TapaIslandsObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new TapaIslandsEmptyObject();
        for (Position p : game.pos2hint.keySet())
            set(p, new TapaIslandsHintObject());
    }

    public TapaIslandsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public TapaIslandsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, TapaIslandsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, TapaIslandsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(TapaIslandsGameMove move) {
        Position p = move.p;
        TapaIslandsObject objOld = get(p);
        TapaIslandsObject objNew = move.obj;
        if (objOld instanceof TapaIslandsHintObject || objOld.equals(objNew))
            return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TapaIslandsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<TapaIslandsObject, TapaIslandsObject> f = obj -> {
            if (obj instanceof TapaIslandsEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TapaIslandsMarkerObject() : new TapaIslandsWallObject();
            if (obj instanceof TapaIslandsWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new TapaIslandsMarkerObject() : new TapaIslandsEmptyObject();
            if (obj instanceof TapaIslandsMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TapaIslandsWallObject() : new TapaIslandsEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/Tapa Islands

        Summary
        Tap-Archipelago

        Description
        1. Plays with the same rules as Tapa with these variations.
        2. Empty tiles from 'islands', or separated areas, are surrounded by the
           filled tiles.
        3. Each separated area may contain at most one clue tile.
        4. If there is a clue tile in an area, at least one digit should give the
           size of that area in unit squares.
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
            List<Integer> filled = range(0, 8).filter(i -> {
                Position p2 = p.add(TapaIslandsGame.offset[i]);
                return isValid(p2) && get(p2) instanceof TapaIslandsWallObject;
            }).toJavaList();
            List<Integer> arr = computeHint.f(filled);
            HintState s = arr.size() == 1 && arr.get(0) == 0 ? HintState.Normal :
                    isCompatible.f(arr, arr2) ? HintState.Complete : HintState.Error;
            TapaIslandsHintObject o = new TapaIslandsHintObject();
            o.state = s;
            set(p, o);
            if (s != HintState.Complete) isSolved = false;
        });
        if (!isSolved) return;
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                if (array(TapaIslandsGame.offset2).forall(os -> {
                    TapaIslandsObject o = get(p.add(os));
                    return o instanceof TapaIslandsWallObject;
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
                if (get(p) instanceof TapaIslandsWallObject)
                    rngWalls.add(p);
                else
                    rngEmpty.add(p);
            }
        for (Position p : rngWalls)
            for (Position os : TapaIslandsGame.offset) {
                Position p2 = p.add(os);
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        for (Position p : rngEmpty)
            for (Position os : TapaIslandsGame.offset) {
                Position p2 = p.add(os);
                if (rngEmpty.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        if (rngWalls.isEmpty()) {isSolved = false; return;}
        {
            g.setRootNode(pos2node.get(rngWalls.get(0)));
            List<Node> nodeList = g.bfs();
            if (rngWalls.size() != nodeList.size()) {isSolved = false; return;}
        }
        while (!rngEmpty.isEmpty()) {
            g.setRootNode(pos2node.get(rngEmpty.get(0)));
            List<Node> nodeList = g.bfs();
            rngEmpty = iterableList(rngEmpty).filter(p -> !nodeList.contains(pos2node.get(p))).toJavaList();
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
                List<Integer> arr2 = game.pos2hint.get(p);
                HintState s = arr2.contains(n2) ? HintState.Complete : HintState.Error;
                TapaIslandsHintObject o = new TapaIslandsHintObject();
                o.state = s;
                set(p, o);
                if (s != HintState.Complete) isSolved = false;
                break;
            }
            default:
                for (Position p : rng) {
                    TapaIslandsHintObject o = new TapaIslandsHintObject();
                    o.state = HintState.Normal;
                    set(p, o);
                }
                isSolved = false;
                break;
            }
        }
    }
}
