package com.zwstudio.logicgamesandroid.lightup.domain;

import com.zwstudio.logicgamesandroid.common.Position;

import java.util.ArrayList;
import java.util.List;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightUpGame {
    public enum MarkerOptions {
        NoMarker, MarkerAfterLightbulb, MarkerBeforeLightbulb
    }
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    private int stateIndex = 0;
    private List<LightUpGameState> states = new ArrayList<>();
    private LightUpGameState state() {return states.get(stateIndex);}
    private List<LightUpGameMove> moves = new ArrayList<>();
    private LightUpGameMove move() {return moves.get(stateIndex - 1);}
    public LightUpGameInterface gi;
    public Position size() {return state().size;}
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
        LightUpGameState state = new LightUpGameState(layout.size(), layout.get(0).length());
        for (int r = 0; r < state.size.row; r++) {
            String str = layout.get(r);
            for (int c = 0; c < state.size.col; c++) {
                char ch = str.charAt(c);
                if (ch == 'W' || ch >= '0' && ch <= '9') {
                    LightUpWallObject o = new LightUpWallObject();
                    o.lightbulbs = ch == 'W' ? -1 : (ch - '0');
                    o.state = o.lightbulbs <= 0 ? LightUpWallObject.WallState.Complete : LightUpWallObject.WallState.Normal;
                    state.set(r, c, o);
                } else
                    state.set(r, c, new LightUpEmptyObject());
            }
        }
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(Position p, F2<LightUpGameState, LightUpGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        LightUpGameState state = state().clone();
        LightUpGameMove move = new LightUpGameMove();
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

    public boolean switchObject(Position p, MarkerOptions markerOption, boolean normalLightbulbsOnly) {
        return changeObject(p, (state, move) -> state.switchObject(p, markerOption, normalLightbulbsOnly, move));
    }

    public boolean setObject(Position p, LightUpObject objNew) {
        return changeObject(p, (state, move) -> state.setObject(p, objNew, move));
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
