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

/**
 * Created by zwvista on 2016/09/29.
 */

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
        iOS Game: Logic Games/Puzzle Set 13/Robot Fences

        Summary
        BZZZZliip ...cows?

        Description
        1. A bit like Robot Crosswords, you need to fill each region with a
           randomly ordered sequence of numbers.
        2. Numbers can only be in range 1 to N where N is the board size.
        3. No same number can appear in the same row or column.
    */
    private void updateIsSolved() {
        isSolved = true;
        F<List<Character>, HintState> f = nums -> {
            List<Character> nums2 = iterableList(nums).filter(ch -> !" .X".contains(String.valueOf(ch))).toJavaList();
            HintState s = nums2.size() == game.chMax - '0' &&
                    nums2.size() == new HashSet<Character>(nums2).size() ? HintState.Complete : HintState.Error;
            if (s != HintState.Complete) isSolved = false;
            return s;
        };
        range(0, rows()).foreachDoEffect(r -> row2state[r] = f.f(range(0, cols()).map(c -> get(r, c)).toJavaList()));
        range(0, cols()).foreachDoEffect(c -> col2state[c] = f.f(range(0, rows()).map(r -> get(r, c)).toJavaList()));
        for (Position p : game.noughts) {
            char ch = get(p);
            HintState s = ch == ' ' || ch == '.' ? HintState.Normal : HintState.Complete;
            pos2state.put(p, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
