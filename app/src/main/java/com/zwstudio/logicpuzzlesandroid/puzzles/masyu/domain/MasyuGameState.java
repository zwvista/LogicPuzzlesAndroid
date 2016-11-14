package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

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

public class MasyuGameState extends CellsGameState<MasyuGame, MasyuGameMove, MasyuGameState> {
    public MasyuObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public MasyuGameState(MasyuGame game) {
        super(game);
        objArray = new MasyuObject[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new MasyuObject[4];
            Arrays.fill(objArray[i], MasyuObject.Empty);
        }
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            pos2state.put(p, n == 0 ? HintState.Complete : HintState.Normal);
        }
    }

    public MasyuObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public MasyuObject[] get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, MasyuObject[] dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, MasyuObject[] obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            if (get(p)[1] == MasyuObject.Line) n1++;
            if (get(p)[2] == MasyuObject.Line) n1++;
            if (get(p.add(new Position(1, 1)))[0] == MasyuObject.Line) n1++;
            if (get(p.add(new Position(1, 1)))[3] == MasyuObject.Line) n1++;
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2Node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = arrayArray(get(p)).filter(o -> o == MasyuObject.Line).length();
                switch (n) {
                case 0:
                    continue;
                case 2:
                    {
                        Node node = new Node(p.toString());
                        g.addNode(node);
                        pos2Node.put(p, node);
                    }
                    break;
                default:
                    isSolved = false;
                    return;
                }
            }

        for (Position p : pos2Node.keySet()) {
            MasyuObject[] dotObj = get(p);
            for (int i = 0; i < 4; i++) {
                if (dotObj[i] != MasyuObject.Line) continue;
                Position p2 = p.add(MasyuGame.offset[i]);
                g.connectNode(pos2Node.get(p), pos2Node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2Node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2Node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(MasyuGameMove move) {
        Position p1 = move.p;
        boolean isH = move.objOrientation == MasyuObjectOrientation.Horizontal;
        int i1 = isH ? 1 : 2;
        MasyuObject o = get(p1)[i1];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(MasyuGame.offset[isH ? 1 : 2]);
        int i2 = isH ? 3 : 0;
        get(p2)[i2] = get(p1)[i1] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(MasyuMarkerOptions markerOption, MasyuGameMove move) {
        F<MasyuObject, MasyuObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MasyuMarkerOptions.MarkerBeforeLine ?
                        MasyuObject.Marker : MasyuObject.Line;
            case Line:
                return markerOption == MasyuMarkerOptions.MarkerAfterLine ?
                        MasyuObject.Marker : MasyuObject.Empty;
            case Marker:
                return markerOption == MasyuMarkerOptions.MarkerBeforeLine ?
                        MasyuObject.Line : MasyuObject.Empty;
            }
            return obj;
        };
        MasyuObject[] dotObj = get(move.p);
        move.obj = f.f(dotObj[move.objOrientation == MasyuObjectOrientation.Horizontal ? 1 : 2]);
        return setObject(move);
    }
}
