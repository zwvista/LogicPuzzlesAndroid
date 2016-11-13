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

    public char[] objArray;
    public char chMax = 'A';
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

    public SkyscrapersGame(List<String> layout, GameInterface<SkyscrapersGame, SkyscrapersGameMove, SkyscrapersGameState> gi) {
        super(gi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new char[rows() * cols()];

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                char ch = str.charAt(c);
                set(r, c, ch);
                if (chMax < ch) chMax = ch;
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

    public char getObject(Position p) {
        return state().get(p);
    }

    public char getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getState(int row, int col) {
        return state().getState(row, col);
    }
}
