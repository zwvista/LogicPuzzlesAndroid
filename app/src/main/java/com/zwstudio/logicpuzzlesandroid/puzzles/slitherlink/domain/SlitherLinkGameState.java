package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.Array.arrayArray;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SlitherLinkGameState extends CellsGameState<SlitherLinkGame, SlitherLinkGameMove, SlitherLinkGameState> {
    public GridLineObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public SlitherLinkGameState(SlitherLinkGame game) {
        super(game);
        objArray = new GridLineObject[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new GridLineObject[4];
            Arrays.fill(objArray[i], GridLineObject.Empty);
        }
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

    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            if (get(p)[1] == GridLineObject.Line) n1++;
            if (get(p)[2] == GridLineObject.Line) n1++;
            if (get(p.add(new Position(1, 1)))[0] == GridLineObject.Line) n1++;
            if (get(p.add(new Position(1, 1)))[3] == GridLineObject.Line) n1++;
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = arrayArray(get(p)).filter(o -> o == GridLineObject.Line).length();
                switch (n) {
                case 0:
                    continue;
                case 2:
                    {
                        Node node = new Node(p.toString());
                        g.addNode(node);
                        pos2node.put(p, node);
                    }
                    break;
                default:
                    isSolved = false;
                    return;
                }
            }

        for (Position p : pos2node.keySet()) {
            GridLineObject[] dotObj = get(p);
            for (int i = 0; i < 4; i++) {
                if (dotObj[i] != GridLineObject.Line) continue;
                Position p2 = p.add(SlitherLinkGame.offset[i]);
                g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(SlitherLinkGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        GridLineObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(SlitherLinkGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(SlitherLinkGameMove move, MarkerOptions markerOption) {
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
}
