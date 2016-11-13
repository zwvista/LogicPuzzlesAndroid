package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SkyscrapersGame extends CellsGame<SkyscrapersGame, SkyscrapersGameMove, SkyscrapersGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public boolean isValid(int row, int col) {
        return row >= 1 && col >= 1 && row < size.row - 1 && col < size.col - 1;
    }

    public int[] objArray;
    public int intMax = 1;
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

    public SkyscrapersGame(List<String> layout, GameInterface<SkyscrapersGame, SkyscrapersGameMove, SkyscrapersGameState> gi) {
        super(gi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new int[rows() * cols()];

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                char ch = str.charAt(c);
                int n = ch == ' ' ? 0 : ch - '0';
                set(r, c, n);
                if (intMax < n) intMax = n;
            }
        }

        SkyscrapersGameState state = new SkyscrapersGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(SkyscrapersGameMove move, F2<SkyscrapersGameState, SkyscrapersGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        SkyscrapersGameState state = cloner.deepClone(state());
        boolean changed = f.f(state, move);
        if (changed) {
            states.add(state);
            stateIndex++;
            moves.add(move);
            moveAdded(move);
            levelUpdated(states.get(stateIndex - 1), state);
        }
        return changed;
   }

    public boolean switchObject(SkyscrapersGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(SkyscrapersGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public int getObject(Position p) {
        return state().get(p);
    }

    public int getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getState(int row, int col) {
        return state().getState(row, col);
    }
}
