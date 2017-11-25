package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fj.function.Effect1;

/**
 * Created by zwvista on 2016/09/29.
 */

public class RippleEffectGameState extends CellsGameState<RippleEffectGame, RippleEffectGameMove, RippleEffectGameState> {
    public int[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public RippleEffectGameState(RippleEffectGame game) {
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

    public boolean setObject(RippleEffectGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != 0 || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(RippleEffectGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != 0) return false;
        move.obj = (get(p) + 1) % (game.areas.get(game.pos2area.get(p)).size() + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 5/Ripple Effect

        Summary
        Fill the Room with the numbers, but take effect of the Ripple Effect

        Description
        1. The goal is to fill the Rooms you see on the board, with numbers 1 to room size.
        2. While doing this, you must consider the Ripple Effect. The same number
           can only appear on the same row or column at the distance of the number
           itself.
        3. For example a 2 must be separated by another 2 on the same row or
           column by at least two tiles.
    */
    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                pos2state.put(new Position(r, c), HintState.Normal);
        Map<Integer, List<Position>> num2rng = new HashMap<>();
        Effect1<Boolean> f = sameRow -> {
            for (Map.Entry<Integer, List<Position>> entry : num2rng.entrySet()) {
                int n = entry.getKey();
                List<Position> rng = entry.getValue();
                Set<Integer> indexes = new HashSet<>();
                for (int i = 0; i < rng.size() - 1; i++)
                    if (sameRow ? rng.get(i + 1).col - rng.get(i).col <= n : rng.get(i + 1).row - rng.get(i).row <= n) {
                        indexes.add(n); indexes.add(n + 1);
                    }
                if (!indexes.isEmpty()) isSolved = false;
                for (int i = 0; i < rng.size(); i++)
                    if (indexes.contains(i))
                        pos2state.put(rng.get(i), HintState.Error);
            }
        };
        for (int r = 0; r < rows(); r++) {
            num2rng.clear();
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = get(p);
                if (n == 0) {isSolved = false; continue;}
                List<Position> rng = num2rng.get(n);
                if (rng == null) rng = new ArrayList<>();
                rng.add(p);
                num2rng.put(n, rng);
            }
            f.f(true);
        }
        for (int c = 0; c < cols(); c++) {
            num2rng.clear();
            for (int r = 0; r < rows(); r++) {
                Position p = new Position(r, c);
                int n = get(p);
                if (n == 0) {isSolved = false; continue;}
                List<Position> rng = num2rng.get(n);
                if (rng == null) rng = new ArrayList<>();
                rng.add(p);
                num2rng.put(n, rng);
            }
            f.f(false);
        }
        for (List<Position> area : game.areas) {
            num2rng.clear();
            for (Position p : area) {
                int n = get(p);
                if (n == 0) continue;
                List<Position> rng = num2rng.get(n);
                if (rng == null) rng = new ArrayList<>();
                rng.add(p);
                num2rng.put(n, rng);
            }
            boolean anySame = false;
            for (List<Position> rng : num2rng.values()) {
                if (rng.size() <= 1) continue;
                anySame = true; isSolved = false;
                for (Position p : rng)
                    pos2state.put(p, HintState.Error);
            }
            if (!anySame)
                for (Position p : area)
                    if (pos2state.get(p) != HintState.Error)
                        pos2state.put(p, HintState.Complete);
        }
    }
}
