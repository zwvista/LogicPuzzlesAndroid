package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F0;
import fj.Ord;
import fj.function.Integers;

import static fj.data.List.iterableList;
import static fj.data.Set.iterableSet;
import static fj.data.Stream.range;

/**
 * Created by zwvista on 2016/09/29.
 */

public class CalcudokuGameState extends CellsGameState<CalcudokuGame, CalcudokuGameMove, CalcudokuGameState> {
    public int[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public CalcudokuGameState(CalcudokuGame game) {
        super(game);
        objArray = new int[rows() * cols()];
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved();
    }

    public int get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public int get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, int dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, int obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(CalcudokuGameMove move) {
        if (!isValid(move.p) || get(move.p) == move.obj) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(CalcudokuGameMove move) {
        Position p = move.p;
        if (!isValid(p)) return false;
        int o = get(p);
        move.obj = (o + 1) % (cols() + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 4/Calcudoku

        Summary
        Mathematical Sudoku

        Description
        1. Write numbers ranging from 1 to board size respecting the calculation
           hint.
        2. The tiny numbers and math signs in the corner of an area give you the
           hint about what's happening inside that area.
        3. For example a '3+' means that the sum of the numbers inside that area
           equals 3. In that case you would have to write the numbers 1 and 2
           there.
        4. Another example: '12*' means that the multiplication of the numbers
           in that area gives 12, so it could be 3 and 4 or even 3, 4 and 1,
           depending on the area size.
        5. Even where the order of the operands matter (in subtraction and division)
           they can appear in any order inside the area (ie.e. 2/ could be done
           with 4 and 2 or 2 and 4.
        6. All the numbers appear just one time in each row and column, but they
           could be repeated in non-straight areas, like the L-shaped one.
    */
    private void updateIsSolved() {
        isSolved = true;
        F<List<Integer>, HintState> f = nums -> {
            List<Integer> nums2 = iterableSet(Ord.intOrd, nums).toJavaList();
            // 1. Write numbers ranging from 1 to board size.
            HintState s = nums2.get(0) == 0 ? HintState.Normal :
                    nums2.size() == nums.size() ? HintState.Complete : HintState.Error;
            if (s != HintState.Complete) isSolved = false;
            return s;
        };
        // 6. All the numbers appear just one time in each row.
        range(0, rows()).foreachDoEffect(r -> row2state[r] = f.f(range(0, cols()).map(c -> get(r, c)).toJavaList()));
        // 6. All the numbers appear just one time in each column.
        range(0, cols()).foreachDoEffect(c -> col2state[c] = f.f(range(0, rows()).map(r -> get(r, c)).toJavaList()));
        for (Map.Entry<Position, CalcudokuHint> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            CalcudokuHint h = entry.getValue();
            List<Integer> nums = iterableList(game.areas.get(game.pos2area.get(p))).map(p2 -> get(p2)).toJavaList();
            F0<HintState> g = () -> {
                if (nums.contains(0)) return HintState.Normal;
                int n = h.result;
                switch (h.op) {
                // 2. The tiny numbers and math signs in the corner of an area give you the
                // hint about what's happening inside that area.
                case '+':
                    return iterableList(nums).foldLeft(Integers.add, 0) == n ? HintState.Complete : HintState.Error;
                case '-':
                {
                    int n1 = nums.get(0), n2 = nums.get(1);
                    return n1 - n2 == n || n2 - n1 == n ? HintState.Complete : HintState.Error;
                }
                case '*':
                    return iterableList(nums).foldLeft(Integers.multiply, 1) == n ? HintState.Complete : HintState.Error;
                case '/':
                {
                    int n1 = nums.get(0), n2 = nums.get(1);
                    return n1 / n2 * n2 == n * n2 || n2 / n1 * n1 == n * n1 ? HintState.Complete : HintState.Error;
                }
                }
                return HintState.Normal;
            };
            HintState s = g.f();
            pos2state.put(p, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
