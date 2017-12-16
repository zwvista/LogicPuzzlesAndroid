package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class TapaGame extends CellsGame<TapaGame, TapaGameMove, TapaGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(-1, 1),
            new Position(0, 1),
            new Position(1, 1),
            new Position(1, 0),
            new Position(1, -1),
            new Position(0, -1),
            new Position(-1, -1),
    };
    public static Position offset2[] = {
            new Position(0, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(1, 1),
    };

    public Map<Position, List<Integer>> pos2hint = new HashMap<>();

    public TapaGame(List<String> layout, GameInterface<TapaGame, TapaGameMove, TapaGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length() / 4);
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                String s = str.substring(c * 4, c * 4 + 4).trim();
                if (s.isEmpty()) continue;
                List<Integer> hint = new ArrayList<>();
                for (char ch : s.toCharArray()) {
                    if (ch == '?' || ch >= '0' && ch <= '9') {
                        int n = ch == '?' ? -1 : (ch - '0');
                        hint.add(n);
                    }
                }
                pos2hint.put(p, hint);
            }
        }
        TapaGameState state = new TapaGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(TapaGameMove move, F2<TapaGameState, TapaGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        TapaGameState state = cloner.deepClone(state());
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

    public boolean switchObject(TapaGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(TapaGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public TapaObject getObject(Position p) {
        return state().get(p);
    }

    public TapaObject getObject(int row, int col) {
        return state().get(row, col);
    }
}
