package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SentinelsGameState extends CellsGameState<SentinelsGame, SentinelsGameMove, SentinelsGameState> {
    public SentinelsObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public SentinelsGameState(SentinelsGame game) {
        super(game);
        objArray = new SentinelsObject[rows() * cols()];
        Arrays.fill(objArray, SentinelsObject.Empty);
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            pos2state.put(p, n == 0 ? HintState.Complete : HintState.Normal);
        }
    }

    public SentinelsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public SentinelsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, SentinelsObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, SentinelsObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            for (Position os : SentinelsGame.offset) {
                Position p2 = p.add(os);
                if (!isValid(p2)) continue;
                if (get(p2) == SentinelsObject.Filled) n1++;
            }
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
    }

    public boolean setObject(SentinelsGameMove move) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(SentinelsGameMove move, MarkerOptions markerOption) {
        F<SentinelsObject, SentinelsObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        SentinelsObject.Marker : SentinelsObject.Filled;
            case Filled:
                return markerOption == MarkerOptions.MarkerLast ?
                        SentinelsObject.Marker : SentinelsObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        SentinelsObject.Filled : SentinelsObject.Empty;
            }
            return obj;
        };
        SentinelsObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }
}
