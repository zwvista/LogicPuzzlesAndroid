package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.List.iterableList;
import static fj.data.Stream.range;

public class NoughtsAndCrossesGameState extends CellsGameState<NoughtsAndCrossesGame, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState> {
    public char[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public NoughtsAndCrossesGameState(NoughtsAndCrossesGame game) {
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
    public void set(int row, int col, char obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, char obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(NoughtsAndCrossesGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != ' ' || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(NoughtsAndCrossesGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        Position p = move.p;
        if (!isValid(p) || game.get(p) != ' ') return false;
        int o = get(p);
        move.obj = 
                o == ' ' ? markerOption == MarkerOptions.MarkerFirst ? '.' : '1' :
                o == '.' ? markerOption == MarkerOptions.MarkerFirst ? '1' : ' ' :
                o == game.chMax ? markerOption == MarkerOptions.MarkerLast ? '.' : ' ' :
                (char)(o + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Noughts & Crosses

        Summary
        Spot the Number

        Description
        1. Place all numbers from 1 to N on each row and column - just once,
           without repeating.
        2. In other words, all numbers must appear just once on each row and column.
        3. A circle marks where a number must go.
        4. A cross marks where no number can go.
        5. All other cells can contain a number or be empty.
    */
    private void updateIsSolved() {
        isSolved = true;
        F<List<Character>, HintState> f = nums -> {
            // 4. A cross marks where no number can go.
            // 5. All other cells can contain a number or be empty.
            List<Character> nums2 = iterableList(nums).filter(ch -> !" .X".contains(String.valueOf(ch))).toJavaList();
            // 2. All numbers must appear just once.
            HintState s = nums2.size() == game.chMax - '0' &&
                    nums2.size() == new HashSet<Character>(nums2).size() ? HintState.Complete : HintState.Error;
            if (s != HintState.Complete) isSolved = false;
            return s;
        };
        // 2. All numbers must appear just once on each row.
        range(0, rows()).foreachDoEffect(r -> row2state[r] = f.f(range(0, cols()).map(c -> get(r, c)).toJavaList()));
        // 2. All numbers must appear just once on each column.
        range(0, cols()).foreachDoEffect(c -> col2state[c] = f.f(range(0, rows()).map(r -> get(r, c)).toJavaList()));
        // 3. A circle marks where a number must go.
        for (Position p : game.noughts) {
            char ch = get(p);
            HintState s = ch == ' ' || ch == '.' ? HintState.Normal : HintState.Complete;
            pos2state.put(p, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
