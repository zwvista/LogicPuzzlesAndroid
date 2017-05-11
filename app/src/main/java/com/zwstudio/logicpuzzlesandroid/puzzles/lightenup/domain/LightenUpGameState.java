package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Map;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightenUpGameState extends CellsGameState<LightenUpGame, LightenUpGameMove, LightenUpGameState> {
    public LightenUpObject[] objArray;

    public LightenUpGameState(LightenUpGame game) {
        super(game);
        objArray = new LightenUpObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new LightenUpEmptyObject();
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            LightenUpWallObject o = new LightenUpWallObject();
            o.state = n <= 0 ? HintState.Complete : HintState.Normal;
            set(p, o);
        }
    }

    public LightenUpObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public LightenUpObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, LightenUpObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, LightenUpObject obj) {
        set(p.row, p.col, obj);
    }

    private boolean objChanged(LightenUpGameMove move, boolean toajust, boolean tolighten) {
        Position p = move.p;
        set(p, move.obj);
        if (toajust) {
            F<Integer, Integer> f = n -> tolighten ? n + 1 : n > 0 ? n - 1 : n;
            LightenUpObject obj = get(p);
            obj.lightness = f.f(obj.lightness);
            for (Position os : LightenUpGame.offset)
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    obj = get(p2);
                    if (obj instanceof LightenUpWallObject) break;
                    obj.lightness = f.f(obj.lightness);
                }
            updateIsSolved();
        }
        return true;
    }

    public boolean setObject(LightenUpGameMove move) {
        Position p = move.p;
        LightenUpObject objOld = get(p);
        LightenUpObject objNew = move.obj;
        objNew.lightness = objOld.lightness;
        if (objOld instanceof LightenUpEmptyObject && objNew instanceof LightenUpMarkerObject ||
                objOld instanceof LightenUpMarkerObject && objNew instanceof LightenUpEmptyObject)
            return objChanged(move, false, false);
        if (objOld instanceof LightenUpEmptyObject && objNew instanceof LightenUpLightbulbObject ||
                objOld instanceof LightenUpMarkerObject && objNew instanceof LightenUpLightbulbObject)
            return objChanged(move, true, true);
        if (objOld instanceof LightenUpLightbulbObject && objNew instanceof LightenUpEmptyObject ||
                objOld instanceof LightenUpLightbulbObject && objNew instanceof LightenUpMarkerObject)
            return objChanged(move, true, false);
        if (objNew instanceof LightenUpWallObject)
            set(p, objNew);
        return false;
    }

    public boolean switchObject(LightenUpGameMove move, MarkerOptions markerOption, boolean allowedObjectsOnly) {
        F<LightenUpObject, LightenUpObject> f = obj -> {
            if (obj instanceof LightenUpEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new LightenUpMarkerObject() : new LightenUpLightbulbObject();
            if (obj instanceof LightenUpLightbulbObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new LightenUpMarkerObject() : new LightenUpEmptyObject();
            if (obj instanceof LightenUpMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new LightenUpLightbulbObject() : new LightenUpEmptyObject();
            return obj;
        };
        LightenUpObject objOld = get(move.p);
        LightenUpObject objNew = f.f(objOld);
        if (objNew instanceof LightenUpEmptyObject || objNew instanceof LightenUpMarkerObject) {
            move.obj = objNew;
            return setObject(move);
        }
        if (objNew instanceof LightenUpLightbulbObject) {
            move.obj = allowedObjectsOnly && objOld.lightness > 0 ? f.f(objNew) : objNew;
            return setObject(move);
        }
        return false;
    }

    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                LightenUpObject o = get(r, c);
                if (o instanceof LightenUpEmptyObject && o.lightness == 0 ||
                        o instanceof LightenUpMarkerObject && o.lightness == 0)
                    isSolved = false;
                else if (o instanceof LightenUpLightbulbObject) {
                    LightenUpLightbulbObject o2 = (LightenUpLightbulbObject)o;
                    o2.state = o.lightness == 1 ? AllowedObjectState.Normal : AllowedObjectState.Error;
                    if (o.lightness > 1) isSolved = false;
                } else if (o instanceof LightenUpWallObject) {
                    LightenUpWallObject o2 = (LightenUpWallObject) o;
                    int n2 = game.pos2hint.get(p);
                    if (n2 < 0) continue;
                    int n1 = 0;
                    for (Position os : LightenUpGame.offset) {
                        Position p2 = p.add(os);
                        if (!isValid(p2)) continue;
                        if (get(p2) instanceof LightenUpLightbulbObject) n1++;
                    }
                    o2.state = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
                    if (n1 != n2) isSolved = false;
                }
            }
    }
}
