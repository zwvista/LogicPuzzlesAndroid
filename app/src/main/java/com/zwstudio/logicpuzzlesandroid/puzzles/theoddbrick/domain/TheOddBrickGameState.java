package com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.domain;

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

public class TheOddBrickGameState extends CellsGameState<TheOddBrickGame, TheOddBrickGameMove, TheOddBrickGameState> {
    public int[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;
    public HintState[] area2state;

    public TheOddBrickGameState(TheOddBrickGame game) {
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

    public boolean setObject(TheOddBrickGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != 0 || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TheOddBrickGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != 0) return false;
        int o = get(p);
        move.obj = (o + 1) % (cols() + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 14/The Odd Brick

        Summary
        Even Bricks are strange, sometimes

        Description
        1. On the board there is a wall, made of 2*1 and 1*1 bricks.
        2. Each 2*1 brick contains and odd and an even number, while 1*1 bricks
           can contain any number.
        3. Each row and column contains numbers 1 to N, where N is the side of
           the board.
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
        range(0, game.areas.size()).foreachDoEffect(i -> {
            List<Integer> nums = iterableList(game.areas.get(i)).map(p -> get(p)).toJavaList();
            area2state[i] = nums.contains(0) ? HintState.Normal :
                    nums.size() == 1 || nums.get(0) % 2 == 0 && nums.get(1) % 2 == 1 ||
                    nums.get(0) % 2 == 1 && nums.get(1) % 2 == 0 ? HintState.Complete : HintState.Error;
            if (area2state[i] != HintState.Complete) isSolved = false;
        });
    }
}
