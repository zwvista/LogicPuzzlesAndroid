package com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.List;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class Square100Game extends CellsGame<Square100Game, Square100GameMove, Square100GameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public String[] objArray;
    public String get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public String get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, String obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, String obj) {
        set(p.row, p.col, obj);
    }

    public Square100Game(List<String> layout, GameInterface<Square100Game, Square100GameMove, Square100GameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new String[rows() * cols()];

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                String s = " " + str.charAt(c) + " ";
                set(r, c, s);
            }
        }

        Square100GameState state = new Square100GameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(Square100GameMove move, F2<Square100GameState, Square100GameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        Square100GameState state = cloner.deepClone(state());
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

    public boolean switchObject(Square100GameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(Square100GameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public String getObject(Position p) {
        return state().get(p);
    }

    public String getObject(int row, int col) {
        return state().get(row, col);
    }

    public int getRowHint(int row) {
        return state().row2hint[row];
    }

    public int getColHint(int col) {
        return state().col2hint[col];
    }
}
