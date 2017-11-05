package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TennerGridGameState extends CellsGameState<TennerGridGame, TennerGridGameMove, TennerGridGameState> {
    public int[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public TennerGridGameState(TennerGridGame game) {
        super(game);
        objArray = new int[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
        updateIsSolved();
    }

    public int get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public int get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, int obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, int obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(TennerGridGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) >= 0 || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TennerGridGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) >= 0) return false;
        int o = get(p);
        move.obj = o == 9 ? -1 : o + 1;
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 7/TennerGrid

        Summary
        Counting up to 10

        Description
        1. You goal is to enter every digit, from 0 to 9, in each row of the Grid.
        2. The number on the bottom row gives you the sum for that column.
        3. Digit can repeat on the same column, however digits in contiguous tiles
           must be different, even diagonally. Obviously digits can't repeat on
           the same row.
    */
    private void updateIsSolved() {
        isSolved = true;
        for (int c = 0; c < cols(); c++) {
            int h = get(rows() - 1, c), n = 0;
            boolean isDirty = false, allFixed = true;
            for (int r = 0; r < rows() - 1; r++) {
                int o1 = game.get(r, c), o2 = get(r, c);
                if (o1 == -1) {
                    allFixed = false;
                    if (o2 == -1)
                        isSolved = false;
                    else
                        isDirty = true;
                }
                n += o2 == -1 ? 0 : o2;
            }
            HintState s = !isDirty && !allFixed ? HintState.Normal : n == h ? HintState.Complete : HintState.Error;
            pos2state.put(new Position(rows() - 1, c), s);
            if (s != HintState.Complete) isSolved = false;
        }
        for (int r = 0; r < rows() - 1; r++) {
            Set<Integer> nums = new HashSet<>();
            HintState rowState = HintState.Complete;
            for (int c = 0; c < cols(); c++) {
                int o1 = game.get(r, c), o2 = get(r, c);
                if (o1 == -1 && o2 == -1) rowState = HintState.Normal;
                if (o2 != -1) nums.add(o2);
            }
            if (nums.size() != cols()) {
                isSolved = false;
                if (rowState == HintState.Complete) rowState = HintState.Error;
            }
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int o1 = game.get(r, c), o2 = get(r, c);
                if (o1 == -1 && o2 != -1)
                    pos2state.put(p, rowState);
            }
        }
    }
}
