package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain;

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

public class ProductSentinelsGameState extends CellsGameState<ProductSentinelsGame, ProductSentinelsGameMove, ProductSentinelsGameState> {
    public ProductSentinelsObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public ProductSentinelsGameState(ProductSentinelsGame game, boolean allowedObjectsOnly) {
        super(game);
        objArray = new ProductSentinelsObject[rows() * cols()];
        Arrays.fill(objArray, new ProductSentinelsEmptyObject());
        for (Position p : game.pos2hint.keySet())
            set(p, new ProductSentinelsHintObject());
        updateIsSolved(allowedObjectsOnly);
    }

    public ProductSentinelsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public ProductSentinelsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, ProductSentinelsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, ProductSentinelsObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved(boolean allowedObjectsOnly) {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0 ; c < cols(); c++) {
                ProductSentinelsObject o = get(r, c);
                if (o instanceof ProductSentinelsTowerObject)
                    ((ProductSentinelsTowerObject) o).state = AllowedObjectState.Normal;
                else {
                    if (o instanceof ProductSentinelsForbiddenObject)
                        set(r, c, new ProductSentinelsEmptyObject());
                    Position p = new Position(r, c);
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Position p : pos2node.keySet())
            for (Position os : ProductSentinelsGame.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0 ; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasTowerNeighbor = () -> {
                    for (Position os : ProductSentinelsGame.offset) {
                        Position p2 = p.add(os);
                        if (isValid(p2) && get(p2) instanceof ProductSentinelsTowerObject)
                            return true;
                    }
                    return false;
                };
                ProductSentinelsObject o = get(r, c);
                if (o instanceof ProductSentinelsTowerObject) {
                    ProductSentinelsTowerObject o2 = (ProductSentinelsTowerObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && !hasTowerNeighbor.f() ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof ProductSentinelsEmptyObject || o instanceof ProductSentinelsMarkerObject) &&
                        allowedObjectsOnly && hasTowerNeighbor.f())
                    set(r, c, new ProductSentinelsForbiddenObject());
            }
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int[] nums = {0, 0, 0, 0};
            List<Position> rng = new ArrayList<>();
            next: for (int i = 0; i < 4; i++) {
                Position os = ProductSentinelsGame.offset[i];
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    ProductSentinelsObject o2 = get(p2);
                    if (o2 instanceof ProductSentinelsTowerObject) continue next;
                    if (o2 instanceof ProductSentinelsEmptyObject)
                        rng.add(p2.plus());
                    nums[i]++;
                }
            }
            int n1 = (nums[0] + nums[2] + 1) * (nums[1] + nums[3] + 1);
            pos2state.put(p, n1 > n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2)
                isSolved = false;
            else
                for (Position p2 : rng)
                    set(p2, new ProductSentinelsForbiddenObject());
        }
        if (!isSolved) return;
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(ProductSentinelsGameMove move, boolean allowedObjectsOnly) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved(allowedObjectsOnly);
        return true;
    }

    public boolean switchObject(ProductSentinelsGameMove move, MarkerOptions markerOption, boolean allowedObjectsOnly) {
        F<ProductSentinelsObject, ProductSentinelsObject> f = obj -> {
            if (obj instanceof ProductSentinelsEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new ProductSentinelsMarkerObject() : new ProductSentinelsTowerObject();
            if (obj instanceof ProductSentinelsTowerObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new ProductSentinelsMarkerObject() : new ProductSentinelsEmptyObject();
            if (obj instanceof ProductSentinelsMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new ProductSentinelsTowerObject() : new ProductSentinelsEmptyObject();
            return obj;
        };
        ProductSentinelsObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move, allowedObjectsOnly);
    }
}
