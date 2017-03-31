package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F0;

import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LighthousesGameState extends CellsGameState<LighthousesGame, LighthousesGameMove, LighthousesGameState> {
    public LighthousesObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public LighthousesGameState(LighthousesGame game, boolean allowedObjectsOnly) {
        super(game);
        objArray = new LighthousesObject[rows() * cols()];
        Arrays.fill(objArray, new LighthousesEmptyObject());
        for (Position p : game.pos2hint.keySet())
            set(p, new LighthousesHintObject());
        updateIsSolved(allowedObjectsOnly);
    }

    public LighthousesObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public LighthousesObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, LighthousesObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, LighthousesObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved(boolean allowedObjectsOnly) {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                LighthousesObject o = get(r, c);
                if (o instanceof LighthousesTowerObject)
                    ((LighthousesTowerObject) o).state = AllowedObjectState.Normal;
                else {
                    if (o instanceof LighthousesForbiddenObject)
                        set(r, c, new LighthousesEmptyObject());
                    Position p = new Position(r, c);
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Position p : pos2node.keySet())
            for (Position os : LighthousesGame.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasTowerNeighbor = () -> {
                    for (Position os : LighthousesGame.offset) {
                        Position p2 = p.add(os);
                        if (isValid(p2) && get(p2) instanceof LighthousesTowerObject)
                            return true;
                    }
                    return false;
                };
                LighthousesObject o = get(r, c);
                if (o instanceof LighthousesTowerObject) {
                    LighthousesTowerObject o2 = (LighthousesTowerObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && !hasTowerNeighbor.f() ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof LighthousesEmptyObject || o instanceof LighthousesMarkerObject) &&
                        allowedObjectsOnly && hasTowerNeighbor.f())
                    set(r, c, new LighthousesForbiddenObject());
            }
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int[] nums = {0, 0, 0, 0};
            List<Position> rng = new ArrayList<>();
            next: for (int i = 0; i < 4; i++) {
                Position os = LighthousesGame.offset[i];
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    LighthousesObject o2 = get(p2);
                    if (o2 instanceof LighthousesTowerObject) continue next;
                    if (o2 instanceof LighthousesEmptyObject)
                        rng.add(p2.plus());
                    nums[i]++;
                }
            }
            int n1 = nums[0] + nums[1] + nums[2] + nums[3] + 1;
            pos2state.put(p, n1 > n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2)
                isSolved = false;
            else
                for (Position p2 : rng)
                    set(p2, new LighthousesForbiddenObject());
        }
        if (!isSolved) return;
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(LighthousesGameMove move, boolean allowedObjectsOnly) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved(allowedObjectsOnly);
        return true;
    }

    public boolean switchObject(LighthousesGameMove move, MarkerOptions markerOption, boolean allowedObjectsOnly) {
        F<LighthousesObject, LighthousesObject> f = obj -> {
            if (obj instanceof LighthousesEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new LighthousesMarkerObject() : new LighthousesTowerObject();
            if (obj instanceof LighthousesTowerObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new LighthousesMarkerObject() : new LighthousesEmptyObject();
            if (obj instanceof LighthousesMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new LighthousesTowerObject() : new LighthousesEmptyObject();
            return obj;
        };
        LighthousesObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move, allowedObjectsOnly);
    }
}
