package com.zwstudio.logicgamesandroid.bridges.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.CellsGameState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Graph;
import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Node;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fj.data.Array.arrayArray;
import static fj.data.List.iterableList;
import static fj.function.Integers.add;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BridgesGameState extends CellsGameState<BridgesGame> {
    public BridgesObject[] objArray;

    public BridgesGameState(BridgesGame game) {
        super(game);
        objArray = new BridgesObject[rows() * cols()];
        Arrays.fill(objArray, new BridgesEmptyObject());
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

    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2Node = new HashMap<>();
        for (Map.Entry<Position, BridgesIslandInfo> entry : game.islandsInfo.entrySet()) {
            Position p = entry.getKey();
            BridgesIslandInfo info = entry.getValue();
            BridgesIslandObject o = (BridgesIslandObject) get(p);
            int n1 = arrayArray(o.bridges).foldLeft(add, 0);
            int n2 = info.bridges;
            o.state = n1 < n2 ? LogicGamesHintState.Normal : n1 == n2 ? LogicGamesHintState.Complete : LogicGamesHintState.Error;
            if (n1 != n2) isSolved = false;
            if (!isSolved) continue;
            Node node = new Node(p.toString());
            pos2Node.put(p, node);
            g.addNode(node);
        }
        if (!isSolved) return;
        for (Map.Entry<Position, BridgesIslandInfo> entry : game.islandsInfo.entrySet()) {
            Position p = entry.getKey();
            BridgesIslandInfo info = entry.getValue();
            for (Position p2 : info.neighbors) {
                if (p2 == null) continue;
                g.connectNode(pos2Node.get(p), pos2Node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2Node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2Node.values().size();
        if (n1 != n2) isSolved = false;
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
                {
                    BridgesObject o = get(p);
                    if (!(o instanceof BridgesEmptyObject)) return false;
                    set(p, new BridgesBridgeObject());
                }
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
}
