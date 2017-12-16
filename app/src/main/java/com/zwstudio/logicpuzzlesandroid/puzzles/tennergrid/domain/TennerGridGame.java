package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.F2;

public class TennerGridGame extends CellsGame<TennerGridGame, TennerGridGameMove, TennerGridGameState> {
    public static Position offset[] = {
            new Position(1, -1),
            new Position(1, 0),
            new Position(1, 1),
    };

    public int[] objArray;
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

    public TennerGridGame(List<String> layout, GameInterface<TennerGridGame, TennerGridGameMove, TennerGridGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length() / 2);
        objArray = new int[rows() * cols()];

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                String s = str.substring(c * 2, c * 2 + 2);
                int n = s.equals("  ") ? -1 : Integer.parseInt(s.trim());
                set(r, c, n);
            }
        }

        TennerGridGameState state = new TennerGridGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(TennerGridGameMove move, F2<TennerGridGameState, TennerGridGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        TennerGridGameState state = cloner.deepClone(state());
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

    public boolean switchObject(TennerGridGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(TennerGridGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public int getObject(Position p) {
        return state().get(p);
    }

    public int getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getPosState(Position p) {
        return state().pos2state.get(p);
    }
}
