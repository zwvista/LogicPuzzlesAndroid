package com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain;

import com.rits.cloning.Cloner;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain.ParksGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fj.F;

import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class RoomsGameState extends CellsGameState<RoomsGame, RoomsGameMove, RoomsGameState> {
    public GridLineObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public RoomsGameState(RoomsGame game) {
        super(game);
        objArray = new Cloner().deepClone(game.objArray);
        updateIsSolved();
    }

    public GridLineObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public GridLineObject[] get(Position p) {
        return get(p.row, p.col);
    }

    public boolean setObject(RoomsGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        GridLineObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(RoomsGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(RoomsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<GridLineObject, GridLineObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        GridLineObject.Marker : GridLineObject.Line;
            case Line:
                return markerOption == MarkerOptions.MarkerLast ?
                        GridLineObject.Marker : GridLineObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        GridLineObject.Line : GridLineObject.Empty;
            }
            return obj;
        };
        GridLineObject[] dotObj = get(move.p);
        move.obj = f.f(dotObj[move.dir]);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 5/Rooms

        Summary
        Close the doors between Rooms

        Description
        1. The view of the board is a castle with every tile identifying a Room.
           Between Rooms there are doors that can be open or closed. At the start
           of the game all doors are open.
        2. Each number inside a Room tells you how many other Rooms you see from
           there, in a straight line horizontally or vertically when the appropriate
           doors are closed.
        3. At the end of the solution, each Room must be reachable from the others.
           That means no single Room or group of Rooms can be divided by the others.
        4. In harder levels some tiles won't tell you how many Rooms are visible
           at all.
    */
    private void updateIsSolved() {
        isSolved = true;
        // 2. Each number inside a Room tells you how many other Rooms you see from
        // there, in a straight line horizontally or vertically when the appropriate
        // doors are closed.
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            for (int i = 0; i < 4; i++)
                for (Position p2 = p.plus(); get(p2.add(RoomsGame.offset2[i]))[RoomsGame.dirs[i]] != GridLineObject.Line; p2.addBy(RoomsGame.offset[i]))
                    n1++;
            pos2state.put(p, n1 > n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
        Set<Position> rng = new HashSet<>();
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                rng.add(p.plus());
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                for (int i = 0; i < 4; i++)
                    if (get(p.add(RoomsGame.offset2[i]))[RoomsGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node.get(p), pos2node.get(p.add(ParksGame.offset[i])));
            }
        // 3. At the end of the solution, each Room must be reachable from the others.
        // That means no single Room or group of Rooms can be divided by the others.
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;
    }
}
