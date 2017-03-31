package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain;

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

public class BootyIslandGameState extends CellsGameState<BootyIslandGame, BootyIslandGameMove, BootyIslandGameState> {
    public BootyIslandObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public BootyIslandGameState(BootyIslandGame game, boolean allowedObjectsOnly) {
        super(game);
        objArray = new BootyIslandObject[rows() * cols()];
        Arrays.fill(objArray, new BootyIslandEmptyObject());
        for (Position p : game.pos2hint.keySet())
            set(p, new BootyIslandHintObject());
        updateIsSolved(allowedObjectsOnly);
    }

    public BootyIslandObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public BootyIslandObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, BootyIslandObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, BootyIslandObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved(boolean allowedObjectsOnly) {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0 ; c < cols(); c++) {
                BootyIslandObject o = get(r, c);
                if (o instanceof BootyIslandTowerObject)
                    ((BootyIslandTowerObject) o).state = AllowedObjectState.Normal;
                else {
                    if (o instanceof BootyIslandForbiddenObject)
                        set(r, c, new BootyIslandEmptyObject());
                    Position p = new Position(r, c);
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Position p : pos2node.keySet())
            for (Position os : BootyIslandGame.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0 ; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasTowerNeighbor = () -> {
                    for (Position os : BootyIslandGame.offset) {
                        Position p2 = p.add(os);
                        if (isValid(p2) && get(p2) instanceof BootyIslandTowerObject)
                            return true;
                    }
                    return false;
                };
                BootyIslandObject o = get(r, c);
                if (o instanceof BootyIslandTowerObject) {
                    BootyIslandTowerObject o2 = (BootyIslandTowerObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && !hasTowerNeighbor.f() ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof BootyIslandEmptyObject || o instanceof BootyIslandMarkerObject) &&
                        allowedObjectsOnly && hasTowerNeighbor.f())
                    set(r, c, new BootyIslandForbiddenObject());
            }
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int[] nums = {0, 0, 0, 0};
            List<Position> rng = new ArrayList<>();
            next: for (int i = 0; i < 4; i++) {
                Position os = BootyIslandGame.offset[i];
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    BootyIslandObject o2 = get(p2);
                    if (o2 instanceof BootyIslandTowerObject) continue next;
                    if (o2 instanceof BootyIslandEmptyObject)
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
                    set(p2, new BootyIslandForbiddenObject());
        }
        if (!isSolved) return;
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(BootyIslandGameMove move, boolean allowedObjectsOnly) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved(allowedObjectsOnly);
        return true;
    }

    public boolean switchObject(BootyIslandGameMove move, MarkerOptions markerOption, boolean allowedObjectsOnly) {
        F<BootyIslandObject, BootyIslandObject> f = obj -> {
            if (obj instanceof BootyIslandEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new BootyIslandMarkerObject() : new BootyIslandTowerObject();
            if (obj instanceof BootyIslandTowerObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new BootyIslandMarkerObject() : new BootyIslandEmptyObject();
            if (obj instanceof BootyIslandMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new BootyIslandTowerObject() : new BootyIslandEmptyObject();
            return obj;
        };
        BootyIslandObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move, allowedObjectsOnly);
    }
}
