package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class PowerGridGameState extends CellsGameState<PowerGridGame, PowerGridGameMove, PowerGridGameState> {
    public PowerGridObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public PowerGridGameState(PowerGridGame game, boolean allowedObjectsOnly) {
        super(game);
        objArray = new PowerGridObject[rows() * cols()];
        Arrays.fill(objArray, new PowerGridEmptyObject());
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved(allowedObjectsOnly);
    }

    public PowerGridObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public PowerGridObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, PowerGridObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, PowerGridObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(PowerGridGameMove move, boolean allowedObjectsOnly) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved(allowedObjectsOnly);
        return true;
    }

    public boolean switchObject(PowerGridGameMove move, MarkerOptions markerOption, boolean allowedObjectsOnly) {
        F<PowerGridObject, PowerGridObject> f = obj -> {
            if (obj instanceof PowerGridEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new PowerGridMarkerObject() : new PowerGridPostObject();
            if (obj instanceof PowerGridPostObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new PowerGridMarkerObject() : new PowerGridEmptyObject();
            if (obj instanceof PowerGridMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new PowerGridPostObject() : new PowerGridEmptyObject();
            return obj;
        };
        PowerGridObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move, allowedObjectsOnly);
    }

    private void updateIsSolved(boolean allowedObjectsOnly) {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                PowerGridObject o = get(r, c);
                if (o instanceof PowerGridForbiddenObject)
                    set(r, c, new PowerGridEmptyObject());
                else if (o instanceof PowerGridPostObject)
                    ((PowerGridPostObject) o).state = AllowedObjectState.Normal;
            }
        for (int r = 0; r < rows(); r++) {
            List<Position> posts = new ArrayList<>();
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) instanceof PowerGridPostObject)
                    posts.add(p);
            }
            int n1 = posts.size(), n2 = game.row2hint[r] + 1;
            HintState s = n1 < 2 ? HintState.Normal : n1 == 2 && n2 == posts.get(1).col - posts.get(0).col ?
                    HintState.Complete : HintState.Error;
            row2state[r] = s;
            if (s != HintState.Complete) isSolved = false;
            if (s == HintState.Error)
                for (Position p : posts)
                    ((PowerGridPostObject) get(p)).state = AllowedObjectState.Error;
            if (allowedObjectsOnly && n1 > 0)
                for (int c = 0; c < cols(); c++)
                    if (get(r, c) instanceof PowerGridEmptyObject && (n1 > 1 || n1 == 1 && n2 != Math.abs(posts.get(0).col - c)))
                        set(r, c, new PowerGridForbiddenObject());
        }
        for (int c = 0; c < cols(); c++) {
            List<Position> posts = new ArrayList<>();
            for (int r = 0; r < rows(); r++) {
                Position p = new Position(r, c);
                if (get(p) instanceof PowerGridPostObject)
                    posts.add(p);
            }
            int n1 = posts.size(), n2 = game.col2hint[c] + 1;
            HintState s = n1 < 2 ? HintState.Normal : n1 == 2 && n2 == posts.get(1).row - posts.get(0).row ?
                    HintState.Complete : HintState.Error;
            col2state[c] = s;
            if (s != HintState.Complete) isSolved = false;
            if (s == HintState.Error)
                for (Position p : posts)
                    ((PowerGridPostObject) get(p)).state = AllowedObjectState.Error;
            if (allowedObjectsOnly && n1 > 0)
                for (int r = 0; r < rows(); r++)
                    if (get(r, c) instanceof PowerGridEmptyObject && (n1 > 1 || n1 == 1 && n2 != Math.abs(posts.get(0).row - r)))
                        set(r, c, new PowerGridForbiddenObject());
        }
    }
}
