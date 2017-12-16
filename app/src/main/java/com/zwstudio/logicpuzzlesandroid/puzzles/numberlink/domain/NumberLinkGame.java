package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class NumberLinkGame extends CellsGame<NumberLinkGame, NumberLinkGameMove, NumberLinkGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, Integer> pos2hint = new HashMap<>();
    public Map<Integer, List<Position>> pos2rng = new HashMap<>();

    public NumberLinkGame(List<String> layout, GameInterface<NumberLinkGame, NumberLinkGameMove, NumberLinkGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch == ' ') continue;
                int n = Character.isDigit(ch) ? ch - '0' : ch - 'A' + 10;
                pos2hint.put(p, n);
                List<Position> rng = pos2rng.get(n);
                if (rng == null) rng = new ArrayList<>();
                rng.add(p);
                pos2rng.put(n, rng);
            }
        }
        NumberLinkGameState state = new NumberLinkGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(NumberLinkGameMove move, F2<NumberLinkGameState, NumberLinkGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        NumberLinkGameState state = cloner.deepClone(state());
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

    public boolean setObject(NumberLinkGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public Boolean[] getObject(Position p) {
        return state().get(p);
    }

    public Boolean[] getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState pos2State(Position p) {
        return state().pos2state.get(p);
    }
}
