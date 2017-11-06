package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.F;
import fj.Ord;

import static fj.data.List.iterableList;
import static fj.data.Set.iterableSet;
import static fj.data.Stream.range;

/**
 * Created by zwvista on 2016/09/29.
 */

public class RobotFencesGameState extends CellsGameState<RobotFencesGame, RobotFencesGameMove, RobotFencesGameState> {
    public int[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;
    public HintState[] area2state;

    public RobotFencesGameState(RobotFencesGame game) {
        super(game);
        objArray = new int[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        area2state = new HintState[game.areas.size()];
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

    public boolean setObject(RobotFencesGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != 0 || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(RobotFencesGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != 0) return false;
        int o = get(p);
        move.obj = (o + 1) % (cols() + 1);
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
        F<List<Integer>, HintState> f = nums -> {
            int size = nums.size();
            List<Integer> nums2 = iterableSet(Ord.intOrd, nums).toJavaList();
            HintState s = nums2.get(0) == 0 ? HintState.Normal :
                    nums2.size() == size ? HintState.Complete : HintState.Error;
            if (s != HintState.Complete) isSolved = false;
            return s;
        };
        range(0, rows()).foreachDoEffect(r -> row2state[r] = f.f(range(0, cols()).map(c -> get(r, c)).toJavaList()));
        range(0, cols()).foreachDoEffect(c -> col2state[c] = f.f(range(0, rows()).map(r -> get(r, c)).toJavaList()));
        range(0, game.areas.size()).foreachDoEffect(i -> area2state[i] = f.f(iterableList(game.areas.get(i)).map(p -> get(p)).toJavaList()));
    }
}
