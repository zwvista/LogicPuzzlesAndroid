package com.zwstudio.logicgamesandroid.slitherlink.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SlitherLinkGameState {
    public SlitherLinkGame game;
    public SlitherLinkObject[][] objArray;
    public Map<Position, LogicGamesHintState> pos2state = new HashMap<>();
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
        objArray = new SlitherLinkObject[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new SlitherLinkObject[4];
            Arrays.fill(objArray[i], SlitherLinkObject.Empty);
        }
    }

    public SlitherLinkObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public SlitherLinkObject[] get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, SlitherLinkObject[] dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, SlitherLinkObject[] obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = false;
    }

    public boolean setObject(SlitherLinkGameMove move) {
        Position p1 = move.p;
        boolean isH = move.objOrientation == SlitherLinkObjectOrientation.Horizontal;
        int i1 = isH ? 1 : 2;
        SlitherLinkObject o = get(p1)[i1];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(SlitherLinkGame.offset[isH ? 1 : 2]);
        int i2 = isH ? 3 : 0;
        get(p2)[i2] = get(p1)[i1] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(SlitherLinkMarkerOptions markerOption, SlitherLinkGameMove move) {
        F<SlitherLinkObject, SlitherLinkObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == SlitherLinkMarkerOptions.MarkerBeforeLine ?
                        SlitherLinkObject.Marker : SlitherLinkObject.Line;
            case Line:
                return markerOption == SlitherLinkMarkerOptions.MarkerAfterLine ?
                        SlitherLinkObject.Marker : SlitherLinkObject.Empty;
            case Marker:
                return markerOption == SlitherLinkMarkerOptions.MarkerBeforeLine ?
                        SlitherLinkObject.Line : SlitherLinkObject.Empty;
            }
            return obj;
        };
        SlitherLinkObject[] dotObj = get(move.p);
        move.obj = f.f(dotObj[move.objOrientation == SlitherLinkObjectOrientation.Horizontal ? 1 : 2]);
        return setObject(move);
    }
}
