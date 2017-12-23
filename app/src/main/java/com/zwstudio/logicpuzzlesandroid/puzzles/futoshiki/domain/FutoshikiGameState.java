package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F0;
import fj.Ord;
import fj.data.Stream;

import static fj.data.Set.iterableSet;

public class FutoshikiGameState extends CellsGameState<FutoshikiGame, FutoshikiGameMove, FutoshikiGameState> {
    public char[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public FutoshikiGameState(FutoshikiGame game) {
        super(game);
        objArray = new char[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved();
    }

    public char get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public char get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, char dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, char obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(FutoshikiGameMove move) {
        Position p = move.p;
        if (!(isValid(p) && p.row % 2 == 0 && p.col % 2 == 0 && game.get(p) == ' ') || get(move.p) == move.obj) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(FutoshikiGameMove move) {
        Position p = move.p;
        if (!(isValid(p) && p.row % 2 == 0 && p.col % 2 == 0 && game.get(p) == ' ')) return false;
        int o = get(p);
        move.obj = o == ' ' ? '1' : o == '1' + rows() / 2 ? ' ' : (char)(o + 1);
        return setObject(move);
    }

    private static Stream<Integer> range(final int from, final int to, final int step) {
        return from >= to ? Stream.nil() : Stream.cons(from, () -> range(from + step, to, step));
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Futoshiki

        Summary
        Fill the rows and columns with numbers, respecting the relations

        Description
        1. In a manner similar to Sudoku, you have to put in each row and column
           numbers ranging from 1 to N, where N is the puzzle board size.
        2. The hints you have are the 'less than'/'greater than' signs between tiles.
        3. Remember you can't repeat the same number in a row or column.

        Variation
        4. Some boards, instead of having less/greater signs, have just a line
           separating the tiles.
        5. That separator hints at two tiles with consecutive numbers, i.e. 1-2
           or 3-4..
        6. Please note that in this variation consecutive numbers MUST have a
           line separating the tiles. Otherwise they're not consecutive.
        7. This Variation is a taste of a similar game: 'Consecutives'.
    */
    private void updateIsSolved() {
        isSolved = true;
        F<List<Character>, HintState> f = nums -> {
            List<Character> nums2 = iterableSet(Ord.charOrd, nums).toJavaList();
            // 1. You have to put in each row and column
            // numbers ranging from 1 to N, where N is the puzzle board size.
            HintState s = nums2.get(0) == ' ' ? HintState.Normal :
                    nums2.size() == nums.size() ? HintState.Complete : HintState.Error;
            if (s != HintState.Complete) isSolved = false;
            return s;
        };
        // 3. Remember you can't repeat the same number in a row.
        range(0, rows(), 2).foreachDoEffect(r -> row2state[r] = f.f(range(0, cols(), 2).map(c -> get(r, c)).toJavaList()));
        // 3. Remember you can't repeat the same number in a column.
        range(0, cols(), 2).foreachDoEffect(c -> col2state[c] = f.f(range(0, rows(), 2).map(r -> get(r, c)).toJavaList()));
        for (Map.Entry<Position, Character> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            char h = entry.getValue();
            int r = p.row, c = p.col;
            char ch1 = r % 2 == 0 ? get(r, c - 1) : get(r - 1, c);
            char ch2 = r % 2 == 0 ? get(r, c + 1) : get(r + 1, c);
            F0<HintState> g = () -> {
                if (ch1 == ' ' || ch2 == ' ') return HintState.Normal;
                int n1 = ch1 - '0', n2 = ch2 - '0';
                switch (h) {
                // 2. The hints you have are the 'less than'/'greater than' signs between tiles.
                case '^':
                case '<':
                    return n1 < n2 ? HintState.Complete : HintState.Error;
                case 'v':
                case '>':
                    return n1 > n2 ? HintState.Complete : HintState.Error;
                default:
                    return HintState.Normal;
                }
            };
            HintState s = g.f();
            pos2state.put(p, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
