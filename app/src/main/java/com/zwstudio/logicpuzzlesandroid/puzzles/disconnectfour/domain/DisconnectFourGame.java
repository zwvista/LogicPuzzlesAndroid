package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.List;

import fj.F2;

public class DisconnectFourGame extends CellsGame<DisconnectFourGame, DisconnectFourGameMove, DisconnectFourGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public DisconnectFourObject[] objArray;

    public DisconnectFourObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public DisconnectFourObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, DisconnectFourObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, DisconnectFourObject obj) {
        set(p.row, p.col, obj);
    }

    public DisconnectFourGame(List<String> layout, GameInterface<DisconnectFourGame, DisconnectFourGameMove, DisconnectFourGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new DisconnectFourObject[rows() * cols()];

        for (int r = 0, i = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                objArray[i++] = ch == 'Y' ? DisconnectFourObject.Yellow :
                        ch == 'R' ? DisconnectFourObject.Red : DisconnectFourObject.Empty;
            }
        }

        DisconnectFourGameState state = new DisconnectFourGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(DisconnectFourGameMove move, F2<DisconnectFourGameState, DisconnectFourGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        DisconnectFourGameState state = cloner.deepClone(state());
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

    public boolean switchObject(DisconnectFourGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(DisconnectFourGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public DisconnectFourObject getObject(Position p) {
        return state().get(p);
    }

    public DisconnectFourObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public AllowedObjectState pos2State(Position p) {
        return state().pos2state.get(p);
    }
}
