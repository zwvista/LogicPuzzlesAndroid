package com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zwvista on 2016/09/29.
 */

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
        iOS Game: Logic Games/Puzzle Set 1/Walls

        Summary
        Put one Tree in each Park, row and column.(two in bigger levels)

        Description
        1. In Walls, you have many differently coloured areas(Walls) on the board.
        2. The goal is to plant Trees, following these rules:
        3. A Tree can't touch another Tree, not even diagonally.
        4. Each park must have exactly ONE Tree.
        5. There must be exactly ONE Tree in each row and each column.
        6. Remember a Tree CANNOT touch another Tree diagonally,
           but it CAN be on the same diagonal line.
        7. Larger puzzles have TWO Trees in each park, each row and each column.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                WallsObject o = get(p);
                if (o instanceof WallsEmptyObject)
                    isSolved = false;
                else if (o instanceof WallsHintObject) {
                    WallsHintObject o2 = (WallsHintObject) o;
                    int n2 = o2.walls;
                    int n1 = 0;
                    for (int i = 0; i < 4; i++) {
                        Position os = WallsGame.offset[i];
                        for (Position p2 = p.add(os); isValid(p2); p2.addBy(os))
                             if (i % 2 == 0)
                                 if (get(p2) instanceof WallsVertObject)
                                     n1++;
                                 else
                                     break;
                             else
                                 if (get(p2) instanceof WallsHorzObject)
                                     n1++;
                                 else
                                     break;
                    }
                    HintState s = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
                    if (s != HintState.Complete) isSolved = false;
                    ((WallsHintObject)get(p)).state = s;
                }
            }
    }
}
