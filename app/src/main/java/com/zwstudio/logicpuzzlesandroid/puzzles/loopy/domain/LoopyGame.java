package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.Arrays;
import java.util.List;

import fj.F2;

public class LoopyGame extends CellsGame<LoopyGame, LoopyGameMove, LoopyGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public GridLineObject[][] objArray;

    public GridLineObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public GridLineObject[] get(Position p) {
        return get(p.row, p.col);
    }

    public LoopyGame(List<String> layout, GameInterface<LoopyGame, LoopyGameMove, LoopyGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size() / 2 + 1, layout.get(0).length() / 2 + 1);
        objArray = new GridLineObject[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new GridLineObject[4];
            Arrays.fill(objArray[i], GridLineObject.Empty);
        }
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(2 * r);
            for (int c = 0; c < cols() - 1; c++) {
                char ch = str.charAt(2 * c + 1);
                if (ch == '-') get(r, c)[1] = get(r, c + 1)[3] = GridLineObject.Line;
            }
            if (r == rows() - 1) break;
            str = layout.get(2 * r + 1);
            for (int c = 0; c < cols(); c++) {
                char ch = str.charAt(2 * c);
                if (ch == '|') get(r, c)[2] = get(r + 1, c)[0] = GridLineObject.Line;
            }
        }
        LoopyGameState state = new LoopyGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(LoopyGameMove move, F2<LoopyGameState, LoopyGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        LoopyGameState state = cloner.deepClone(state());
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

    public boolean switchObject(LoopyGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(LoopyGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public GridLineObject[] getObject(Position p) {
        return state().get(p);
    }

    public GridLineObject[] getObject(int row, int col) {
        return state().get(row, col);
    }
}
