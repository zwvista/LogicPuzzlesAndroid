package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F2;
import fj.Ord;

import static fj.data.Array.array;
import static fj.data.Set.iterableSet;
import static fj.data.Stream.range;

public class MathraxGameState extends CellsGameState<MathraxGame, MathraxGameMove, MathraxGameState> {
    public int[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public MathraxGameState(MathraxGame game) {
        super(game);
        objArray = new int[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
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

    public boolean setObject(MathraxGameMove move) {
        if (!isValid(move.p) || game.get(move.p) != 0 || get(move.p) == move.obj) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(MathraxGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != 0) return false;
        int o = get(p);
        move.obj = (o + 1) % (cols() + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 6/Mathrax

        Summary
        Diagonal Math Wiz

        Description
        1. The goal is to input numbers 1 to N, where N is the board size, following
           the hints in the intersections.
        2. A number must appear once for every row and column.
        3. The tiny numbers and sign in the intersections tell you the result of
           the operation between the two opposite diagonal tiles. This is valid
           for both pairs of numbers surrounding the hint.
        4. In some puzzles, there will be 'E' or 'O' as hint. This means that all
           four tiles are either (E)ven or (O)dd numbers.
    */
    private void updateIsSolved() {
        isSolved = true;
        F<List<Integer>, HintState> f = nums -> {
            int size = nums.size();
            List<Integer> nums2 = iterableSet(Ord.intOrd, nums).toJavaList();
            // 1. The goal is to input numbers 1 to N, where N is the board size.
            HintState s = nums2.get(0) == 0 ? HintState.Normal :
                    nums2.size() == size ? HintState.Complete : HintState.Error;
            if (s != HintState.Complete) isSolved = false;
            return s;
        };
        // 2. A number must appear once for every row.
        range(0, rows()).foreachDoEffect(r -> row2state[r] = f.f(range(0, cols()).map(c -> get(r, c)).toJavaList()));
        // 2. A number must appear once for every column.
        range(0, cols()).foreachDoEffect(c -> col2state[c] = f.f(range(0, rows()).map(r -> get(r, c)).toJavaList()));
        for (Map.Entry<Position, MathraxHint> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            MathraxHint h = entry.getValue();
            F2<Integer, Integer, HintState> g = (n1, n2) -> {
                if (n1 == 0 || n2 == 0) return HintState.Normal;
                int n = h.result;
                switch (h.op) {
                // 3. The tiny numbers and sign in the intersections tell you the result of
                // the operation between the two opposite diagonal tiles.
                case '+':
                    return n1 + n2 == n ? HintState.Complete : HintState.Error;
                case '-':
                    return n1 - n2 == n || n2 - n1 == n ? HintState.Complete : HintState.Error;
                case '*':
                    return n1 * n2 == n ? HintState.Complete : HintState.Error;
                case '/':
                    return n1 / n2 * n2 == n * n2 || n2 / n1 * n1 == n * n1 ? HintState.Complete : HintState.Error;
                // 4. In some puzzles, there will be 'E' or 'O' as hint. This means that all
                // four tiles are either (E)ven or (O)dd numbers.
                case 'O':
                    return n1 % 2 == 1 && n2 % 2 == 1 ? HintState.Complete : HintState.Error;
                case 'E':
                    return n1 % 2 == 0 && n2 % 2 == 0 ? HintState.Complete : HintState.Error;
                }
                return HintState.Normal;
            };
            List<Integer> nums = array(MathraxGame.offset2).map(os -> get(p.add(os))).toJavaList();
            // 3. This is valid for both pairs of numbers surrounding the hint.
            HintState s1 = g.f(nums.get(0), nums.get(1)), s2 = g.f(nums.get(2), nums.get(3));
            HintState s = s1 == HintState.Error || s2 == HintState.Error ? HintState.Error :
                    s1 == HintState.Complete && s2 == HintState.Complete ? HintState.Complete : HintState.Normal;
            pos2state.put(p, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
