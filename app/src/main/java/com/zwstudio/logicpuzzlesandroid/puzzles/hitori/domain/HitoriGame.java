package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.List;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class HitoriGame extends CellsGame<HitoriGame, HitoriGameMove, HitoriGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public char[] objArray;
    public char get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public char get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, char obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, char obj) {
        set(p.row, p.col, obj);
    }

    public HitoriGame(List<String> layout, GameInterface<HitoriGame, HitoriGameMove, HitoriGameState> gi) {
        super(gi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new char[rows() * cols()];
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                char ch = str.charAt(c);
                if (ch >= '0' && ch <= '9')
                    set(r, c, ch);
            }
        }
        HitoriGameState state = new HitoriGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(HitoriGameMove move, F2<HitoriGameState, HitoriGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        HitoriGameState state = cloner.deepClone(state());
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

    public boolean switchObject(HitoriGameMove move, MarkerOptions markerOption) {
        return changeObject(move, (state, move2) -> state.switchObject(markerOption, move2));
    }

    public boolean setObject(HitoriGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public HitoriObject getObject(Position p) {
        return state().get(p);
    }

    public HitoriObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public String getRowHint(int row) {
        return state().row2hint[row];
    }

    public String getColHint(int col) {
        return state().col2hint[col];
    }
}
