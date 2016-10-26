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
    public LogicGamesHintState[] row2state;
    public LogicGamesHintState[] col2state;
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
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == CloudsObject.Cloud)
                    n1++;
            row2state[r] = n1 < n2 ? LogicGamesHintState.Normal : n1 == n2 ? LogicGamesHintState.Complete : LogicGamesHintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++)
                if (get(r, c) == CloudsObject.Cloud)
                    n1++;
            col2state[c] = n1 < n2 ? LogicGamesHintState.Normal : n1 == n2 ? LogicGamesHintState.Complete : LogicGamesHintState.Error;
            if (n1 != n2) isSolved = false;
        }
        if (!isSolved) return;
    }

    public boolean setObject(CloudsGameMove move) {
        Position p = move.p;
        CloudsObject objOld = get(p);
        CloudsObject objNew = move.obj;
        set(p, move.obj);
        updateIsSolved();
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
