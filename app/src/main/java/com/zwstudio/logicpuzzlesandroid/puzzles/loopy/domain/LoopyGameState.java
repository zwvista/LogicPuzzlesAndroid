package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain;

import com.rits.cloning.Cloner;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.Array.arrayArray;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LoopyGameState extends CellsGameState<LoopyGame, LoopyGameMove, LoopyGameState> {
    public GridLineObject[][] objArray;

    public LoopyGameState(LoopyGame game) {
        super(game);
        objArray = new Cloner().deepClone(game.objArray);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                for (int dir = 1; dir <= 2; dir++) {
                    if (game.get(r, c)[dir] != GridLineObject.Line) continue;
                    LoopyGameMove move = new LoopyGameMove();
                    move.p = new Position(r, c);
                    move.dir = dir;
                    move.obj = GridLineObject.Line;
                    setObject(move);
                }
    }

    public GridLineObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public GridLineObject[] get(Position p) {
        return get(p.row, p.col);
    }

    public boolean setObject(LoopyGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        GridLineObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(LoopyGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(LoopyGameMove move) {
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
        iOS Game: Logic Games/Puzzle Set 5/Loopy

        Summary
        Loop a loop! And touch all the dots!

        Description
        1. Draw a single looping path. You have to touch all the dots. As usual,
           the path cannot have branches or cross itself.
    */
    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = arrayArray(get(p)).filter(o -> o == GridLineObject.Line).length();
                switch (n) {
                    case 2:
                    {
                        Node node = new Node(p.toString());
                        g.addNode(node);
                        pos2node.put(p, node);
                        break;
                    }
                    default:
                        isSolved = false;
                        return;
                }
            }
        for (Position p : pos2node.keySet()) {
            GridLineObject[] dotObj = get(p);
            for (int i = 0; i < 4; i++) {
                if (dotObj[i] != GridLineObject.Line) continue;
                Position p2 = p.add(LoopyGame.offset[i]);
                g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2node.values().size();
        if (n1 != n2) isSolved = false;
    }
}
