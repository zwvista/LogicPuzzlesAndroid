package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.List;

import fj.F2;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class NoughtsAndCrossesGame extends CellsGame<NoughtsAndCrossesGame, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };
    public static Position offset2[] = {
            new Position(0, 0),
            new Position(1, 1),
            new Position(1, 1),
            new Position(0, 0),
    };
    public static int dirs[] = {1, 0, 3, 2};

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

    public List<Position> noughts = new ArrayList<>();
    public char chMax;

    public NoughtsAndCrossesGame(List<String> layout, char chMax, GameInterface<NoughtsAndCrossesGame, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        this.chMax = chMax;
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new char[rows() * cols()];
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch == 'O') noughts.add(p);
                set(p, ch == 'O' ? ' ' : ch);
            }
        }

        NoughtsAndCrossesGameState state = new NoughtsAndCrossesGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(NoughtsAndCrossesGameMove move, F2<NoughtsAndCrossesGameState, NoughtsAndCrossesGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        NoughtsAndCrossesGameState state = cloner.deepClone(state());
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

    public boolean switchObject(NoughtsAndCrossesGameMove move) {
        return changeObject(move, NoughtsAndCrossesGameState::switchObject);
    }

    public boolean setObject(NoughtsAndCrossesGameMove move) {
        return changeObject(move, NoughtsAndCrossesGameState::setObject);
    }

    public char getObject(Position p) {
        return state().get(p);
    }

    public char getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getRowState(int row) {
        return state().row2state[row];
    }

    public HintState getColState(int col) {
        return state().col2state[col];
    }

    public HintState getPosState(Position p) {
        return defaultIfNull(state().pos2state.get(p), HintState.Normal);
    }
}
