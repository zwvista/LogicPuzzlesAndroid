package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain;

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

public class MosaikGameState extends CellsGameState<MosaikGame, MosaikGameMove, MosaikGameState> {
    public MosaikObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public MosaikGameState(MosaikGame game) {
        super(game);
        objArray = new MosaikObject[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new MosaikObject[4];
            Arrays.fill(objArray[i], MosaikObject.Empty);
        }
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            pos2state.put(p, n == 0 ? HintState.Complete : HintState.Normal);
        }
    }

    public MosaikObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public MosaikObject[] get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, MosaikObject[] dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, MosaikObject[] obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            if (get(p)[1] == MosaikObject.Line) n1++;
            if (get(p)[2] == MosaikObject.Line) n1++;
            if (get(p.add(new Position(1, 1)))[0] == MosaikObject.Line) n1++;
            if (get(p.add(new Position(1, 1)))[3] == MosaikObject.Line) n1++;
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2Node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = arrayArray(get(p)).filter(o -> o == MosaikObject.Line).length();
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
            MosaikObject[] dotObj = get(p);
            for (int i = 0; i < 4; i++) {
                if (dotObj[i] != MosaikObject.Line) continue;
                Position p2 = p.add(MosaikGame.offset[i]);
                g.connectNode(pos2Node.get(p), pos2Node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2Node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2Node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(MosaikGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        MosaikObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(MosaikGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(MosaikMarkerOptions markerOption, MosaikGameMove move) {
        F<MosaikObject, MosaikObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MosaikMarkerOptions.MarkerBeforeLine ?
                        MosaikObject.Marker : MosaikObject.Line;
            case Line:
                return markerOption == MosaikMarkerOptions.MarkerAfterLine ?
                        MosaikObject.Marker : MosaikObject.Empty;
            case Marker:
                return markerOption == MosaikMarkerOptions.MarkerBeforeLine ?
                        MosaikObject.Line : MosaikObject.Empty;
            }
            return obj;
        };
        MosaikObject[] dotObj = get(move.p);
        move.obj = f.f(dotObj[move.dir]);
        return setObject(move);
    }
}
