package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TierraDelFuegoGame extends CellsGame<TierraDelFuegoGame, TierraDelFuegoGameMove, TierraDelFuegoGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, Character> pos2hint = new HashMap<>();

    public TierraDelFuegoGame(List<String> layout, GameInterface<TierraDelFuegoGame, TierraDelFuegoGameMove, TierraDelFuegoGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch != ' ') pos2hint.put(p, ch);
            }
        }

        TierraDelFuegoGameState state = new TierraDelFuegoGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(TierraDelFuegoGameMove move, F2<TierraDelFuegoGameState, TierraDelFuegoGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        TierraDelFuegoGameState state = cloner.deepClone(state());
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

    public boolean switchObject(TierraDelFuegoGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(TierraDelFuegoGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public TierraDelFuegoObject getObject(Position p) {
        return state().get(p);
    }

    public TierraDelFuegoObject getObject(int row, int col) {
        return state().get(row, col);
    }
}
