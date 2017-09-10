package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class FenceSentinelsGame extends CellsGame<FenceSentinelsGame, FenceSentinelsGameMove, FenceSentinelsGameState> {
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
    public static int dirs[] = { 1, 0, 3, 2 };

    public boolean isValid(int row, int col) {
        return row >= 0 && col >= 0 && row < size.row - 1 && col < size.col - 1;
    }

    public Map<Position, Integer> pos2hint = new HashMap<>();

    public FenceSentinelsGame(List<String> layout, GameInterface<FenceSentinelsGame, FenceSentinelsGameMove, FenceSentinelsGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
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
        FenceSentinelsGameState state = new FenceSentinelsGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(FenceSentinelsGameMove move, F2<FenceSentinelsGameState, FenceSentinelsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        FenceSentinelsGameState state = cloner.deepClone(state());
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

    public boolean switchObject(FenceSentinelsGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(FenceSentinelsGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public GridLineObject[] getObject(Position p) {
        return state().get(p);
    }

    public GridLineObject[] getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState hint2State(Position p) {
        return state().pos2state.get(p);
    }
}
