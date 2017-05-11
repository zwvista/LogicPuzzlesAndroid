package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class CloudsGameState extends CellsGameState<CloudsGame, CloudsGameMove, CloudsGameState> {
    public CloudsObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public CloudsGameState(CloudsGame game) {
        super(game);
        objArray = new CloudsObject[rows() * cols()];
        Arrays.fill(objArray, CloudsObject.Empty);
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved();
    }

    public CloudsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public CloudsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, CloudsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, CloudsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(CloudsGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(CloudsGameMove move, MarkerOptions markerOption) {
        F<CloudsObject, CloudsObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        CloudsObject.Marker : CloudsObject.Cloud;
            case Cloud:
                return markerOption == MarkerOptions.MarkerLast ?
                        CloudsObject.Marker : CloudsObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        CloudsObject.Cloud : CloudsObject.Empty;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == CloudsObject.Cloud)
                    n1++;
            row2state[r] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++)
                if (get(r, c) == CloudsObject.Cloud)
                    n1++;
            col2state[c] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) != CloudsObject.Cloud) continue;
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
            }
        for (Position p : pos2node.keySet())
            for (Position os : CloudsGame.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        while (!pos2node.isEmpty()) {
            g.setRootNode(iterableList(pos2node.values()).head());
            List<Node> nodeList = g.bfs();
            int r2 = 0, r1 = rows(), c2 = 0, c1 = cols();
            for (Node node : nodeList) {
                Position p = fromMap(pos2node).toStream().find(e -> e._2().equals(node)).some()._1();
                pos2node.remove(p);
                if (r2 < p.row) r2 = p.row;
                if (r1 > p.row) r1 = p.row;
                if (c2 < p.col) c2 = p.col;
                if (c1 > p.col) c1 = p.col;
            }
            int rs = r2 - r1 + 1, cs = c2 - c1 + 1;
            if (!(rs >= 2 && cs >= 2 && rs * cs == nodeList.size())) {
                isSolved = false;
                return;
            }
        }
    }
}
