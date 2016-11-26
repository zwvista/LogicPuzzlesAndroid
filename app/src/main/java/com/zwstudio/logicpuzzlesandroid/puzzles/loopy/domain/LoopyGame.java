package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain;

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

public class LoopyGame extends CellsGame<LoopyGame, LoopyGameMove, LoopyGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();

    public LoopyGame(List<String> layout, GameInterface<LoopyGame, LoopyGameMove, LoopyGameState> gi) {
        super(gi);
        size = new Position(layout.size() + 1, layout.get(0).length() + 1);
        for (int r = 0; r < rows() - 1; r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch >= '0' && ch <= '9') {
                    int n = ch - '0';
                    pos2hint.put(p, n);
                }
            }
        }
        LoopyGameState state = new LoopyGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(LoopyGameMove move, F2<LoopyGameState, LoopyGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        LoopyGameState state = cloner.deepClone(state());
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

    public boolean switchObject(LoopyGameMove move, LoopyMarkerOptions markerOption) {
        return changeObject(move, (state, move2) -> state.switchObject(markerOption, move2));
    }

    public boolean setObject(LoopyGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public LoopyObject[] getObject(Position p) {
        return state().get(p);
    }

    public LoopyObject[] getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getHintState(Position p) {
        return state().pos2state.get(p);
    }
}
