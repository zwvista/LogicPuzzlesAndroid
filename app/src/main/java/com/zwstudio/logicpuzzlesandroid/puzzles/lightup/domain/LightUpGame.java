package com.zwstudio.logicpuzzlesandroid.puzzles.lightup.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightUpGame extends CellsGame<LightUpGame, LightUpGameMove, LightUpGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();

    public LightUpGame(List<String> layout, GameInterface<LightUpGame, LightUpGameMove, LightUpGameState> gi) {
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
        LightUpGameState state = new LightUpGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(LightUpGameMove move, F2<LightUpGameState, LightUpGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        LightUpGameState state = cloner.deepClone(state());
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

    public boolean switchObject(LightUpGameMove move, LightUpMarkerOptions markerOption, boolean normalLightbulbsOnly) {
        return changeObject(move, (state, move2) -> state.switchObject(markerOption, normalLightbulbsOnly, move2));
    }

    public boolean setObject(LightUpGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public LightUpObject getObject(Position p) {
        return state().get(p);
    }

    public LightUpObject getObject(int row, int col) {
        return state().get(row, col);
    }
}
