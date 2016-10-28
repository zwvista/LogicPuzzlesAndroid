package com.zwstudio.logicgamesandroid.slitherlink.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.CellsGame;
import com.zwstudio.logicgamesandroid.logicgames.domain.GameInterface;
import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SlitherLinkGame extends CellsGame<SlitherLinkGame, SlitherLinkGameMove, SlitherLinkGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();

    public SlitherLinkGame(List<String> layout, GameInterface<SlitherLinkGame, SlitherLinkGameMove, SlitherLinkGameState> gi) {
        super(gi);
        size = new Position(layout.size() + 1, layout.get(0).length() + 1);
        SlitherLinkGameState state = new SlitherLinkGameState(this);
        for (int r = 0; r < rows() - 1; r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch >= '0' && ch <= '9') {
                    int n = ch - '0';
                    pos2hint.put(p, n);
                    state.pos2state.put(p, n == 0 ? LogicGamesHintState.Complete : LogicGamesHintState.Normal);
                }
            }
        }
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(SlitherLinkGameMove move, F2<SlitherLinkGameState, SlitherLinkGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        SlitherLinkGameState state = cloner.deepClone(state());
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

    public boolean switchObject(SlitherLinkGameMove move, SlitherLinkMarkerOptions markerOption) {
        return changeObject(move, (state, move2) -> state.switchObject(markerOption, move2));
    }

    public boolean setObject(SlitherLinkGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public SlitherLinkObject[] getObject(Position p) {
        return state().get(p);
    }

    public SlitherLinkObject[] getObject(int row, int col) {
        return state().get(row, col);
    }

    public LogicGamesHintState getHintState(Position p) {
        return state().pos2state.get(p);
    }

    public void undo() {
        if (!canUndo()) return;
        stateIndex--;
        levelUpdated(states.get(stateIndex + 1), state());
    }

    public void redo() {
        if (!canRedo()) return;
        stateIndex++;
        levelUpdated(states.get(stateIndex - 1), state());
    }
}
