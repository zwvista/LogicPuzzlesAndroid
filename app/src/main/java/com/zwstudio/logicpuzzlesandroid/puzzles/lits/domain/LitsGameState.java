package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F0;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LitsGameState extends CellsGameState<LitsGame, LitsGameMove, LitsGameState> {
    public LitsObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public LitsGameState(LitsGame game) {
        super(game);
        objArray = new LitsObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new LitsEmptyObject();
    }

    public LitsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public LitsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, LitsObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, LitsObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved(boolean allowedObjectsOnly) {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                LitsObject o = get(r, c);
                if (o instanceof LitsForbiddenObject)
                    set(r, c, new LitsEmptyObject());
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasTreeNeighbor = () -> {
                    for (Position os : LitsGame.offset) {
                        Position p2 = p.add(os);
                        if (isValid(p2) && get(p2) instanceof LitsTreeObject)
                            return true;
                    }
                    return false;
                };
                LitsObject o = get(r, c);
                if (o instanceof LitsTreeObject) {
                    LitsTreeObject o2 = (LitsTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && !hasTreeNeighbor.f() ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if (!(o instanceof LitsForbiddenObject) && allowedObjectsOnly && hasTreeNeighbor.f())
                    set(r, c, new LitsForbiddenObject());
            }
        int n2 = game.treesInEachArea;
        for (int r = 0; r < rows(); r++) {
            int n1 = 0;
            for (int c = 0; c < cols(); c++)
                if (get(r, c) instanceof LitsTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (int c = 0; c < cols(); c++) {
                LitsObject o = get(r, c);
                if (o instanceof LitsTreeObject) {
                    LitsTreeObject o2 = (LitsTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if (!(o instanceof LitsForbiddenObject) && n1 == n2 && allowedObjectsOnly)
                    set(r, c, new LitsForbiddenObject());
            }
        }
        for (int c = 0; c < cols(); c++) {
            int n1 = 0;
            for (int r = 0; r < rows(); r++)
                if (get(r, c) instanceof LitsTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (int r = 0; r < rows(); r++) {
                LitsObject o = get(r, c);
                if (o instanceof LitsTreeObject) {
                    LitsTreeObject o2 = (LitsTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if (!(o instanceof LitsForbiddenObject) && n1 == n2 && allowedObjectsOnly)
                    set(r, c, new LitsForbiddenObject());
            }
        }
        for (List<Position> a : game.areas) {
            int n1 = 0;
            for (Position p : a)
                if (get(p) instanceof LitsTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (Position p : a) {
                LitsObject o = get(p);
                if (o instanceof LitsTreeObject) {
                    LitsTreeObject o2 = (LitsTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if (!(o instanceof LitsForbiddenObject) && n1 == n2 && allowedObjectsOnly)
                    set(p, new LitsForbiddenObject());
            }
        }
    }

    public boolean setObject(LitsGameMove move, boolean allowedObjectsOnly) {
        if (!isValid(move.p) || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved(allowedObjectsOnly);
        return true;
    }

    public boolean switchObject(LitsGameMove move, MarkerOptions markerOption, boolean allowedObjectsOnly) {
        F<LitsObject, LitsObject> f = obj -> {
            if (obj instanceof LitsEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new LitsMarkerObject() : new LitsTreeObject();
            if (obj instanceof LitsTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new LitsMarkerObject() : new LitsEmptyObject();
            if (obj instanceof LitsMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new LitsTreeObject() : new LitsEmptyObject();
            return obj;
        };
        LitsObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move, allowedObjectsOnly);
    }
}
