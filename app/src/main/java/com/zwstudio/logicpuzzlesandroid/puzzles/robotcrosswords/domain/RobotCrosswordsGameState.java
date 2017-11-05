package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.Ord;

import static fj.data.List.iterableList;
import static fj.data.Set.iterableSet;

/**
 * Created by zwvista on 2016/09/29.
 */

public class RobotCrosswordsGameState extends CellsGameState<RobotCrosswordsGame, RobotCrosswordsGameMove, RobotCrosswordsGameState> {
    public int[] objArray;
    public Map<Position, HintState> pos2horzState = new HashMap<>();
    public Map<Position, HintState> pos2vertState = new HashMap<>();

    public RobotCrosswordsGameState(RobotCrosswordsGame game) {
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

    public boolean setObject(RobotCrosswordsGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != 0 || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(RobotCrosswordsGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != 0) return false;
        int o = get(p);
        move.obj = (o + 1) % 10;
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/RobotCrosswords

        Summary
        BZZZZliip 4 across?

        Description
        1. In a possible crossword for Robots, letters are substituted with digits.
        2. Each 'word' is formed by an uninterrupted sequence of numbers (i.e.
           2-3-4-5), but in any order (i.e. 3-4-2-5).
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int i = 0; i < game.areas.size(); i++) {
            List<Position> a = game.areas.get(i);
            List<Integer> nums = iterableList(a).map(p -> get(p)).toJavaList();
            int size = nums.size();
            List<Integer> nums2 = iterableSet(Ord.intOrd, nums).toJavaList();
            HintState s = nums2.get(0) == 0 ? HintState.Normal :
                    nums2.size() == size && nums2.get(nums2.size() - 1) - nums2.get(0) + 1 == size ?
                    HintState.Complete : HintState.Error;
            for (Position p : a)
                (i < game.horzAreaCount ? pos2horzState : pos2vertState).put(p, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
