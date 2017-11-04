package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class KakuroGame extends CellsGame<KakuroGame, KakuroGameMove, KakuroGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, Integer> pos2horzHint = new HashMap<>();
    public Map<Position, Integer> pos2vertHint = new HashMap<>();
    public Map<Position, Integer> pos2num = new HashMap<>();

    public KakuroGame(List<String> layout, GameInterface<KakuroGame, KakuroGameMove, KakuroGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length() / 4);

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                String s  = str.substring(c * 4, c * 4 + 4);
                if (s.equals("    "))
                    pos2num.put(p, 0);
                else {
                    String s1 = s.substring(0, 2), s2 = s.substring(2, 4);
                    if (!s1.equals("00"))
                        pos2vertHint.put(p, Integer.parseInt(s1));
                    if (!s2.equals("00"))
                        pos2horzHint.put(p, Integer.parseInt(s2));
                }
            }
        }

        KakuroGameState state = new KakuroGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(KakuroGameMove move, F2<KakuroGameState, KakuroGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        KakuroGameState state = cloner.deepClone(state());
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

    public boolean switchObject(KakuroGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(KakuroGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public Integer getObject(Position p) {
        return state().get(p);
    }

    public HintState getHorzState(Position p) {
        return state().pos2horzHint.get(p);
    }

    public HintState getVertState(Position p) {
        return state().pos2vertHint.get(p);
    }
}
