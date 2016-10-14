package com.zwstudio.logicgamesandroid.lightup.domain;

import com.zwstudio.logicgamesandroid.common.Position;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class GameState implements Cloneable {
    public Position size;
    public GameObject[] objArray;
    public boolean isSolved;

    public GameState(int rows, int cols) {
        size = new Position(rows, cols);
        objArray = new GameObject[rows * cols];
        for (GameObject obj: objArray)
            obj = new EmptyObject();
    }

    @Override
    public GameState clone(){
        try {
            GameState o = (GameState)super.clone();
            o.size = size;
            o.objArray = new GameObject[objArray.length];
            for (int i = 0; i < objArray.length; i++)
                o.objArray[i] = objArray[i].clone();
            o.isSolved = isSolved;
            return o;
        } catch(CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    public GameObject get(int row, int col) {
        return objArray[row * size.col + col];
    }
    public GameObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, GameObject obj) {
        objArray[row * size.col + col] = obj;
    }
    public void set(Position p, GameObject obj) {
        set(p.row, p.col, obj);
    }
    public boolean isValid(int row, int col) {
        return row >= 0 && col >= 0 && row < size.row && col < size.col;
    }
    public boolean isValid(Position p) {
        return isValid(p.row, p.col);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < size.row; r++)
            for (int c = 0; c < size.col; c++) {
                Position p = new Position(r, c);
                GameObject o = get(r, c);
                if (o instanceof EmptyObject && o.lightness == 0 ||
                        o instanceof MarkerObject && o.lightness == 0)
                    isSolved = false;
                else if (o instanceof LightbulbObject) {
                    LightbulbObject o2 = (LightbulbObject)o;
                    o2.state = o.lightness == 1 ? LightbulbObject.LightbulbState.Normal : LightbulbObject.LightbulbState.Error;
                    if (o.lightness > 1) isSolved = false;
                } else if (o instanceof WallObject) {
                    WallObject o2 = (WallObject)o;
                    if (o2.lightbulbs < 0) continue;
                    int n = 0;
                    for (Position os : Game.offset) {
                        Position p2 = p.add(os);
                        if (!isValid(p2)) continue;
                        if (get(p2) instanceof LightbulbObject) n++;
                    }
                    o2.state = n < o2.lightbulbs ? WallObject.WallState.Normal :
                            n == o2.lightbulbs ? WallObject.WallState.Complete :
                            WallObject.WallState.Error;
                    if (n != o2.lightbulbs) isSolved = false;
                }
            }
    }

    private boolean objChanged(Position p, GameObject objNew, GameMove move, boolean toajust, boolean tolighten) {
        move.p = p;
        move.obj = objNew;
        set(p, objNew);
        if (toajust) {
            F<Integer, Integer> f = n -> tolighten ? n + 1 : n > 0 ? n - 1 : n;
            GameObject obj = get(p);
            obj.lightness = f.f(obj.lightness);
            for (Position os : Game.offset)
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    obj = get(p2);
                    if (obj instanceof WallObject) break;
                    obj.lightness = f.f(obj.lightness);
                }
            updateIsSolved();
        }
        return true;
    }

    public boolean setObject(Position p, GameObject objNew, GameMove move) {
        GameObject objOld = get(p);
        objNew.lightness = objOld.lightness;
        if (objOld instanceof EmptyObject && objNew instanceof MarkerObject ||
                objOld instanceof MarkerObject && objNew instanceof EmptyObject)
            return objChanged(p, objNew, move, false, false);
        if (objOld instanceof EmptyObject && objNew instanceof LightbulbObject ||
                objOld instanceof MarkerObject && objNew instanceof LightbulbObject)
            return objChanged(p, objNew, move, true, true);
        if (objOld instanceof LightbulbObject && objNew instanceof EmptyObject ||
                objOld instanceof LightbulbObject && objNew instanceof MarkerObject)
            return objChanged(p, objNew, move, true, false);
        if (objNew instanceof WallObject)
            set(p, objNew);
        return false;
    }

    public boolean switchObject(Position p, Game.MarkerOptions markerOption, boolean normalLightbulbsOnly, GameMove move) {
        F<GameObject, GameObject> f = obj -> {
            if (obj instanceof EmptyObject)
                return markerOption == Game.MarkerOptions.MarkerBeforeLightbulb ?
                        new MarkerObject() : new LightbulbObject();
            if (obj instanceof LightbulbObject)
                return markerOption == Game.MarkerOptions.MarkerAfterLightbulb ?
                        new MarkerObject() : new EmptyObject();
            if (obj instanceof MarkerObject)
                return markerOption == Game.MarkerOptions.MarkerBeforeLightbulb ?
                        new LightbulbObject() : new EmptyObject();
            return obj;
        };
        GameObject objOld = get(p);
        GameObject objNew = f.f(objOld);
        if (objNew instanceof EmptyObject || objNew instanceof MarkerObject)
            return setObject(p, objNew, move);
        if (objNew instanceof LightbulbObject)
            return setObject(p, normalLightbulbsOnly && objOld.lightness > 0 ? f.f(objNew) : objNew, move);
        return false;
    }
}
