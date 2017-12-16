package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain;

import com.rits.cloning.Cloner;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

public class FenceItUpGameState extends CellsGameState<FenceItUpGame, FenceItUpGameMove, FenceItUpGameState> {
    public GridLineObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public FenceItUpGameState(FenceItUpGame game) {
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

    public boolean setObject(FenceItUpGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        if (game.get(p1)[dir] != GridLineObject.Empty) return false;
        GridLineObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(FenceItUpGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(FenceItUpGameMove move) {
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
        iOS Game: Logic Games/Puzzle Set 7/Fence It Up

        Summary
        Now with Fences

        Description
        1. A simple puzzle where you have to divide the Board into enclosed
           areas by Fences.
        2. Each area must contain one number and the number tells you the length
           of the perimeter of the area.
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
                    if (get(p.add(FenceItUpGame.offset2[i]))[FenceItUpGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node.get(p), pos2node.get(p.add(FenceItUpGame.offset[i])));
            }
        while (!pos2node.isEmpty()) {
            g.setRootNode(iterableList(pos2node.values()).head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            for (Position p : area)
                pos2node.remove(p);
            List<Position> rng = iterableList(area).filter(p -> game.pos2hint.containsKey(p)).toJavaList();
            if (rng.size() != 1) {
                for (Position p : rng)
                    pos2state.put(p, HintState.Normal);
                isSolved = false; continue;
            }
            Position p2 = rng.get(0);
            int n1 = 0, n2 = game.pos2hint.get(p2);
            // 2. Each area must contain one number and the number tells you the length
            // of the perimeter of the area.
            for (Position p : area)
                for (int i = 0; i < 4; i++)
                    if (get(p.add(FenceItUpGame.offset2[i]))[FenceItUpGame.dirs[i]] == GridLineObject.Line)
                        n1++;
            HintState s = n1 == n2 ? HintState.Complete : HintState.Error;
            pos2state.put(p2, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
