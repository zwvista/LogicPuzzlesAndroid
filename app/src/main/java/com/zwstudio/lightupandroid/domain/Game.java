package com.zwstudio.lightupandroid.domain;

import java.util.ArrayList;
import java.util.List;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class Game {
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
    private List<GameState> states = new ArrayList<>();
    private GameState state() {return states.get(stateIndex);}
    private List<GameMove> moves = new ArrayList<>();
    private GameMove move() {return moves.get(stateIndex - 1);}
    public GameInterface gi;
    public Position size() {return state().size;}
    public boolean isSolved() {return state().isSolved;}
    public boolean canUndo() {return stateIndex > 0;}
    public boolean canRedo() {return stateIndex < states.size() - 1;}
    public int moveIndex() {return stateIndex;}
    public int moveCount() {return states.size() - 1;}

    private void moveAdded(GameMove move) {
        if (gi == null) return;
        gi.moveAdded(this, move);
    }

    private void levelInitilized(GameState state) {
        if (gi == null) return;
        gi.levelInitilized(this, state);
        if (isSolved()) gi.gameSolved(this);
    }

    private void levelUpdated(GameState stateFrom, GameState stateTo) {
        if (gi == null) return;
        gi.levelUpdated(this, stateFrom, stateTo);
        if (isSolved()) gi.gameSolved(this);
    }

    public Game(List<String> layout, GameInterface gi) {
        this.gi = gi;
        GameState state = new GameState(layout.size(), layout.get(0).length());
        for (int r = 0; r < state.size.row; r++) {
            String str = layout.get(r);
            for (int c = 0; c < state.size.col; c++) {
                char ch = str.charAt(c);
                if (ch == 'W' || ch >= '0' && ch <= '9') {
                    WallObject o = new WallObject();
                    o.lightbulbs = ch == 'W' ? -1 : (ch - '0');
                    o.state = o.lightbulbs <= 0 ? WallObject.WallState.Complete : WallObject.WallState.Normal;
                    state.set(r, c, o);
                } else
                    state.set(r, c, new EmptyObject());
            }
        }
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(Position p, F2<GameState, GameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        GameState state = state().clone();
        GameMove move = new GameMove();
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

    public boolean setObject(Position p, GameObject objNew) {
        return changeObject(p, (state, move) -> state.setObject(p, objNew, move));
    }

    public GameObject getObject(Position p) {
        return state().get(p);
    }

    public GameObject getObject(int row, int col) {
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
