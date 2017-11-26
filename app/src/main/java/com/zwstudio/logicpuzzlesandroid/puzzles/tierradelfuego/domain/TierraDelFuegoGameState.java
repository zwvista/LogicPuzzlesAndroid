package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fj.F;

import static fj.data.HashMap.fromMap;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TierraDelFuegoGameState extends CellsGameState<TierraDelFuegoGame, TierraDelFuegoGameMove, TierraDelFuegoGameState> {
    public TierraDelFuegoObject[] objArray;

    public TierraDelFuegoGameState(TierraDelFuegoGame game) {
        super(game);
        objArray = new TierraDelFuegoObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new TierraDelFuegoEmptyObject();
        for (Map.Entry<Position, Character> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            char ch = entry.getValue();
            set(p, new TierraDelFuegoHintObject() {{id = ch;}});
        }
        updateIsSolved();
    }

    public TierraDelFuegoObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public TierraDelFuegoObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, TierraDelFuegoObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, TierraDelFuegoObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(TierraDelFuegoGameMove move) {
        if (!isValid(move.p) || game.pos2hint.get(move.p) != null || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TierraDelFuegoGameMove move) {
        if (!isValid(move.p) || game.pos2hint.get(move.p) != null) return false;
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<TierraDelFuegoObject, TierraDelFuegoObject> f = obj -> {
            if (obj instanceof TierraDelFuegoEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TierraDelFuegoMarkerObject() : new TierraDelFuegoTreeObject();
            if (obj instanceof TierraDelFuegoTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new TierraDelFuegoMarkerObject() : new TierraDelFuegoEmptyObject();
            if (obj instanceof TierraDelFuegoMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TierraDelFuegoTreeObject() : new TierraDelFuegoEmptyObject();
            return obj;
        };
        TierraDelFuegoObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 11/Tierra Del Fuego

        Summary
        Fuegians!

        Description
        1. The board represents the 'Tierra del Fuego' archipelago, where native
           tribes, the Fuegians, live.
        2. Being organized in tribes, each tribe, marked with a different letter,
           has occupied an island in the archipelago.
        3. The archipelago is peculiar because all bodies of water separating the
           islands are identical in shape and occupied a 2*1 or 1*2 space.
        4. These bodies of water can only touch diagonally.
        5. Your task is to find these bodies of water.
        6. Please note there are no hidden tribes or islands without a tribe on it.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                TierraDelFuegoObject o = get(p);
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
                if (o instanceof TierraDelFuegoForbiddenObject)
                    set(p, new TierraDelFuegoEmptyObject());
                else if (o instanceof TierraDelFuegoTreeObject)
                    ((TierraDelFuegoTreeObject)o).state = AllowedObjectState.Normal;
                else if (o instanceof TierraDelFuegoHintObject)
                    ((TierraDelFuegoHintObject)o).state = HintState.Normal;
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            boolean b1 = get(p) instanceof TierraDelFuegoTreeObject;
            for (Position os : TierraDelFuegoGame.offset) {
                Position p2 = p.add(os);
                Node node2 = pos2node.get(p2);
                if (node2 == null) continue;
                boolean b2 = get(p2) instanceof TierraDelFuegoTreeObject;
                if (b1 == b2)
                    g.connectNode(node, node2);
            }
        }
        while (!pos2node.isEmpty()) {
            g.setRootNode(fromMap(pos2node).values().head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            if (get(fromMap(pos2node).keys().head()) instanceof TierraDelFuegoTreeObject) {
                // 3. The archipelago is peculiar because all bodies of water separating the
                // islands are identical in shape and occupied a 2*1 or 1*2 space.
                // 4. These bodies of water can only touch diagonally.
                if (area.size() != 2)
                    isSolved = false;
                else if (allowedObjectsOnly)
                    for (Position p : area)
                        for (Position os : TierraDelFuegoGame.offset) {
                            Position p2 = p.add(os);
                            if (!isValid(p2)) continue;
                            TierraDelFuegoObject o = get(p2);
                            if (o instanceof TierraDelFuegoEmptyObject || o instanceof TierraDelFuegoMarkerObject)
                                set(p, new TierraDelFuegoForbiddenObject());
                        }
                if (area.size() > 2)
                    for (Position p : area)
                        ((TierraDelFuegoTreeObject)get(p)).state = AllowedObjectState.Error;
            } else {
                // 2. Being organized in tribes, each tribe, marked with a different letter,
                // has occupied an island in the archipelago.
                Set<Character> ids = new HashSet<>();
                for (Position p : area) {
                    TierraDelFuegoObject o = get(p);
                    if (o instanceof TierraDelFuegoHintObject)
                        ids.add(((TierraDelFuegoHintObject)o).id);
                }
                if (ids.size() == 1)
                    for (Position p : area) {
                        TierraDelFuegoObject o = get(p);
                        if (o instanceof TierraDelFuegoHintObject)
                            ((TierraDelFuegoHintObject)o).state = HintState.Complete;
                    }
                else
                    isSolved = false;
            }
            for (Position p : area)
                pos2node.remove(p);
        }
    }
}
