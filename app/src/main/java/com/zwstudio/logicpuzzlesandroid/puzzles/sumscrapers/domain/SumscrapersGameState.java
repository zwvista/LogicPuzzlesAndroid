package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SumscrapersGameState extends CellsGameState<SumscrapersGame, SumscrapersGameMove, SumscrapersGameState> {
    private int[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public SumscrapersGameState(SumscrapersGame game) {
        super(game);
        objArray = new int[rows() * cols()];
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                set(r, c, game.get(r, c));
        row2state = new HintState[rows() * 2];
        col2state = new HintState[cols() * 2];
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

    public HintState getState(int row, int col) {
        return row == 0 && col >= 1 && col < cols() - 1 ? col2state[col * 2] :
                row == rows() - 1 && col >= 1 && col < cols() - 1 ? col2state[col * 2 + 1] :
                col == 0 && row >= 1 && row < rows() - 1 ? row2state[row * 2] :
                col == cols() - 1 && row >= 1 && row < rows() - 1 ? row2state[row * 2 + 1] :
                HintState.Normal;
    }

    public boolean setObject(SumscrapersGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(SumscrapersGameMove move) {
        Position p = move.p;
        if (!isValid(p)) return false;
        int o = get(p);
        move.obj = (o + 1) % (game.intMax() + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 6/Sumscrapers

        Summary
        Sum the skyline!

        Description
        1. The grid in the center represents a city from above. Each cell contain
           a skyscraper, of different height.
        2. The goal is to guess the height of each Skyscraper.
        3. Each row and column can't have two Skyscrapers of the same height.
        4. The numbers on the boarders tell the SUM of the heights of the Skyscrapers
           you see from there, keeping mind that a higher skyscraper hides a lower one.
           Skyscrapers are numbered from 1 (lowest) to the grid size (highest).
        5. Each row and column can't have similar Skyscrapers.
    */
    private void updateIsSolved() {
        isSolved = true;
        List<List<Integer>> numss = new ArrayList<>();
        List<Integer> nums = new ArrayList<>();
        for (int r = 1; r < rows() - 1; r++) {
            int h1 = get(r, 0), h2 = get(r, cols() - 1);
            int n1 = 0, n2 = 0;
            int n11 = 0, n21 = 0;
            nums.clear();
            for (int c = 1; c < cols() - 1; c++) {
                int n12 = get(r, c), n22 = get(r, cols() - 1 - c);
                if (n11 < n12) {n11 = n12; n1 += n12;}
                if (n21 < n22) {n21 = n22; n2 += n22;}
                if (n12 == 0) continue;
                if (nums.contains(n12))
                    isSolved = false;
                else
                    nums.add(n12);
            }
            // 4. The numbers on the boarders tell you the SUM of the heights skyscrapers
            // you see from there, keeping mind that a higher skyscraper hides a lower one.
            // Skyscrapers are numbered from 1(lowest) to the grid size(highest).
            HintState s1 = n1 == 0 ? HintState.Normal : n1 == h1 ? HintState.Complete : HintState.Error;
            HintState s2 = n2 == 0 ? HintState.Normal : n2 == h2 ? HintState.Complete : HintState.Error;
            row2state[r * 2] = s1; row2state[r * 2 + 1] = s2;
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false;
            if (nums.size() != game.intMax()) isSolved = false;
            // 5. Each row and column can't have similar Skyscrapers.
            if (numss.contains(nums))
                isSolved = false;
            else
                numss.add(nums);
        }
        for (int c = 1; c < cols() - 1; c++) {
            int h1 = get(0, c), h2 = get(rows() - 1, c);
            int n1 = 0, n2 = 0;
            int n11 = 0, n21 = 0;
            nums.clear();
            for (int r = 1; r < rows() - 1; r++) {
                int n12 = get(r, c), n22 = get(rows() - 1 - r, c);
                if (n11 < n12) {n11 = n12; n1 += n12;}
                if (n21 < n22) {n21 = n22; n2 += n22;}
                if (n12 == 0) continue;
                if (nums.contains(n12))
                    isSolved = false;
                else
                    nums.add(n12);
            }
            // 4. The numbers on the boarders tell you the SUM of the heights skyscrapers
            // you see from there, keeping mind that a higher skyscraper hides a lower one.
            // Skyscrapers are numbered from 1(lowest) to the grid size(highest).
            HintState s1 = n1 == 0 ? HintState.Normal : n1 == h1 ? HintState.Complete : HintState.Error;
            HintState s2 = n2 == 0 ? HintState.Normal : n2 == h2 ? HintState.Complete : HintState.Error;
            col2state[c * 2] = s1; col2state[c * 2 + 1] = s2;
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false;
            if (nums.size() != game.intMax()) isSolved = false;
            // 5. Each row and column can't have similar Skyscrapers.
            if (numss.contains(nums))
                isSolved = false;
            else
                numss.add(nums);
        }
    }
}
