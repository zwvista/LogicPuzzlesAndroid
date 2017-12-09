package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.HashMap.fromMap;

/**
 * Created by zwvista on 2016/09/29.
 */

public class ParkLakesGameState extends CellsGameState<ParkLakesGame, ParkLakesGameMove, ParkLakesGameState> {
    public ParkLakesObject[] objArray;

    public ParkLakesGameState(ParkLakesGame game) {
        super(game);
        objArray = new ParkLakesObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new ParkLakesEmptyObject();
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            set(p, new ParkLakesHintObject() {{tiles = n;}});
        }
    }

    public ParkLakesObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public ParkLakesObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, ParkLakesObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, ParkLakesObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(ParkLakesGameMove move) {
        if (!isValid(move.p) || game.pos2hint.get(move.p) != null || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(ParkLakesGameMove move) {
        if (!isValid(move.p) || game.pos2hint.get(move.p) != null) return false;
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<ParkLakesObject, ParkLakesObject> f = obj -> {
            if (obj instanceof ParkLakesEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new ParkLakesMarkerObject() : new ParkLakesTreeObject();
            if (obj instanceof ParkLakesTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new ParkLakesMarkerObject() : new ParkLakesEmptyObject();
            if (obj instanceof ParkLakesMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new ParkLakesTreeObject() : new ParkLakesEmptyObject();
            return obj;
        };
        ParkLakesObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 15/Park Lakes

        Summary
        Find the Lakes

        Description
        1. The board represents a park, where there are some hidden lakes, all square
           in shape.
        2. You have to find the lakes with the aid of hints, knowing that:
        3. A number tells you the total size of the any lakes orthogonally touching it,
           while a question mark tells you that there is at least one lake orthogonally
           touching it.
        4. Lakes aren't on tiles with numbers or question marks.
        5. All the land tiles are connected horizontally or vertically.
    */
    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                ParkLakesObject o = get(p);
                if (o instanceof ParkLakesTreeObject) {
                    ((ParkLakesTreeObject)o).state = AllowedObjectState.Normal;
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                } else if (o instanceof ParkLakesHintObject)
                    ((ParkLakesHintObject)o).state = HintState.Normal;
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (Position os : TierraDelFuegoGame.offset) {
                Position p2 = p.add(os);
                Node node2 = pos2node.get(p2);
                if (node2 != null) g.connectNode(node, node2);
            }
        }
        List<List<Position>> areas = new ArrayList<>();
        Map<Position, Integer> pos2area = new HashMap<>();
        while (!pos2node.isEmpty()) {
            g.setRootNode(fromMap(pos2node).values().head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            int r2 = 0, r1 = rows(), c2 = 0, c1 = cols(), n = areas.size();
            for (Node node : nodeList) {
                Position p = fromMap(pos2node).toStream().find(e -> e._2().equals(node)).some()._1();
                pos2node.remove(p);
                if (r2 < p.row) r2 = p.row;
                if (r1 > p.row) r1 = p.row;
                if (c2 < p.col) c2 = p.col;
                if (c1 > p.col) c1 = p.col;
                pos2area.put(p, n);
            }
            areas.add(area);
            int rs = r2 - r1 + 1, cs = c2 - c1 + 1;
            if (!(rs == cs && rs * cs == nodeList.size())) {
                isSolved = false;
                for (Position p : area)
                    ((ParkLakesTreeObject)get(p)).state = AllowedObjectState.Error;
            }
        }
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue(), n1 = 0;
            for (Position os : ParkLakesGame.offset) {
                Integer i = pos2area.get(p.add(os));
                if (i == null) continue;
                n1 += areas.get(i).size();
            }
            // 3. A number tells you the total size of any lakes orthogonally touching it,
            // while a question mark tells you that there is at least one lake orthogonally
            // touching it.
            HintState s = n1 == 0 ? HintState.Normal : n1 == n2 || n2 == -1 ?
                    HintState.Complete : HintState.Error;
            ((ParkLakesHintObject)get(p)).state = s;
            if (s != HintState.Complete) isSolved = false;
        }
        g = new Graph();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (!(get(p) instanceof ParkLakesTreeObject)) {
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (Position os : TierraDelFuegoGame.offset) {
                Position p2 = p.add(os);
                Node node2 = pos2node.get(p2);
                if (node2 == null) continue;
                g.connectNode(node, node2);
            }
        }
        // 5. All the land tiles are connected horizontally or vertically.
        g.setRootNode(fromMap(pos2node).values().head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;
    }
}
