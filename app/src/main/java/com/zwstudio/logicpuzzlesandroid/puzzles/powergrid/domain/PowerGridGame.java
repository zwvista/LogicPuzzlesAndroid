package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class PowerGridGame extends CellsGame<PowerGridGame, PowerGridGameMove, PowerGridGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public int[] row2hint;
    public int[] col2hint;

    public PowerGridGame(List<String> layout, GameInterface<PowerGridGame, PowerGridGameMove, PowerGridGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size() - 1, layout.get(0).length() - 1);
        row2hint = new int[rows()];
        col2hint = new int[cols()];

        for (int r = 0; r < rows() + 1; r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols() + 1; c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch >= '0' && ch <= '9') {
                    int n = ch - '0';
                    if (r == rows())
                        col2hint[c] = n;
                    else if (c == cols())
                        row2hint[r] = n;
                }
            }
        }
        PowerGridGameState state = new PowerGridGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(PowerGridGameMove move, F2<PowerGridGameState, PowerGridGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        PowerGridGameState state = cloner.deepClone(state());
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

    public boolean switchObject(PowerGridGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(PowerGridGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public PowerGridObject getObject(Position p) {
        return state().get(p);
    }

    public PowerGridObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getRowState(int row) {
        return state().row2state[row];
    }

    public HintState getColState(int col) {
        return state().col2state[col];
    }
}
