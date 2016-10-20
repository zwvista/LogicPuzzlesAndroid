package com.zwstudio.logicgamesandroid.lightup.domain;

import com.rits.cloning.Cloner;
import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightUpGame {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Position size;
    public int rows() {return size.row;}
    public int cols() {return size.col;}
    public boolean isValid(int row, int col) {
        return row >= 0 && col >= 0 && row < size.row && col < size.col;
    }
    public boolean isValid(Position p) {
        return isValid(p.row, p.col);
    }

    public Map<Position, Integer> pos2hint = new HashMap<>();

    private int stateIndex = 0;
    private List<LightUpGameState> states = new ArrayList<>();
    private LightUpGameState state() {return states.get(stateIndex);}
    private List<LightUpGameMove> moves = new ArrayList<>();
    private LightUpGameMove move() {return moves.get(stateIndex - 1);}
    public LightUpGameInterface gi;

    public boolean isSolved() {return state().isSolved;}
    public boolean canUndo() {return stateIndex > 0;}
    public boolean canRedo() {return stateIndex < states.size() - 1;}
    public int moveIndex() {return stateIndex;}
    public int moveCount() {return states.size() - 1;}

    private void moveAdded(LightUpGameMove move) {
        if (gi == null) return;
        gi.moveAdded(this, move);
    }

    private void levelInitilized(LightUpGameState state) {
        if (gi == null) return;
        gi.levelInitilized(this, state);
        if (isSolved()) gi.gameSolved(this);
    }

    private void levelUpdated(LightUpGameState stateFrom, LightUpGameState stateTo) {
        if (gi == null) return;
        gi.levelUpdated(this, stateFrom, stateTo);
        if (isSolved()) gi.gameSolved(this);
    }

    public LightUpGame(List<String> layout, LightUpGameInterface gi) {
        this.gi = gi;
        size = new Position(layout.size(), layout.get(0).length());
        LightUpGameState state = new LightUpGameState(this);
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch == 'W' || ch >= '0' && ch <= '9') {
                    int n = ch == 'W' ? -1 : (ch - '0');
                    pos2hint.put(p, n);
                    LightUpWallObject o = new LightUpWallObject();
                    o.state = n <= 0 ? LogicGamesHintState.Complete : LogicGamesHintState.Normal;
                    state.set(r, c, o);
                } else
                    state.set(r, c, new LightUpEmptyObject());
            }
        }
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(LightUpGameMove move, F2<LightUpGameState, LightUpGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        LightUpGameState state = new Cloner().deepClone(state());
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
