package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.HashMap.fromMap;

/**
 * Created by zwvista on 2016/09/29.
 */

public class OrchardsGameState extends CellsGameState<OrchardsGame, OrchardsGameMove, OrchardsGameState> {
    public OrchardsObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public OrchardsGameState(OrchardsGame game) {
        super(game);
        objArray = new OrchardsObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new OrchardsEmptyObject();
    }

    public OrchardsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public OrchardsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, OrchardsObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, OrchardsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(OrchardsGameMove move) {
        if (!isValid(move.p) || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(OrchardsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<OrchardsObject, OrchardsObject> f = obj -> {
            if (obj instanceof OrchardsEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new OrchardsMarkerObject() : new OrchardsTreeObject();
            if (obj instanceof OrchardsTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new OrchardsMarkerObject() : new OrchardsEmptyObject();
            if (obj instanceof OrchardsMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new OrchardsTreeObject() : new OrchardsEmptyObject();
            return obj;
        };
        OrchardsObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 11/Orchards

        Summary
        Plant the trees. Very close, this time!

        Description
        1. In a reverse of 'Parks', you're now planting Trees close together in
           neighboring country areas.
        2. These are Apple Trees, which must cross-pollinate, thus must be planted
           in pairs - horizontally or vertically touching.
        3. A Tree must be touching just one other Tree: you can't put three or
           more contiguous Trees.
        4. At the same time, like in Parks, every country area must have exactly
           two Trees in it.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                OrchardsObject o = get(p);
                if (o instanceof OrchardsForbiddenObject)
                    set(r, c, new OrchardsEmptyObject());
                else if (o instanceof OrchardsTreeObject) {
                    OrchardsTreeObject o2 = (OrchardsTreeObject) o;
                    o2.state = AllowedObjectState.Normal;
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Position p : pos2node.keySet())
            for (Position os : OrchardsGame.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        while (!pos2node.isEmpty()) {
            g.setRootNode(fromMap(pos2node).values().head());
            List<Node> nodeList = g.bfs();
            List<Position> trees = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            if (trees.size() != 2) isSolved = false;
            if (trees.size() > 2)
                for (Position p : trees) {
                    OrchardsTreeObject o = (OrchardsTreeObject) get(p);
                    o.state = AllowedObjectState.Error;
                }
            for (Position p : trees)
                pos2node.remove(p);
        }
        for (List<Position> a : game.areas) {
            List<Position> trees = new ArrayList<>();
            int n2 = 2;
            for (Position p : a)
                if (get(p) instanceof OrchardsTreeObject)
                    trees.add(p);
            int n1 = trees.size();
            if (n1 != n2) isSolved = false;
            for (Position p : a) {
                OrchardsObject o = get(p);
                if (o instanceof OrchardsTreeObject) {
                    OrchardsTreeObject o2 = (OrchardsTreeObject) o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ? AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if (o instanceof OrchardsEmptyObject || o instanceof OrchardsMarkerObject) {
                    if (n1 == n2 && allowedObjectsOnly)
                        set(p, new OrchardsForbiddenObject());
                }
            }
        }
    }
}
