package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain;

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
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class GalaxiesGameState extends CellsGameState<GalaxiesGame, GalaxiesGameMove, GalaxiesGameState> {
    public GridLineObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public GalaxiesGameState(GalaxiesGame game) {
        super(game);
        objArray = new Cloner().deepClone(game.objArray);
        for (Position p : game.galaxies)
            pos2state.put(p, HintState.Normal);
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

    public boolean setObject(GalaxiesGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        if (game.get(p1)[dir] != GridLineObject.Empty) return false;
        GridLineObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(GalaxiesGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(GalaxiesGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
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
        iOS Game: Logic Games/Puzzle Set 4/Galaxies

        Summary
        Fill the Symmetric Spiral Galaxies

        Description
        1. In the board there are marked centers of a few 'Spiral' Galaxies.
        2. These Galaxies are symmetrical to a rotation of 180 degrees. This
           means that rotating the shape of the Galaxy by 180 degrees (half a
           full turn) around the center, will result in an identical shape.
        3. In the end, all the space must be included in Galaxies and Galaxies
           can't overlap.
        4. There can be single tile Galaxies (with the center inside it) and
           some Galaxy center will be cross two or four tiles.
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
                    if (get(p.add(GalaxiesGame.offset2[i]))[GalaxiesGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node.get(p), pos2node.get(p.add(GalaxiesGame.offset[i])));
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
        int n1 = 0;
        for (List<Position> area : areas) {
            List<Position> rng = iterableList(game.galaxies).filter(p -> area.contains(new Position(p.row / 2, p.col / 2))).toJavaList();
            if (rng.size() != 1) {
                // 3. Galaxies can't overlap.
                for (Position p : rng)
                    pos2state.put(p, HintState.Normal);
                isSolved = false;
            } else {
                // 2. These Galaxies are symmetrical to a rotation of 180 degrees. This
                // means that rotating the shape of the Galaxy by 180 degrees (half a
                // full turn) around the center, will result in an identical shape.
                Position galaxy = rng.get(0);
                boolean b = iterableList(area).forall(p -> area.contains(new Position(galaxy.row - p.row - 1, galaxy.col - p.col - 1)));
                HintState s = b ? HintState.Complete : HintState.Error;
                pos2state.put(galaxy, s);
                if (!b) isSolved = false;
            }
            n1 += area.size();
        }
        // 3. In the end, all the space must be included in Galaxies
        if (n1 != rows() * cols()) isSolved = false;
    }
}
