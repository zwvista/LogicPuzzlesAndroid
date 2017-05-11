package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain;

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
import fj.Ord;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class FenceLitsGameState extends CellsGameState<FenceLitsGame, FenceLitsGameMove, FenceLitsGameState> {
    public GridLineObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public FenceLitsGameState(FenceLitsGame game) {
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

    public boolean setObject(FenceLitsGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        GridLineObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(FenceLitsGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(FenceLitsGameMove move, MarkerOptions markerOption) {
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
                    if (get(p.add(FenceLitsGame.offset2[i]))[FenceLitsGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node.get(p), pos2node.get(p.add(FenceLitsGame.offset[i])));
            }
        List<List<Integer>> fencelitses = new ArrayList<>();
        while (!pos2node.isEmpty()) {
            g.setRootNode(fromMap(pos2node).values().head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            if (area.size() != 2) {isSolved = false; return;}
            List<Integer> fencelits = iterableList(area).map(p -> game.pos2hint.get(p)).sort(Ord.intOrd).toJavaList();
            if (fencelitses.contains(fencelits)) {isSolved = false; return;}
            fencelitses.add(fencelits);
            for (Position p : area)
                pos2node.remove(p);

        }
    }
}
