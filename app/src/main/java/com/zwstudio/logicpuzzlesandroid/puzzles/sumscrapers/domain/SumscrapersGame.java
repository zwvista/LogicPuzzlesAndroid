package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.F2;

public class SumscrapersGame extends CellsGame<SumscrapersGame, SumscrapersGameMove, SumscrapersGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public boolean isValid(int row, int col) {
        return row >= 1 && col >= 1 && row < size.row - 1 && col < size.col - 1;
    }

    public int[] objArray;
    public int intMax() {return rows() - 2;}
    public int get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public int get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, int obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, int obj) {
        set(p.row, p.col, obj);
    }

    public SumscrapersGame(List<String> layout, GameInterface<SumscrapersGame, SumscrapersGameMove, SumscrapersGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length() / 2);
        objArray = new int[rows() * cols()];

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                String s = str.substring(c * 2, c * 2 + 2).trim();
                int n = s.equals("") ? 0 : Integer.parseInt(s);
                set(r, c, n);
            }
        }

        SumscrapersGameState state = new SumscrapersGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(SumscrapersGameMove move, F2<SumscrapersGameState, SumscrapersGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        SumscrapersGameState state = cloner.deepClone(state());
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

    public boolean switchObject(SumscrapersGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(SumscrapersGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public int getObject(Position p) {
        return state().get(p);
    }

    public int getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getState(int row, int col) {
        return state().getState(row, col);
    }
}
