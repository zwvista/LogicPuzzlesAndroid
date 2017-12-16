package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.F2;

public class FourMeNotGame extends CellsGame<FourMeNotGame, FourMeNotGameMove, FourMeNotGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public FourMeNotObject[] objArray;

    public FourMeNotObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public FourMeNotObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, FourMeNotObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, FourMeNotObject obj) {
        set(p.row, p.col, obj);
    }

    public FourMeNotGame(List<String> layout, GameInterface<FourMeNotGame, FourMeNotGameMove, FourMeNotGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new FourMeNotObject[rows() * cols()];

        for (int r = 0, i = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                objArray[i++] = ch == 'F' ? new FourMeNotTreeObject() :
                        ch == 'B' ? new FourMeNotTreeObject() : new FourMeNotEmptyObject();
            }
        }

        FourMeNotGameState state = new FourMeNotGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(FourMeNotGameMove move, F2<FourMeNotGameState, FourMeNotGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        FourMeNotGameState state = cloner.deepClone(state());
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

    public boolean switchObject(FourMeNotGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(FourMeNotGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public FourMeNotObject getObject(Position p) {
        return state().get(p);
    }

    public FourMeNotObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState pos2State(Position p) {
        return state().pos2state.get(p);
    }
}
