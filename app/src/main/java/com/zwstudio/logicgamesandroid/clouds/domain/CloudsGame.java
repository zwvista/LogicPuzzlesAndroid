package com.zwstudio.logicgamesandroid.clouds.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.CellsGame;
import com.zwstudio.logicgamesandroid.logicgames.domain.GameInterface;
import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.util.ArrayList;
import java.util.List;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class CloudsGame extends CellsGame<CloudsGame, CloudsGameMove, CloudsGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public int[] row2hint;
    public int[] col2hint;
    public List<Position> pos2cloud = new ArrayList<>();

    public CloudsGame(List<String> layout, GameInterface<CloudsGame, CloudsGameMove, CloudsGameState> gi) {
        super(gi);
        size = new Position(layout.size() - 1, layout.get(0).length() - 1);
        row2hint = new int[rows()];
        col2hint = new int[cols()];

        CloudsGameState state = new CloudsGameState(this);
        state.row2state = new LogicGamesHintState[rows()];
        state.col2state = new LogicGamesHintState[cols()];

        for (int r = 0; r < rows() + 1; r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols() + 1; c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch == 'C')
                    pos2cloud.add(p);
                else if (ch >= '0' && ch <= '9') {
                    int n = ch - '0';
                    LogicGamesHintState s = n == 0 ? LogicGamesHintState.Complete : LogicGamesHintState.Normal;
                    if (r == rows()) {
                        col2hint[c] = n;
                        state.col2state[c] = s;
                    } else if (c == cols()) {
                        row2hint[r] = n;
                        state.row2state[r] = s;
                    }
                }
            }
        }
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(CloudsGameMove move, F2<CloudsGameState, CloudsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        CloudsGameState state = cloner.deepClone(state());
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

    public boolean switchObject(CloudsGameMove move, CloudsMarkerOptions markerOption) {
        return changeObject(move, (state, move2) -> state.switchObject(markerOption, move2));
    }

    public boolean setObject(CloudsGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public CloudsObject getObject(Position p) {
        return state().get(p);
    }

    public CloudsObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public LogicGamesHintState getRowState(int row) {
        return state().row2state[row];
    }

    public LogicGamesHintState getColState(int col) {
        return state().col2state[col];
    }
}
