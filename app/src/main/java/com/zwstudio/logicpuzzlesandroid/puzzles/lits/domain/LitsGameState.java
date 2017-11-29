package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fj.F;
import fj.Ord;
import fj.data.Option;
import fj.function.Effect1;

import static fj.data.Array.array;
import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LitsGameState extends CellsGameState<LitsGame, LitsGameMove, LitsGameState> {
    public LitsObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public LitsGameState(LitsGame game) {
        super(game);
        objArray = new LitsObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new LitsEmptyObject();
        updateIsSolved();
    }

    public LitsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public LitsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, LitsObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, LitsObject obj) {
        set(p.row, p.col, obj);
    }

    private class LitsAreaInfo {
        List<Position> trees = new ArrayList<>();
        Set<Integer> blockIndexes = new HashSet<>();
        Set<Integer> neighborIndexes = new HashSet<>();
        int tetrominoIndex = -1;
    }

    public boolean setObject(LitsGameMove move) {
        if (!isValid(move.p) || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(LitsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<LitsObject, LitsObject> f = obj -> {
            if (obj instanceof LitsEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new LitsMarkerObject() : new LitsTreeObject();
            if (obj instanceof LitsTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new LitsMarkerObject() : new LitsEmptyObject();
            if (obj instanceof LitsMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new LitsTreeObject() : new LitsEmptyObject();
            return obj;
        };
        LitsObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 12/Lits

        Summary
        Tetris without the fat guy

        Description
        1. You play the game with all the Tetris pieces, except the square one.
        2. So in other words you use pieces of four squares (tetrominoes) in the
           shape of L, I, T and S, which can also be rotated or reflected (mirrored).
        3. The board is divided into many areas. You have to place a tetromino
           into each area respecting these rules:
        4. No two adjacent (touching horizontally / vertically) tetromino should
           be of equal shape, even counting rotations or reflections.
        5. All the shaded cells should form a valid Nurikabe (hence no fat guy).
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                LitsObject o = get(p);
                if (o instanceof LitsForbiddenObject)
                    set(r, c, new LitsEmptyObject());
                else if(o instanceof LitsTreeObject) {
                    LitsTreeObject o2 = (LitsTreeObject)o;
                    o2.state = AllowedObjectState.Normal;
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (Position os : LitsGame.offset) {
                Position p2 = p.add(os);
                Node node2 = pos2node.get(p2);
                if (node2 != null) g.connectNode(node, node2);
            }
        }
        List<List<Position>> blocks = new ArrayList<>();
        while (!pos2node.isEmpty()) {
            g.setRootNode(iterableList(pos2node.values()).head());
            List<Node> nodeList = g.bfs();
            List<Position> block = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            blocks.add(block);
            for (Position p : block)
                pos2node.remove(p);
        }
        // 5. All the shaded cells should form a valid Nurikabe.
        if (blocks.size() != 1) isSolved = false;
        List<LitsAreaInfo> infos = new ArrayList<>();
        for (int i = 0; i < game.areas.size(); i++)
            infos.add(new LitsAreaInfo());
        for (int i = 0; i < blocks.size(); i++) {
            List<Position> block = blocks.get(i);
            for (Position p : block) {
                Integer n = game.pos2area.get(p);
                LitsAreaInfo info = infos.get(n);
                info.trees.add(p);
                info.blockIndexes.add(i);
            }
        }
        for (int i = 0; i < infos.size(); i++) {
            LitsAreaInfo info = infos.get(i);
            for (Position p : info.trees)
                for (Position os : LitsGame.offset) {
                    Position p2 = p.add(os);
                    Option<Integer> index = iterableList(infos).toStream().indexOf(info2 -> info2.trees.contains(p2));
                    if (index.isSome() && index.some() != i)
                        info.neighborIndexes.add(index.some());
                }
        }
        Effect1<LitsAreaInfo> notSolved = info -> {
            isSolved = false;
            for (Position p : info.trees) {
                LitsTreeObject o = (LitsTreeObject)get(p);
                o.state = AllowedObjectState.Error;
            }
        };
        for (int i = 0; i < infos.size(); i++) {
            LitsAreaInfo info = infos.get(i);
            int treeCount = info.trees.size();
            if (treeCount >= 4 && allowedObjectsOnly)
                for (Position p : game.areas.get(i)) {
                    LitsObject o = get(p);
                    if (o instanceof LitsEmptyObject || o instanceof LitsMarkerObject)
                        set(p, new LitsForbiddenObject());
                }
            if (treeCount > 4 || treeCount == 4 && info.blockIndexes.size() > 1) notSolved.f(info);
            // 3. The board is divided into many areas. You have to place a tetromino
            // into each area.
            if (treeCount == 4 && info.blockIndexes.size() == 1) {
                Collections.sort(info.trees, Position::compareTo);
                List<Position> treeOffsets = new ArrayList<>();
                Position p2 = new Position(iterableList(info.trees).map(p -> p.row).minimum(Ord.intOrd),
                        iterableList(info.trees).map(p -> p.col).minimum(Ord.intOrd));
                for (Position p : info.trees)
                    treeOffsets.add(p.subtract(p2));
                info.tetrominoIndex = array(LitsGame.tetrominoes).toStream()
                        .indexOf(arr -> array(arr).exists(arr2 -> Arrays.equals(arr2, treeOffsets.toArray()))).orSome(-1);
                if (info.tetrominoIndex == -1) notSolved.f(info);
            }
            if (treeCount < 4) isSolved = false;
        }
        // 4. No two adjacent (touching horizontally / vertically) tetromino should
        // be of equal shape, even counting rotations or reflections.
        for (int i = 0; i < infos.size(); i++) {
            LitsAreaInfo info = infos.get(i);
            int index = info.tetrominoIndex;
            if (index == -1) continue;
            if (iterableList(info.neighborIndexes).exists(j -> infos.get(j).tetrominoIndex == index)) notSolved.f(info);
        }
        if (!isSolved) return;
        List<Position> block = blocks.get(0);
        // 5. All the shaded cells should form a valid Nurikabe.
        rule2x2: for (Position p : block) {
            for (Position os : LitsGame.offset3)
                if (block.contains(p.add(os)))
                    continue rule2x2;
            isSolved = false;
            for (Position os : LitsGame.offset3) {
                LitsTreeObject o = new LitsTreeObject();
                o.state = AllowedObjectState.Error;
                set(p.add(os), o);
            }
        }
    }
}
