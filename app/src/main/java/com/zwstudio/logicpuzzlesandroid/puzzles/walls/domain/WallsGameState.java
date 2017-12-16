package com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.Map;

public class WallsGameState extends CellsGameState<WallsGame, WallsGameMove, WallsGameState> {
    public WallsObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public WallsGameState(WallsGame game) {
        super(game);
        objArray = new WallsObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new WallsEmptyObject();
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            WallsHintObject o = new WallsHintObject();
            o.walls = n; o.state = HintState.Normal;
            set(p, o);
        }
    }

    public WallsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public WallsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, WallsObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, WallsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(WallsGameMove move) {
        if (!isValid(move.p) || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(WallsGameMove move) {
        WallsObject o = get(move.p);
        move.obj = o instanceof WallsEmptyObject ? new WallsHorzObject() :
            o instanceof WallsHorzObject ? new WallsVertObject() :
            o instanceof WallsVertObject ? new WallsEmptyObject() : o;
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 5/Walls

        Summary
        Find the maze of Bricks

        Description
        1. In Walls you must fill the board with straight horizontal and
           vertical lines (walls) that stem from each number.
        2. The number itself tells you the total length of Wall segments
           connected to it.
        3. Wall pieces have two ways to be put, horizontally or vertically.
        4. Not every wall piece must be connected with a number, but the
           board must be filled with wall pieces.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                WallsObject o = get(p);
                if (o instanceof WallsEmptyObject)
                    // 1. In Walls you must fill the board with straight horizontal and
                    // vertical lines (walls) that stem from each number.
                    // 4. Not every wall piece must be connected with a number, but the
                    // board must be filled with wall pieces.
                    isSolved = false;
                else if (o instanceof WallsHintObject) {
                    WallsHintObject o2 = (WallsHintObject) o;
                    int n2 = o2.walls;
                    int n1 = 0;
                    for (int i = 0; i < 4; i++) {
                        Position os = WallsGame.offset[i];
                        for (Position p2 = p.add(os); isValid(p2); p2.addBy(os))
                             if (i % 2 == 0)
                                 // 3. Wall pieces have two ways to be put, horizontally or vertically.
                                 if (get(p2) instanceof WallsVertObject)
                                     n1++;
                                 else
                                     break;
                             else
                                 // 3. Wall pieces have two ways to be put, horizontally or vertically.
                                 if (get(p2) instanceof WallsHorzObject)
                                     n1++;
                                 else
                                     break;
                    }
                    // 2. The number itself tells you the total length of Wall segments
                    // connected to it.
                    HintState s = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
                    if (s != HintState.Complete) isSolved = false;
                    ((WallsHintObject)get(p)).state = s;
                }
            }
    }
}
