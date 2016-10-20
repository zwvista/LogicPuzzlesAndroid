package com.zwstudio.logicgamesandroid.lightup.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightUpGameState implements Cloneable {
    public LightUpGame game;
    public LightUpObject[] objArray;
    public boolean isSolved;

    public Position size() {return game.size;}
    public int rows() {return game.rows();}
    public int cols() {return game.cols();}
    public boolean isValid(int row, int col) { return game.isValid(row, col); }
    public boolean isValid(Position p) {
        return game.isValid(p);
    }

    public LightUpGameState(LightUpGame game) {
        this.game = game;
        objArray = new LightUpObject[rows() * cols()];
        for (LightUpObject obj: objArray)
            obj = new LightUpEmptyObject();
    }

    @Override
    public LightUpGameState clone(){
        try {
            LightUpGameState o = (LightUpGameState)super.clone();
            o.game = game;
            o.objArray = new LightUpObject[objArray.length];
            for (int i = 0; i < objArray.length; i++)
                o.objArray[i] = objArray[i].clone();
            o.isSolved = isSolved;
            return o;
        } catch(CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    public LightUpObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public LightUpObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, LightUpObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, LightUpObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                LightUpObject o = get(r, c);
                if (o instanceof LightUpEmptyObject && o.lightness == 0 ||
                        o instanceof LightUpMarkerObject && o.lightness == 0)
                    isSolved = false;
                else if (o instanceof LightUpLightbulbObject) {
                    LightUpLightbulbObject o2 = (LightUpLightbulbObject)o;
                    o2.state = o.lightness == 1 ? LightUpLightbulbState.Normal : LightUpLightbulbState.Error;
                    if (o.lightness > 1) isSolved = false;
                } else if (o instanceof LightUpWallObject) {
                    LightUpWallObject o2 = (LightUpWallObject) o;
                    int n2 = game.pos2hint.get(p);
                    if (n2 < 0) continue;
                    int n1 = 0;
                    for (Position os : LightUpGame.offset) {
                        Position p2 = p.add(os);
                        if (!isValid(p2)) continue;
                        if (get(p2) instanceof LightUpLightbulbObject) n1++;
                    }
                    o2.state = n1 < n2 ? LogicGamesHintState.Normal : n1 == n2 ? LogicGamesHintState.Complete : LogicGamesHintState.Error;
                    if (n1 != n2) isSolved = false;
                }
            }
    }

    private boolean objChanged(LightUpGameMove move, boolean toajust, boolean tolighten) {
        Position p = move.p;
        set(p, move.obj);
        if (toajust) {
            F<Integer, Integer> f = n -> tolighten ? n + 1 : n > 0 ? n - 1 : n;
            LightUpObject obj = get(p);
            obj.lightness = f.f(obj.lightness);
            for (Position os : LightUpGame.offset)
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    obj = get(p2);
                    if (obj instanceof LightUpWallObject) break;
                    obj.lightness = f.f(obj.lightness);
                }
            updateIsSolved();
        }
        return true;
    }

    public boolean setObject(LightUpGameMove move) {
        Position p = move.p;
        LightUpObject objOld = get(p);
        LightUpObject objNew = move.obj;
        objNew.lightness = objOld.lightness;
        if (objOld instanceof LightUpEmptyObject && objNew instanceof LightUpMarkerObject ||
                objOld instanceof LightUpMarkerObject && objNew instanceof LightUpEmptyObject)
            return objChanged(move, false, false);
        if (objOld instanceof LightUpEmptyObject && objNew instanceof LightUpLightbulbObject ||
                objOld instanceof LightUpMarkerObject && objNew instanceof LightUpLightbulbObject)
            return objChanged(move, true, true);
        if (objOld instanceof LightUpLightbulbObject && objNew instanceof LightUpEmptyObject ||
                objOld instanceof LightUpLightbulbObject && objNew instanceof LightUpMarkerObject)
            return objChanged(move, true, false);
        if (objNew instanceof LightUpWallObject)
            set(p, objNew);
        return false;
    }

    public boolean switchObject(LightUpMarkerOptions markerOption, boolean normalLightbulbsOnly, LightUpGameMove move) {
        F<LightUpObject, LightUpObject> f = obj -> {
            if (obj instanceof LightUpEmptyObject)
                return markerOption == LightUpMarkerOptions.MarkerBeforeLightbulb ?
                        new LightUpMarkerObject() : new LightUpLightbulbObject();
            if (obj instanceof LightUpLightbulbObject)
                return markerOption == LightUpMarkerOptions.MarkerAfterLightbulb ?
                        new LightUpMarkerObject() : new LightUpEmptyObject();
            if (obj instanceof LightUpMarkerObject)
                return markerOption == LightUpMarkerOptions.MarkerBeforeLightbulb ?
                        new LightUpLightbulbObject() : new LightUpEmptyObject();
            return obj;
        };
        LightUpObject objOld = get(move.p);
        LightUpObject objNew = f.f(objOld);
        if (objNew instanceof LightUpEmptyObject || objNew instanceof LightUpMarkerObject) {
            move.obj = objNew;
            return setObject(move);
        }
        if (objNew instanceof LightUpLightbulbObject) {
            move.obj = normalLightbulbsOnly && objOld.lightness > 0 ? f.f(objNew) : objNew;
            return setObject(move);
        }
        return false;
    }
}
