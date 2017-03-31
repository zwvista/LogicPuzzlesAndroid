package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain;

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

public class PowerGridGameState extends CellsGameState<PowerGridGame, PowerGridGameMove, PowerGridGameState> {
    public PowerGridObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public PowerGridGameState(PowerGridGame game, boolean allowedObjectsOnly) {
        super(game);
        objArray = new PowerGridObject[rows() * cols()];
        Arrays.fill(objArray, new PowerGridEmptyObject());
        for (Position p : game.pos2hint.keySet())
            set(p, new PowerGridHintObject());
        updateIsSolved(allowedObjectsOnly);
    }

    public PowerGridObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public PowerGridObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, PowerGridObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, PowerGridObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved(boolean allowedObjectsOnly) {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0 ; c < cols(); c++) {
                PowerGridObject o = get(r, c);
                if (o instanceof PowerGridTowerObject)
                    ((PowerGridTowerObject) o).state = AllowedObjectState.Normal;
                else {
                    if (o instanceof PowerGridForbiddenObject)
                        set(r, c, new PowerGridEmptyObject());
                    Position p = new Position(r, c);
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Position p : pos2node.keySet())
            for (Position os : PowerGridGame.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0 ; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasTowerNeighbor = () -> {
                    for (Position os : PowerGridGame.offset) {
                        Position p2 = p.add(os);
                        if (isValid(p2) && get(p2) instanceof PowerGridTowerObject)
                            return true;
                    }
                    return false;
                };
                PowerGridObject o = get(r, c);
                if (o instanceof PowerGridTowerObject) {
                    PowerGridTowerObject o2 = (PowerGridTowerObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && !hasTowerNeighbor.f() ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof PowerGridEmptyObject || o instanceof PowerGridMarkerObject) &&
                        allowedObjectsOnly && hasTowerNeighbor.f())
                    set(r, c, new PowerGridForbiddenObject());
            }
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int[] nums = {0, 0, 0, 0};
            List<Position> rng = new ArrayList<>();
            next: for (int i = 0; i < 4; i++) {
                Position os = PowerGridGame.offset[i];
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    PowerGridObject o2 = get(p2);
                    if (o2 instanceof PowerGridTowerObject) continue next;
                    if (o2 instanceof PowerGridEmptyObject)
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
                    set(p2, new PowerGridForbiddenObject());
        }
        if (!isSolved) return;
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(PowerGridGameMove move, boolean allowedObjectsOnly) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved(allowedObjectsOnly);
        return true;
    }

    public boolean switchObject(PowerGridGameMove move, MarkerOptions markerOption, boolean allowedObjectsOnly) {
        F<PowerGridObject, PowerGridObject> f = obj -> {
            if (obj instanceof PowerGridEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new PowerGridMarkerObject() : new PowerGridTowerObject();
            if (obj instanceof PowerGridTowerObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new PowerGridMarkerObject() : new PowerGridEmptyObject();
            if (obj instanceof PowerGridMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new PowerGridTowerObject() : new PowerGridEmptyObject();
            return obj;
        };
        PowerGridObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move, allowedObjectsOnly);
    }
}
