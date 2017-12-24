package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class PairakabeGame extends CellsGame<PairakabeGame, PairakabeGameMove, PairakabeGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };
    public static Position offset2[] = {
            new Position(0, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(1, 1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();

    public PairakabeGame(List<String> layout, GameInterface<PairakabeGame, PairakabeGameMove, PairakabeGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch >= '0' && ch <= '9') {
                    int n = ch - '0';
                    pos2hint.put(p, n);
                }
            }
        }
        PairakabeGameState state = new PairakabeGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(PairakabeGameMove move, F2<PairakabeGameState, PairakabeGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        PairakabeGameState state = cloner.deepClone(state());
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

    public boolean switchObject(PairakabeGameMove move) {
        return changeObject(move, PairakabeGameState::switchObject);
    }

    public boolean setObject(PairakabeGameMove move) {
        return changeObject(move, PairakabeGameState::setObject);
    }

    public PairakabeObject getObject(Position p) {
        return state().get(p);
    }

    public PairakabeObject getObject(int row, int col) {
        return state().get(row, col);
    }
}
