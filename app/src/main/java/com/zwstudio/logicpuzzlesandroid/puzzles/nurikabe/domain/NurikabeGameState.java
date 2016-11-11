package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.Arrays;
import java.util.Map;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class NurikabeGameState extends CellsGameState<NurikabeGame, NurikabeGameMove, NurikabeGameState> {
    public NurikabeObject[] objArray;

    public NurikabeGameState(NurikabeGame game) {
        super(game);
        objArray = new NurikabeObject[rows() * cols()];
        Arrays.fill(objArray, new NurikabeEmptyObject());
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            NurikabeHintObject o = new NurikabeHintObject();
            o.state = n <= 0 ? HintState.Complete : HintState.Normal;
            set(p, o);
        }
    }

    public NurikabeObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public NurikabeObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, NurikabeObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, NurikabeObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        isSolved = false;
    }

    public boolean setObject(NurikabeGameMove move) {
        Position p = move.p;
        NurikabeObject objOld = get(p);
        NurikabeObject objNew = move.obj;
        if (objOld instanceof NurikabeHintObject || objOld.toString().equals(objNew.toString())) return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(NurikabeMarkerOptions markerOption, NurikabeGameMove move) {
        F<NurikabeObject, NurikabeObject> f = obj -> {
            if (obj instanceof NurikabeEmptyObject)
                return markerOption == NurikabeMarkerOptions.MarkerBeforeWall ?
                        new NurikabeMarkerObject() : new NurikabeWallObject();
            if (obj instanceof NurikabeWallObject)
                return markerOption == NurikabeMarkerOptions.MarkerAfterWall ?
                        new NurikabeMarkerObject() : new NurikabeEmptyObject();
            if (obj instanceof NurikabeMarkerObject)
                return markerOption == NurikabeMarkerOptions.MarkerBeforeWall ?
                        new NurikabeWallObject() : new NurikabeEmptyObject();
            return obj;
        };
        move.obj = f.f(get(move.p));
        return setObject(move);
    }
}
