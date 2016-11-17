package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.Arrays;

/**
 * Created by zwvista on 2016/09/29.
 */

public class MasyuGameState extends CellsGameState<MasyuGame, MasyuGameMove, MasyuGameState> {
    public Boolean[][] objArray;

    public MasyuGameState(MasyuGame game) {
        super(game);
        objArray = new Boolean[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new Boolean[4];
            Arrays.fill(objArray[i], false);
        }
    }

    public Boolean[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public Boolean[] get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, Boolean[] dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, Boolean[] obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        isSolved = false;
    }

    public boolean setObject(MasyuGameMove move) {
        Position p = move.p;
        int dir = move.dir;
        Position p2 = p.add(MasyuGame.offset[dir]);
        int dir2 = (dir + 2) % 4;
        Boolean[] o = get(p);
        o[dir] = !o[dir];
        o[dir2] = !o[dir2];
        updateIsSolved();
        return true;
    }
}
