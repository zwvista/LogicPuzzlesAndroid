package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fj.data.Array.array;
import static fj.data.List.iterableList;
import static fj.function.Integers.add;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BridgesGameState extends CellsGameState<BridgesGame, BridgesGameMove, BridgesGameState> {
    public BridgesObject[] objArray;

    public BridgesGameState(BridgesGame game) {
        super(game);
        objArray = new BridgesObject[rows() * cols()];
        Arrays.fill(objArray, new BridgesEmptyObject());
        for (Position p: game.islandsInfo.keySet())
            set(p, new BridgesIslandObject());
    }

    public BridgesObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public BridgesObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, BridgesObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, BridgesObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean switchBridges(BridgesGameMove move) {
        Position pFrom = move.pFrom, pTo = move.pTo;
        if (!(pFrom.compareTo(pTo) < 0 && (pFrom.row == pTo.row || pFrom.col == pTo.col))) return false;
        BridgesObject o11 = get(pFrom), o22 = get(pTo);
        if (!(o11 instanceof BridgesIslandObject && o22 instanceof BridgesIslandObject)) return false;
        BridgesIslandObject o1 = (BridgesIslandObject) o11, o2 = (BridgesIslandObject) o22;
        int n1 = pFrom.row == pTo.row ? 1 : 2;
        int n2 = (n1 + 2) % 4;
        Position os = BridgesGame.offset[n1];
        for (Position p = pFrom.add(os); !p.equals(pTo); p.addBy(os))
            switch (o1.bridges[n1]) {
            case 0:
                BridgesObject o = get(p);
                if (!(o instanceof BridgesEmptyObject)) return false;
                set(p, new BridgesBridgeObject());
                break;
            case 1:
                set(p, new BridgesEmptyObject());
                break;
            }
        int n = (o1.bridges[n1] + 1) % 3;
        o1.bridges[n1] = o2.bridges[n2] = n;
        updateIsSolved();
        return true;
    }

    /*
        iOS Game: Logic Games/Puzzle Set 7/Bridges

        Summary
        Enough Sudoku, let's build!

        Description
        1. The board represents a Sea with some islands on it.
        2. You must connect all the islands with Bridges, making sure every
           island is connected to each other with a Bridges path.
        3. The number on each island tells you how many Bridges are touching
           that island.
        4. Bridges can only run horizontally or vertically and can't cross
           each other.
        5. Lastly, you can connect two islands with either one or two Bridges
           (or none, of course)
    */
    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (Map.Entry<Position, BridgesIslandInfo> entry : game.islandsInfo.entrySet()) {
            Position p = entry.getKey();
            BridgesIslandInfo info = entry.getValue();
            BridgesIslandObject o = (BridgesIslandObject) get(p);
            int n1 = array(o.bridges).foldLeft(add, 0);
            int n2 = info.bridges;
            o.state = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
            if (!isSolved) continue;
            Node node = new Node(p.toString());
            pos2node.put(p, node);
            g.addNode(node);
        }
        if (!isSolved) return;
        for (Map.Entry<Position, BridgesIslandInfo> entry : game.islandsInfo.entrySet()) {
            Position p = entry.getKey();
            BridgesIslandInfo info = entry.getValue();
            for (Position p2 : info.neighbors) {
                if (p2 == null) continue;
                g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2node.values().size();
        if (n1 != n2) isSolved = false;
    }
}
