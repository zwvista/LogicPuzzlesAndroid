package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.Ord;
import fj.function.Effect2;

import static fj.data.List.iterableList;
import static fj.data.Set.iterableSet;
import static fj.data.Stream.range;

/**
 * Created by zwvista on 2016/09/29.
 */

public class RobotFencesGameState extends CellsGameState<RobotFencesGame, RobotFencesGameMove, RobotFencesGameState> {
    public int[] objArray;
    public RobotFencesInfo[] row2info;
    public RobotFencesInfo[] col2info;
    public RobotFencesInfo[] area2info;

    public RobotFencesGameState(RobotFencesGame game) {
        super(game);
        objArray = new int[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
        row2info = new RobotFencesInfo[rows()];
        for (int i = 0; i < row2info.length; i++)
            row2info[i] = new RobotFencesInfo();
        col2info = new RobotFencesInfo[cols()];
        for (int i = 0; i < col2info.length; i++)
            col2info[i] = new RobotFencesInfo();
        area2info = new RobotFencesInfo[game.areas.size()];
        for (int i = 0; i < area2info.length; i++)
            area2info[i] = new RobotFencesInfo();
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
        Effect2<List<Integer>, RobotFencesInfo> f = (nums, info) -> {
            int size = nums.size();
            List<Integer> nums2 = iterableSet(Ord.intOrd, nums).toJavaList();
            HintState s = nums2.get(0) == 0 ? HintState.Normal :
                    nums2.size() == size && nums2.get(nums2.size() - 1) - nums2.get(0) + 1 == size ?
                    HintState.Complete : HintState.Error;
            info.nums = iterableList(nums2).toStream().filter(i -> i != 0).foldLeft((acc, v) -> acc + v.toString(), "");
            info.state = s;
            if (s != HintState.Complete) isSolved = false;
        };
        range(0, rows()).foreachDoEffect(r -> f.f(range(0, cols()).map(c -> get(r, c)).toJavaList(), row2info[r]));
        range(0, cols()).foreachDoEffect(c -> f.f(range(0, rows()).map(r -> get(r, c)).toJavaList(), col2info[c]));
        range(0, game.areas.size()).foreachDoEffect(i -> f.f(iterableList(game.areas.get(i)).map(p -> get(p)).toJavaList(), area2info[i]));
    }
}
