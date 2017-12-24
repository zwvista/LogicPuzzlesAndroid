package com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.F2;

public class AbcGame extends CellsGame<AbcGame, AbcGameMove, AbcGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public boolean isValid(int row, int col) {
        return row >= 1 && col >= 1 && row < size.row - 1 && col < size.col - 1;
    }

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

    public AbcGame(List<String> layout, GameInterface<AbcGame, AbcGameMove, AbcGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
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

        AbcGameState state = new AbcGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(AbcGameMove move, F2<AbcGameState, AbcGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        AbcGameState state = cloner.deepClone(state());
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

    public boolean switchObject(AbcGameMove move) {
        return changeObject(move, AbcGameState::switchObject);
    }

    public boolean setObject(AbcGameMove move) {
        return changeObject(move, AbcGameState::setObject);
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
