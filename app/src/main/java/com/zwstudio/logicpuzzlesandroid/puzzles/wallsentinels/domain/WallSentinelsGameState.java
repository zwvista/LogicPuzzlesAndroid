package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.function.Effect2;

import static fj.data.Array.array;
import static fj.data.List.iterableList;

public class WallSentinelsGameState extends CellsGameState<WallSentinelsGame, WallSentinelsGameMove, WallSentinelsGameState> {
    public WallSentinelsObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public WallSentinelsGameState(WallSentinelsGame game) {
        super(game);
        objArray = new WallSentinelsObject[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
        updateIsSolved();
    }

    public WallSentinelsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public WallSentinelsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, WallSentinelsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, WallSentinelsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(WallSentinelsGameMove move) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(WallSentinelsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<WallSentinelsObject, WallSentinelsObject> f = obj -> {
            if (obj instanceof WallSentinelsEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new WallSentinelsMarkerObject() : new WallSentinelsWallObject();
            if (obj instanceof WallSentinelsWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new WallSentinelsMarkerObject() : new WallSentinelsEmptyObject();
            if (obj instanceof WallSentinelsMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new WallSentinelsWallObject() : new WallSentinelsEmptyObject();
            return obj;
        };
        WallSentinelsObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 11/Wall Sentinels

        Summary
        It's midnight and all is well!

        Description
        1. On the board there is a single continuous castle wall, which you
           must discover.
        2. The numbers on the board represent Sentinels (in a similar way to
           'Sentinels'). The Sentinels can be placed on the Wall or Land.
        3. The number tells you how many tiles that Sentinel can control (see)
           from there vertically and horizontally - of his type of tile.
        4. That means the number of a Land Sentinel indicates how many Land tiles
           are visible from there, up to Wall tiles or the grid border.
        5. That works the opposite way for Wall Sentinels: they control all the
           Wall tiles up to Land tiles or the grid border.
        6. The number includes the tile itself, where the Sentinel is located
           (again, like 'Sentinels').
        7. Lastly there is a single, orthogonally contiguous, Wall and it cannot
           contain 2*2 Wall tiles - just like Nurikabe.
    */
    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                WallSentinelsObject o = get(p);
                Effect2<Boolean, Integer> f = (isWall, n2) -> {
                    int n1 = 1;
                    for (Position os : WallSentinelsGame.offset)
                        for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                            WallSentinelsObject o2 = get(p2);
                            boolean isWall2 = o2 instanceof WallSentinelsWallObject || o2 instanceof WallSentinelsHintWallObject;
                            if (isWall2 != isWall) break;
                            n1++;
                        }
                    // 3. The number tells you how many tiles that Sentinel can control (see)
                    // from there vertically and horizontally - of his type of tile.
                    HintState s = (isWall ? n1 < n2 : n1 > n2) ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
                    if (isWall)
                        ((WallSentinelsHintWallObject)o).state = s;
                    else
                        ((WallSentinelsHintLandObject)o).state = s;
                    if (s != HintState.Complete) isSolved = false;
                };
                if (o instanceof WallSentinelsHintLandObject)
                    f.f(false, ((WallSentinelsHintLandObject)o).tiles);
                else if (o instanceof WallSentinelsHintWallObject)
                    f.f(true, ((WallSentinelsHintWallObject)o).tiles);
            }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                WallSentinelsObject o = get(p);
                if (o instanceof WallSentinelsWallObject || o instanceof WallSentinelsHintWallObject) {
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                    // 7. The Wall cannot contain 2*2 Wall tiles.
                    if (array(WallSentinelsGame.offset2).forall(os -> {
                        Position p2 = p.add(os);
                        if (!isValid(p2)) return false;
                        WallSentinelsObject o2 = get(p2);
                        return o2 instanceof WallSentinelsWallObject || o2 instanceof WallSentinelsHintWallObject;
                    })) {
                       isSolved = false; return;
                    }
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (Position os : WallSentinelsGame.offset) {
                Position p2 = p.add(os);
                Node node2 = pos2node.get(p2);
                if (node2 != null) g.connectNode(node, node2);
            }
        }
        // 7. Lastly there is a single, orthogonally contiguous, Wall - just like Nurikabe.
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;
    }
}
