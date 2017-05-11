package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain;

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
import fj.F0;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BoxItUpGameState extends CellsGameState<BoxItUpGame, BoxItUpGameMove, BoxItUpGameState> {
    public GridLineObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public BoxItUpGameState(BoxItUpGame game) {
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

    public boolean setObject(BoxItUpGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        if (game.get(p1)[dir] == GridLineObject.Line) return false;
        GridLineObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(BoxItUpGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(BoxItUpGameMove move, MarkerOptions markerOption) {
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
                    if (get(p.add(BoxItUpGame.offset2[i]))[BoxItUpGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node.get(p), pos2node.get(p.add(BoxItUpGame.offset[i])));
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
            int n1 = area.size(), n2 = game.pos2hint.get(p2);
            int r2 = 0, r1 = rows(), c2 = 0, c1 = cols();
            for (Position p : area) {
                if (r2 < p.row) r2 = p.row;
                if (r1 > p.row) r1 = p.row;
                if (c2 < p.col) c2 = p.col;
                if (c1 > p.col) c1 = p.col;
            }
            int rs = r2 - r1 + 1, cs = c2 - c1 + 1;
            int r11 = r1, r22 = r2, c11 = c1, c22 = c2;
            F0<Boolean> hasLine = () -> {
                for (int r = r11; r <= r22; r++)
                    for (int c = c11; c <= c22; c++) {
                        GridLineObject[] dotObj = get(r + 1, c + 1);
                        if (r < r22 && dotObj[3] == GridLineObject.Line || c < c22 && dotObj[0] == GridLineObject.Line)
                            return true;
                    }
                return false;
            };
            HintState s = rs * cs == n1 && rs * cs == n2 && !hasLine.f() ? HintState.Complete : HintState.Error;
            pos2state.put(p2, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
