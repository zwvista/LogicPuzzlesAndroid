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
            NurikabeWallObject o = new NurikabeWallObject();
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
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                NurikabeObject o = get(r, c);
                if (o instanceof NurikabeEmptyObject && o.lightness == 0 ||
                        o instanceof NurikabeMarkerObject && o.lightness == 0)
                    isSolved = false;
                else if (o instanceof NurikabeLightbulbObject) {
                    NurikabeLightbulbObject o2 = (NurikabeLightbulbObject)o;
                    o2.state = o.lightness == 1 ? NurikabeLightbulbState.Normal : NurikabeLightbulbState.Error;
                    if (o.lightness > 1) isSolved = false;
                } else if (o instanceof NurikabeWallObject) {
                    NurikabeWallObject o2 = (NurikabeWallObject) o;
                    int n2 = game.pos2hint.get(p);
                    if (n2 < 0) continue;
                    int n1 = 0;
                    for (Position os : NurikabeGame.offset) {
                        Position p2 = p.add(os);
                        if (!isValid(p2)) continue;
                        if (get(p2) instanceof NurikabeLightbulbObject) n1++;
                    }
                    o2.state = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
                    if (n1 != n2) isSolved = false;
                }
            }
    }

    private boolean objChanged(NurikabeGameMove move, boolean toajust, boolean tolighten) {
        Position p = move.p;
        set(p, move.obj);
        if (toajust) {
            F<Integer, Integer> f = n -> tolighten ? n + 1 : n > 0 ? n - 1 : n;
            NurikabeObject obj = get(p);
            obj.lightness = f.f(obj.lightness);
            for (Position os : NurikabeGame.offset)
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    obj = get(p2);
                    if (obj instanceof NurikabeWallObject) break;
                    obj.lightness = f.f(obj.lightness);
                }
            updateIsSolved();
        }
        return true;
    }

    public boolean setObject(NurikabeGameMove move) {
        Position p = move.p;
        NurikabeObject objOld = get(p);
        NurikabeObject objNew = move.obj;
        objNew.lightness = objOld.lightness;
        if (objOld instanceof NurikabeEmptyObject && objNew instanceof NurikabeMarkerObject ||
                objOld instanceof NurikabeMarkerObject && objNew instanceof NurikabeEmptyObject)
            return objChanged(move, false, false);
        if (objOld instanceof NurikabeEmptyObject && objNew instanceof NurikabeLightbulbObject ||
                objOld instanceof NurikabeMarkerObject && objNew instanceof NurikabeLightbulbObject)
            return objChanged(move, true, true);
        if (objOld instanceof NurikabeLightbulbObject && objNew instanceof NurikabeEmptyObject ||
                objOld instanceof NurikabeLightbulbObject && objNew instanceof NurikabeMarkerObject)
            return objChanged(move, true, false);
        if (objNew instanceof NurikabeWallObject)
            set(p, objNew);
        return false;
    }

    public boolean switchObject(NurikabeMarkerOptions markerOption, boolean normalLightbulbsOnly, NurikabeGameMove move) {
        F<NurikabeObject, NurikabeObject> f = obj -> {
            if (obj instanceof NurikabeEmptyObject)
                return markerOption == NurikabeMarkerOptions.MarkerBeforeLightbulb ?
                        new NurikabeMarkerObject() : new NurikabeLightbulbObject();
            if (obj instanceof NurikabeLightbulbObject)
                return markerOption == NurikabeMarkerOptions.MarkerAfterLightbulb ?
                        new NurikabeMarkerObject() : new NurikabeEmptyObject();
            if (obj instanceof NurikabeMarkerObject)
                return markerOption == NurikabeMarkerOptions.MarkerBeforeLightbulb ?
                        new NurikabeLightbulbObject() : new NurikabeEmptyObject();
            return obj;
        };
        NurikabeObject objOld = get(move.p);
        NurikabeObject objNew = f.f(objOld);
        if (objNew instanceof NurikabeEmptyObject || objNew instanceof NurikabeMarkerObject) {
            move.obj = objNew;
            return setObject(move);
        }
        if (objNew instanceof NurikabeLightbulbObject) {
            move.obj = normalLightbulbsOnly && objOld.lightness > 0 ? f.f(objNew) : objNew;
            return setObject(move);
        }
        return false;
    }
}
