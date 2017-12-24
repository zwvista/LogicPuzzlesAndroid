package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.List;

import fj.F2;

public class MagnetsGame extends CellsGame<MagnetsGame, MagnetsGameMove, MagnetsGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public int[] row2hint;
    public int[] col2hint;
    public List<MagnetsArea> areas = new ArrayList<>();
    public List<Position> singles = new ArrayList<>();

    public MagnetsGame(List<String> layout, GameInterface<MagnetsGame, MagnetsGameMove, MagnetsGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size() - 2, layout.get(0).length() - 2);
        row2hint = new int[rows() * 2];
        col2hint = new int[cols() * 2];

        for (int r = 0; r < rows() + 2; r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols() + 2; c++) {
                Position p2 = new Position(r, c);
                char ch = str.charAt(c);
                if (ch == '.') {
                    areas.add(new MagnetsArea() {{
                        p = p2; type = MagnetsAreaType.Single;
                    }});
                    singles.add(p2);
                } else if (ch == 'H')
                    areas.add(new MagnetsArea() {{
                        p = p2; type = MagnetsAreaType.Horizontal;
                    }});
                else if (ch == 'V')
                    areas.add(new MagnetsArea() {{
                        p = p2; type = MagnetsAreaType.Vertical;
                    }});
                else if (ch >= '0' && ch <= '9') {
                    int n = ch - '0';
                    if (r >= rows())
                        col2hint[c * 2 + r - rows()] = n;
                    else if (c >= cols())
                        row2hint[r * 2 + c - cols()] = n;
                }
            }
        }

        MagnetsGameState state = new MagnetsGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(MagnetsGameMove move, F2<MagnetsGameState, MagnetsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        MagnetsGameState state = cloner.deepClone(state());
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

    public boolean switchObject(MagnetsGameMove move) {
        return changeObject(move, MagnetsGameState::switchObject);
    }

    public boolean setObject(MagnetsGameMove move) {
        return changeObject(move, MagnetsGameState::setObject);
    }

    public MagnetsObject getObject(Position p) {
        return state().get(p);
    }

    public MagnetsObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getRowState(int id) {
        return state().row2state[id];
    }

    public HintState getColState(int id) {
        return state().col2state[id];
    }
}
