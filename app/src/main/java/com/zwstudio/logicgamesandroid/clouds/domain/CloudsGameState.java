package com.zwstudio.logicgamesandroid.clouds.domain;

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
        Arrays.fill(objArray, CloudsObject.Empty);
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
    }

    public boolean setObject(CloudsGameMove move) {
        Position p = move.p;
        CloudsObject objOld = get(p);
        CloudsObject objNew = move.obj;
        set(p, move.obj);
        return true;
    }

    public boolean switchObject(CloudsMarkerOptions markerOption, CloudsGameMove move) {
        F<CloudsObject, CloudsObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == CloudsMarkerOptions.MarkerBeforeCloud ?
                        CloudsObject.Marker : CloudsObject.Cloud;
            case Cloud:
                return markerOption == CloudsMarkerOptions.MarkerAfterCloud ?
                        CloudsObject.Marker : CloudsObject.Empty;
            case Marker:
                return markerOption == CloudsMarkerOptions.MarkerBeforeCloud ?
                        CloudsObject.Cloud : CloudsObject.Empty;
            }
            return obj;
        };
        CloudsObject objOld = get(move.p);
        move.obj = f.f(objOld);
        return setObject(move);
    }
}
