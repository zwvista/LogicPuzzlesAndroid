package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domain;

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

public class MosaikGameState extends CellsGameState<MosaikGame, MosaikGameMove, MosaikGameState> {
    public MosaikObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public MosaikGameState(MosaikGame game) {
        super(game);
        objArray = new MosaikObject[rows() * cols()];
        Arrays.fill(objArray, MosaikObject.Empty);
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            pos2state.put(p, n == 0 ? HintState.Complete : HintState.Normal);
        }
    }

    public MosaikObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public MosaikObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, MosaikObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, MosaikObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            for (Position os : MosaikGame.offset) {
                Position p2 = p.add(os);
                if (!isValid(p2)) continue;
                if (get(p2) == MosaikObject.Filled) n1++;
            }
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
    }

    public boolean setObject(MosaikGameMove move) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(MosaikGameMove move, MarkerOptions markerOption) {
        F<MosaikObject, MosaikObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        MosaikObject.Marker : MosaikObject.Filled;
            case Filled:
                return markerOption == MarkerOptions.MarkerLast ?
                        MosaikObject.Marker : MosaikObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        MosaikObject.Filled : MosaikObject.Empty;
            }
            return obj;
        };
        MosaikObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }
}
