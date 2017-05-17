package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.List;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class MasyuGame extends CellsGame<MasyuGame, MasyuGameMove, MasyuGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public char[] objArray;
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

    public MasyuGame(List<String> layout, GameInterface<MasyuGame, MasyuGameMove, MasyuGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new char[rows() * cols()];
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                set(r, c, ch);
            }
        }
        MasyuGameState state = new MasyuGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(MasyuGameMove move, F2<MasyuGameState, MasyuGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        MasyuGameState state = cloner.deepClone(state());
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

    public boolean setObject(MasyuGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public Boolean[] getObject(Position p) {
        return state().get(p);
    }

    public Boolean[] getObject(int row, int col) {
        return state().get(row, col);
    }
}
