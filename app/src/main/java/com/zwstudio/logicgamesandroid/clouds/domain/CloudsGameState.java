package com.zwstudio.logicgamesandroid.clouds.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.util.Arrays;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class CloudsGameState {
    public CloudsGame game;
    public CloudsObject[] objArray;
    public boolean isSolved;

    public Position size() {return game.size;}
    public int rows() {return game.rows();}
    public int cols() {return game.cols();}
    public boolean isValid(int row, int col) { return game.isValid(row, col); }
    public boolean isValid(Position p) {
        return game.isValid(p);
    }

    public CloudsGameState(CloudsGame game) {
        this.game = game;
        objArray = new CloudsObject[rows() * cols()];
        Arrays.fill(objArray, new CloudsEmptyObject());
    }

    public CloudsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public CloudsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, CloudsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, CloudsObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                CloudsObject o = get(r, c);
                if (o instanceof CloudsEmptyObject && o.lightness == 0 ||
                        o instanceof CloudsMarkerObject && o.lightness == 0)
                    isSolved = false;
                else if (o instanceof CloudsLightbulbObject) {
                    CloudsLightbulbObject o2 = (CloudsLightbulbObject)o;
                    o2.state = o.lightness == 1 ? CloudsLightbulbState.Normal : CloudsLightbulbState.Error;
                    if (o.lightness > 1) isSolved = false;
                } else if (o instanceof CloudsWallObject) {
                    CloudsWallObject o2 = (CloudsWallObject) o;
                    int n2 = game.pos2hint.get(p);
                    if (n2 < 0) continue;
                    int n1 = 0;
                    for (Position os : CloudsGame.offset) {
                        Position p2 = p.add(os);
                        if (!isValid(p2)) continue;
                        if (get(p2) instanceof CloudsLightbulbObject) n1++;
                    }
                    o2.state = n1 < n2 ? LogicGamesHintState.Normal : n1 == n2 ? LogicGamesHintState.Complete : LogicGamesHintState.Error;
                    if (n1 != n2) isSolved = false;
                }
            }
    }

    private boolean objChanged(CloudsGameMove move, boolean toajust, boolean tolighten) {
        Position p = move.p;
        set(p, move.obj);
        if (toajust) {
            F<Integer, Integer> f = n -> tolighten ? n + 1 : n > 0 ? n - 1 : n;
            CloudsObject obj = get(p);
            obj.lightness = f.f(obj.lightness);
            for (Position os : CloudsGame.offset)
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    obj = get(p2);
                    if (obj instanceof CloudsWallObject) break;
                    obj.lightness = f.f(obj.lightness);
                }
            updateIsSolved();
        }
        return true;
    }

    public boolean setObject(CloudsGameMove move) {
        Position p = move.p;
        CloudsObject objOld = get(p);
        CloudsObject objNew = move.obj;
        objNew.lightness = objOld.lightness;
        if (objOld instanceof CloudsEmptyObject && objNew instanceof CloudsMarkerObject ||
                objOld instanceof CloudsMarkerObject && objNew instanceof CloudsEmptyObject)
            return objChanged(move, false, false);
        if (objOld instanceof CloudsEmptyObject && objNew instanceof CloudsLightbulbObject ||
                objOld instanceof CloudsMarkerObject && objNew instanceof CloudsLightbulbObject)
            return objChanged(move, true, true);
        if (objOld instanceof CloudsLightbulbObject && objNew instanceof CloudsEmptyObject ||
                objOld instanceof CloudsLightbulbObject && objNew instanceof CloudsMarkerObject)
            return objChanged(move, true, false);
        if (objNew instanceof CloudsWallObject)
            set(p, objNew);
        return false;
    }

    public boolean switchObject(CloudsMarkerOptions markerOption, boolean normalLightbulbsOnly, CloudsGameMove move) {
        F<CloudsObject, CloudsObject> f = obj -> {
            if (obj instanceof CloudsEmptyObject)
                return markerOption == CloudsMarkerOptions.MarkerBeforeLightbulb ?
                        new CloudsMarkerObject() : new CloudsLightbulbObject();
            if (obj instanceof CloudsLightbulbObject)
                return markerOption == CloudsMarkerOptions.MarkerAfterLightbulb ?
                        new CloudsMarkerObject() : new CloudsEmptyObject();
            if (obj instanceof CloudsMarkerObject)
                return markerOption == CloudsMarkerOptions.MarkerBeforeLightbulb ?
                        new CloudsLightbulbObject() : new CloudsEmptyObject();
            return obj;
        };
        CloudsObject objOld = get(move.p);
        CloudsObject objNew = f.f(objOld);
        if (objNew instanceof CloudsEmptyObject || objNew instanceof CloudsMarkerObject) {
            move.obj = objNew;
            return setObject(move);
        }
        if (objNew instanceof CloudsLightbulbObject) {
            move.obj = normalLightbulbsOnly && objOld.lightness > 0 ? f.f(objNew) : objNew;
            return setObject(move);
        }
        return false;
    }
}
