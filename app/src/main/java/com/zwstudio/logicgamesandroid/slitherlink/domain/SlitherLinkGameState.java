package com.zwstudio.logicgamesandroid.slitherlink.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SlitherLinkGameState implements Cloneable {
    public SlitherLinkGame game;
    public SlitherLinkObject[] objArray;
    public boolean isSolved;

    public Position size() {return game.size;}
    public int rows() {return game.rows();}
    public int cols() {return game.cols();}
    public boolean isValid(int row, int col) { return game.isValid(row, col); }
    public boolean isValid(Position p) {
        return game.isValid(p);
    }

    public SlitherLinkGameState(SlitherLinkGame game) {
        this.game = game;
        objArray = new SlitherLinkObject[rows() * cols()];
        for (SlitherLinkObject obj: objArray)
            obj = new SlitherLinkEmptyObject();
    }

    @Override
    public SlitherLinkGameState clone(){
        try {
            SlitherLinkGameState o = (SlitherLinkGameState)super.clone();
            o.game = game;
            o.objArray = new SlitherLinkObject[objArray.length];
            for (int i = 0; i < objArray.length; i++)
                o.objArray[i] = objArray[i].clone();
            o.isSolved = isSolved;
            return o;
        } catch(CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    public SlitherLinkObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public SlitherLinkObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, SlitherLinkObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, SlitherLinkObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                SlitherLinkObject o = get(r, c);
                if (o instanceof SlitherLinkEmptyObject && o.lightness == 0 ||
                        o instanceof SlitherLinkMarkerObject && o.lightness == 0)
                    isSolved = false;
                else if (o instanceof SlitherLinkLightbulbObject) {
                    SlitherLinkLightbulbObject o2 = (SlitherLinkLightbulbObject)o;
                    o2.state = o.lightness == 1 ? SlitherLinkLightbulbObject.LightbulbState.Normal : SlitherLinkLightbulbObject.LightbulbState.Error;
                    if (o.lightness > 1) isSolved = false;
                } else if (o instanceof SlitherLinkWallObject) {
                    SlitherLinkWallObject o2 = (SlitherLinkWallObject)o;
                    if (o2.lightbulbs < 0) continue;
                    int n = 0;
                    for (Position os : SlitherLinkGame.offset) {
                        Position p2 = p.add(os);
                        if (!isValid(p2)) continue;
                        if (get(p2) instanceof SlitherLinkLightbulbObject) n++;
                    }
                    o2.state = n < o2.lightbulbs ? SlitherLinkWallObject.WallState.Normal :
                            n == o2.lightbulbs ? SlitherLinkWallObject.WallState.Complete :
                            SlitherLinkWallObject.WallState.Error;
                    if (n != o2.lightbulbs) isSolved = false;
                }
            }
    }

    private boolean objChanged(Position p, SlitherLinkObject objNew, SlitherLinkGameMove move, boolean toajust, boolean tolighten) {
        move.p = p;
        move.obj = objNew;
        set(p, objNew);
        if (toajust) {
            F<Integer, Integer> f = n -> tolighten ? n + 1 : n > 0 ? n - 1 : n;
            SlitherLinkObject obj = get(p);
            obj.lightness = f.f(obj.lightness);
            for (Position os : SlitherLinkGame.offset)
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    obj = get(p2);
                    if (obj instanceof SlitherLinkWallObject) break;
                    obj.lightness = f.f(obj.lightness);
                }
            updateIsSolved();
        }
        return true;
    }

    public boolean setObject(Position p, SlitherLinkObject objNew, SlitherLinkGameMove move) {
        SlitherLinkObject objOld = get(p);
        objNew.lightness = objOld.lightness;
        if (objOld instanceof SlitherLinkEmptyObject && objNew instanceof SlitherLinkMarkerObject ||
                objOld instanceof SlitherLinkMarkerObject && objNew instanceof SlitherLinkEmptyObject)
            return objChanged(p, objNew, move, false, false);
        if (objOld instanceof SlitherLinkEmptyObject && objNew instanceof SlitherLinkLightbulbObject ||
                objOld instanceof SlitherLinkMarkerObject && objNew instanceof SlitherLinkLightbulbObject)
            return objChanged(p, objNew, move, true, true);
        if (objOld instanceof SlitherLinkLightbulbObject && objNew instanceof SlitherLinkEmptyObject ||
                objOld instanceof SlitherLinkLightbulbObject && objNew instanceof SlitherLinkMarkerObject)
            return objChanged(p, objNew, move, true, false);
        if (objNew instanceof SlitherLinkWallObject)
            set(p, objNew);
        return false;
    }

    public boolean switchObject(Position p, SlitherLinkGame.MarkerOptions markerOption, boolean normalLightbulbsOnly, SlitherLinkGameMove move) {
        F<SlitherLinkObject, SlitherLinkObject> f = obj -> {
            if (obj instanceof SlitherLinkEmptyObject)
                return markerOption == SlitherLinkGame.MarkerOptions.MarkerBeforeLightbulb ?
                        new SlitherLinkMarkerObject() : new SlitherLinkLightbulbObject();
            if (obj instanceof SlitherLinkLightbulbObject)
                return markerOption == SlitherLinkGame.MarkerOptions.MarkerAfterLightbulb ?
                        new SlitherLinkMarkerObject() : new SlitherLinkEmptyObject();
            if (obj instanceof SlitherLinkMarkerObject)
                return markerOption == SlitherLinkGame.MarkerOptions.MarkerBeforeLightbulb ?
                        new SlitherLinkLightbulbObject() : new SlitherLinkEmptyObject();
            return obj;
        };
        SlitherLinkObject objOld = get(p);
        SlitherLinkObject objNew = f.f(objOld);
        if (objNew instanceof SlitherLinkEmptyObject || objNew instanceof SlitherLinkMarkerObject)
            return setObject(p, objNew, move);
        if (objNew instanceof SlitherLinkLightbulbObject)
            return setObject(p, normalLightbulbsOnly && objOld.lightness > 0 ? f.f(objNew) : objNew, move);
        return false;
    }
}
