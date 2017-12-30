package com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.List;

import fj.F2;

public class SnakeGame extends CellsGame<SnakeGame, SnakeGameMove, SnakeGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public int[] row2hint;
    public int[] col2hint;
    public List<Position> pos2snake = new ArrayList<>();

    public SnakeGame(List<String> layout, GameInterface<SnakeGame, SnakeGameMove, SnakeGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size() - 1, layout.get(0).length() - 1);
        row2hint = new int[rows()];
        col2hint = new int[cols()];

        for (int r = 0; r < rows() + 1; r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols() + 1; c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch == 'S')
                    pos2snake.add(p);
                else if (ch >= '0' && ch <= '9') {
                    int n = ch - '0';
                    if (r == rows())
                        col2hint[c] = n;
                    else if (c == cols())
                        row2hint[r] = n;
                } else {
                    if (r == rows() && c == cols()) {
                        //
                    } else if (r == rows())
                        col2hint[c] = -1;
                    else if (c == cols())
                        row2hint[r] = -1;
                }
            }
        }

        SnakeGameState state = new SnakeGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(SnakeGameMove move, F2<SnakeGameState, SnakeGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        SnakeGameState state = cloner.deepClone(state());
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

    public boolean switchObject(SnakeGameMove move) {
        return changeObject(move, SnakeGameState::switchObject);
    }

    public boolean setObject(SnakeGameMove move) {
        return changeObject(move, SnakeGameState::setObject);
    }

    public SnakeObject getObject(Position p) {
        return state().get(p);
    }

    public SnakeObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getRowState(int row) {
        return state().row2state[row];
    }

    public HintState getColState(int col) {
        return state().col2state[col];
    }
}
