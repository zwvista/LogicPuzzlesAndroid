package com.zwstudio.logicpuzzlesandroid.common.domain;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TCC-2-9002 on 2016/10/27.
 */

public class Game<G extends Game<G, GM, GS>, GM, GS extends GameState> {
    protected Cloner cloner = new Cloner();
    protected int stateIndex = 0;
    protected List<GS> states = new ArrayList<>();
    protected GS state() {return states.get(stateIndex);}
    protected List<GM> moves = new ArrayList<>();
    protected GM move() {return moves.get(stateIndex - 1);}
    public boolean isSolved() {return state().isSolved;}
    public boolean canUndo() {return stateIndex > 0;}
    public boolean canRedo() {return stateIndex < states.size() - 1;}
    public int moveIndex() {return stateIndex;}
    public int moveCount() {return states.size() - 1;}

    private GameInterface<G, GM, GS> gi;

    public Game(GameInterface<G, GM, GS> gi) {
        cloner.dontClone(this.getClass());
        this.gi = gi;
    }

    protected void moveAdded(GM move) {
        if (gi == null) return;
        gi.moveAdded((G)this, move);
    }

    protected void levelInitilized(GS state) {
        if (gi == null) return;
        gi.levelInitilized((G)this, state);
        if (isSolved()) gi.gameSolved((G)this);
    }

    protected void levelUpdated(GS stateFrom, GS stateTo) {
        if (gi == null) return;
        gi.levelUpdated((G)this, stateFrom, stateTo);
        if (isSolved()) gi.gameSolved((G)this);
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
