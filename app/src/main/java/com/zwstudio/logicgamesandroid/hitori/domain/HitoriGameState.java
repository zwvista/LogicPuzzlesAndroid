package com.zwstudio.logicgamesandroid.hitori.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.Graph;
import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Node;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

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

public class HitoriGameState {
    public HitoriGame game;
    public HitoriObject[] objArray;
    public LogicGamesHintState[] row2state;
    public LogicGamesHintState[] col2state;
    public boolean isSolved;

    public Position size() {return game.size;}
    public int rows() {return game.rows();}
    public int cols() {return game.cols();}
    public boolean isValid(int row, int col) { return game.isValid(row, col); }
    public boolean isValid(Position p) {
        return game.isValid(p);
    }

    public HitoriGameState(HitoriGame game) {
        this.game = game;
        objArray = new HitoriObject[rows() * cols()];
        Arrays.fill(objArray, HitoriObject.Empty);
    }

    public HitoriObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public HitoriObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, HitoriObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, HitoriObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == HitoriObject.Cloud)
                    n1++;
            row2state[r] = n1 < n2 ? LogicGamesHintState.Normal : n1 == n2 ? LogicGamesHintState.Complete : LogicGamesHintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++)
                if (get(r, c) == HitoriObject.Cloud)
                    n1++;
            col2state[c] = n1 < n2 ? LogicGamesHintState.Normal : n1 == n2 ? LogicGamesHintState.Complete : LogicGamesHintState.Error;
            if (n1 != n2) isSolved = false;
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) != HitoriObject.Cloud) continue;
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
            }
        for (Position p : pos2node.keySet())
            for (Position os : HitoriGame.offset) {
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

    public boolean setObject(HitoriGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(HitoriMarkerOptions markerOption, HitoriGameMove move) {
        F<HitoriObject, HitoriObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == HitoriMarkerOptions.MarkerBeforeCloud ?
                        HitoriObject.Marker : HitoriObject.Cloud;
            case Cloud:
                return markerOption == HitoriMarkerOptions.MarkerAfterCloud ?
                        HitoriObject.Marker : HitoriObject.Empty;
            case Marker:
                return markerOption == HitoriMarkerOptions.MarkerBeforeCloud ?
                        HitoriObject.Cloud : HitoriObject.Empty;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }
}
