package com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain;

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

public class ParksGameState extends CellsGameState<ParksGame, ParksGameMove, ParksGameState> {
    public ParksObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public ParksGameState(ParksGame game) {
        super(game);
        objArray = new ParksObject[rows() * cols()];
        Arrays.fill(objArray, ParksObject.Empty);
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            pos2state.put(p, n == 0 ? HintState.Complete : HintState.Normal);
        }
    }

    public ParksObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public ParksObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, ParksObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, ParksObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            for (Position os : ParksGame.offset) {
                Position p2 = p.add(os);
                if (!isValid(p2)) continue;
                if (get(p2) == ParksObject.Filled) n1++;
            }
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
    }

    public boolean setObject(ParksGameMove move) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(MarkerOptions markerOption, ParksGameMove move) {
        F<ParksObject, ParksObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        ParksObject.Marker : ParksObject.Filled;
            case Filled:
                return markerOption == MarkerOptions.MarkerLast ?
                        ParksObject.Marker : ParksObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        ParksObject.Filled : ParksObject.Empty;
            }
            return obj;
        };
        ParksObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }
}
