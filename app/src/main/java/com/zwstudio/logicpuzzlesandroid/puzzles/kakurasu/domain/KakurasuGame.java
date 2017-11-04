package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class KakurasuGame extends CellsGame<KakurasuGame, KakurasuGameMove, KakurasuGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public boolean isValid(int row, int col) {
        return row >= 1 && col >= 1 && row < size.row - 1 && col < size.col - 1;
    }

    public int[] row2hint;
    public int[] col2hint;

    public KakurasuGame(List<String> layout, GameInterface<KakurasuGame, KakurasuGameMove, KakurasuGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length() / 2);
        row2hint = new int[rows() * 2];
        col2hint = new int[cols() * 2];

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                String s = str.substring(c * 2, c * 2 + 2);
                if (s.equals("  ")) continue;
                int n = Integer.parseInt(s.trim());
                if (r == 0 || r == rows() - 1)
                    col2hint[c * 2 + (r == 0 ? 0 : 1)] = n;
                else if (c == 0 || c == cols() - 1)
                    row2hint[r * 2 + (c == 0 ? 0 : 1)] = n;
            }
        }

        KakurasuGameState state = new KakurasuGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(KakurasuGameMove move, F2<KakurasuGameState, KakurasuGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        KakurasuGameState state = cloner.deepClone(state());
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

    public boolean switchObject(KakurasuGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(KakurasuGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public KakurasuObject getObject(Position p) {
        return state().get(p);
    }

    public KakurasuObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getRowState(int row) {
        return state().row2state[row];
    }

    public HintState getColState(int col) {
        return state().col2state[col];
    }
}
