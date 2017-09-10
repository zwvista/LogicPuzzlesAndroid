package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BootyIslandGame extends CellsGame<BootyIslandGame, BootyIslandGameMove, BootyIslandGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(-1, 1),
            new Position(0, 1),
            new Position(1, 1),
            new Position(1, 0),
            new Position(1, -1),
            new Position(0, -1),
            new Position(-1, -1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();

    public BootyIslandGame(List<String> layout, GameInterface<BootyIslandGame, BootyIslandGameMove, BootyIslandGameState> gi, GameDocumentInterface gdi) {
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
        BootyIslandGameState state = new BootyIslandGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(BootyIslandGameMove move, F2<BootyIslandGameState, BootyIslandGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        BootyIslandGameState state = cloner.deepClone(state());
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

    public boolean switchObject(BootyIslandGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(BootyIslandGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public BootyIslandObject getObject(Position p) {
        return state().get(p);
    }

    public BootyIslandObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState hint2State(Position p) {
        return state().pos2state.get(p);
    }
}
