package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fj.F;

import static fj.data.HashMap.fromMap;

public class HolidayIslandGameState extends CellsGameState<HolidayIslandGame, HolidayIslandGameMove, HolidayIslandGameState> {
    public HolidayIslandObject[] objArray;

    public HolidayIslandGameState(HolidayIslandGame game) {
        super(game);
        objArray = new HolidayIslandObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new HolidayIslandEmptyObject();
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            set(p, new HolidayIslandHintObject() {{tiles = n;}});
        }
    }

    public HolidayIslandObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public HolidayIslandObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, HolidayIslandObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, HolidayIslandObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(HolidayIslandGameMove move) {
        if (!isValid(move.p) || game.pos2hint.get(move.p) != null || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(HolidayIslandGameMove move) {
        if (!isValid(move.p) || game.pos2hint.get(move.p) != null) return false;
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<HolidayIslandObject, HolidayIslandObject> f = obj -> {
            if (obj instanceof HolidayIslandEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new HolidayIslandMarkerObject() : new HolidayIslandTreeObject();
            if (obj instanceof HolidayIslandTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new HolidayIslandMarkerObject() : new HolidayIslandEmptyObject();
            if (obj instanceof HolidayIslandMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new HolidayIslandTreeObject() : new HolidayIslandEmptyObject();
            return obj;
        };
        HolidayIslandObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 11/Holiday Island

        Summary
        This time the campers won't have their way!

        Description
        1. This time the resort is an island, the place is packed and the campers
           (Tents) must compromise!
        2. The board represents an Island, where there are a few Tents, identified
           by the numbers.
        3. Your job is to find the water surrounding the island, with these rules:
        4. There is only one, continuous island.
        5. The numbers tell you how many tiles that camper can walk from his Tent,
           by moving horizontally or vertically. A camper can't cross water or
           other Tents.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        List<Position> rngHints = new ArrayList<>();
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                HolidayIslandObject o = get(p);
                if (o instanceof HolidayIslandForbiddenObject)
                    set(p, new HolidayIslandEmptyObject());
                else if (o instanceof HolidayIslandTreeObject)
                    ((HolidayIslandTreeObject)o).state = AllowedObjectState.Normal;
                else if (o instanceof HolidayIslandHintObject) {
                    ((HolidayIslandHintObject)o).state = HintState.Normal;
                    rngHints.add(p);
                }
                if (!(o instanceof HolidayIslandTreeObject)) {
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
                if (node2 != null) g.connectNode(node, node2);
            }
        }
        {
            // 4. There is only one, continuous island.
            g.setRootNode(fromMap(pos2node).values().head());
            List<Node> nodeList = g.bfs();
            if (nodeList.size() != pos2node.size()) isSolved = false;
        }
        g = new Graph();
        pos2node.clear();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                HolidayIslandObject o = get(p);
                if (!(o instanceof HolidayIslandTreeObject || o instanceof HolidayIslandHintObject)) {
                    // 5. A camper can't cross water or other Tents.
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
        List<List<Position>> areas = new ArrayList<>();
        Map<Position, Integer> pos2area = new HashMap<>();
        while (!pos2node.isEmpty()) {
            g.setRootNode(fromMap(pos2node).values().head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            int n = areas.size();
            for (Node node : nodeList) {
                Position p = fromMap(pos2node).toStream().find(e -> e._2().equals(node)).some()._1();
                pos2node.remove(p);
                pos2area.put(p, n);
            }
            areas.add(area);
        }
        for (Position p : rngHints) {
            int n2 = game.pos2hint.get(p);
            Set<Position> rng = new HashSet<>();
            for (Position os : HolidayIslandGame.offset) {
                Position p2 = p.add(os);
                Integer i = pos2area.get(p2);
                if (i == null) continue;
                rng.addAll(areas.get(i));
            }
            int n1 = rng.size();
            // 5. The numbers tell you how many tiles that camper can walk from his Tent,
            // by moving horizontally or vertically.
            HintState s = n1 > n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            ((HolidayIslandHintObject)get(p)).state = s;
            if (s != HintState.Complete) isSolved = false;
            if (allowedObjectsOnly && n1 <= n2)
                for(Position p2 : rng)
                    if (!p2.equals(p))
                        set(p2, new HolidayIslandForbiddenObject());
        }
    }
}
