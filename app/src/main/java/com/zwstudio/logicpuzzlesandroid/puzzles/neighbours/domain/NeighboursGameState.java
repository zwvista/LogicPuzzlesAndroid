package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domain;

import com.rits.cloning.Cloner;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fj.F;
import fj.F0;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class NeighboursGameState extends CellsGameState<NeighboursGame, NeighboursGameMove, NeighboursGameState> {
    public GridLineObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public NeighboursGameState(NeighboursGame game) {
        super(game);
        objArray = new Cloner().deepClone(game.objArray);
        updateIsSolved();
    }

    public GridLineObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public GridLineObject[] get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, GridLineObject[] dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, GridLineObject[] obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(NeighboursGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        if (game.get(p1)[dir] == GridLineObject.Line) return false;
        GridLineObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(NeighboursGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(NeighboursGameMove move, MarkerOptions markerOption) {
        F<GridLineObject, GridLineObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        GridLineObject.Marker : GridLineObject.Line;
            case Line:
                return markerOption == MarkerOptions.MarkerLast ?
                        GridLineObject.Marker : GridLineObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        GridLineObject.Line : GridLineObject.Empty;
            }
            return obj;
        };
        GridLineObject[] dotObj = get(move.p);
        move.obj = f.f(dotObj[move.dir]);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 8/Neighbours

        Summary
        Neighbours, yes, but not equally sociable

        Description
        1. The board represents a piece of land bought by a bunch of people. They
           decided to split the land in equal parts.
        2. However some people are more social and some are less, so each owner
           wants an exact number of neighbours around him.
        3. Each number on the board represents an owner house and the number of
           neighbours he desires.
        4. Divide the land so that each one has an equal number of squares and
           the requested number of neighbours.
        5. Later on, there will be Question Marks, which represents an owner for
           which you don't know the neighbours preference.
    */
    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
            }
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                for (int i = 0; i < 4; i++)
                    if (get(p.add(NeighboursGame.offset2[i]))[NeighboursGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node.get(p), pos2node.get(p.add(NeighboursGame.offset[i])));
            }
        List<List<Position>> areas = new ArrayList<>();
        Map<Position, Integer> pos2area = new HashMap<>();
        while (!pos2node.isEmpty()) {
            g.setRootNode(iterableList(pos2node.values()).head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            areas.add(area);
            for (Position p : area) {
                pos2area.put(p, areas.size());
                pos2node.remove(p);
            }
        }
        int n2 = game.areaSize;
        for (List<Position> area : areas) {
            List<Position> rng = iterableList(area).filter(p -> game.pos2hint.containsKey(p)).toJavaList();
            if (rng.size() != 1) {
                for (Position p : rng)
                    pos2state.put(p, HintState.Normal);
                isSolved = false; continue;
            }
            Position p3 = rng.get(0);
            int n1 = area.size(), n3 = game.pos2hint.get(p3);
            F0<Integer> neighbours = () -> {
                Set<Integer> indexes = new HashSet<>();
                Integer idx = pos2area.get(area.get(0));
                for (Position p : area)
                    for (int i = 0; i < 4; i++) {
                        if (get(p.add(NeighboursGame.offset2[i]))[NeighboursGame.dirs[i]] != GridLineObject.Line) continue;
                        Position p2 = p.add(NeighboursGame.offset[i]);
                        Integer idx2 = pos2area.get(p2);
                        if (idx2 == null) continue;
                        if (idx == idx2) return -1;
                        indexes.add(idx2);
                    }
                return indexes.size();
            };
            HintState s = n1 == n2 && n3 == neighbours.f() ? HintState.Complete : HintState.Error;
            pos2state.put(p3, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
