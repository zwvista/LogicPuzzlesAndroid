package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain;

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

public class ParkLakesGame extends CellsGame<ParkLakesGame, ParkLakesGameMove, ParkLakesGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();

    public ParkLakesGame(List<String> layout, GameInterface<ParkLakesGame, ParkLakesGameMove, ParkLakesGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length() / 2);

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                String s = str.substring(c * 2, c  * 2 + 2);
                if (!s.equals("  "))
                    pos2hint.put(p, s.equals(" ?") ? -1 : Integer.parseInt(s.trim()));
            }
        }

        ParkLakesGameState state = new ParkLakesGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(ParkLakesGameMove move, F2<ParkLakesGameState, ParkLakesGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        ParkLakesGameState state = cloner.deepClone(state());
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

    public boolean switchObject(ParkLakesGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(ParkLakesGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public ParkLakesObject getObject(Position p) {
        return state().get(p);
    }

    public ParkLakesObject getObject(int row, int col) {
        return state().get(row, col);
    }
}
