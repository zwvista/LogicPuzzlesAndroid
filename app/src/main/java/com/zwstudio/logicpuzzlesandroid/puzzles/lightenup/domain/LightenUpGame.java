package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightenUpGame extends CellsGame<LightenUpGame, LightenUpGameMove, LightenUpGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();

    public LightenUpGame(List<String> layout, GameInterface<LightenUpGame, LightenUpGameMove, LightenUpGameState> gi) {
        super(gi);
        size = new Position(layout.size(), layout.get(0).length());
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch == 'W' || ch >= '0' && ch <= '9') {
                    int n = ch == 'W' ? -1 : (ch - '0');
                    pos2hint.put(p, n);
                }
            }
        }
        LightenUpGameState state = new LightenUpGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(LightenUpGameMove move, F2<LightenUpGameState, LightenUpGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        LightenUpGameState state = cloner.deepClone(state());
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

    public boolean switchObject(LightenUpGameMove move, MarkerOptions markerOption, boolean normalLightbulbsOnly) {
        return changeObject(move, (state, move2) -> state.switchObject(markerOption, normalLightbulbsOnly, move2));
    }

    public boolean setObject(LightenUpGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public LightenUpObject getObject(Position p) {
        return state().get(p);
    }

    public LightenUpObject getObject(int row, int col) {
        return state().get(row, col);
    }
}
