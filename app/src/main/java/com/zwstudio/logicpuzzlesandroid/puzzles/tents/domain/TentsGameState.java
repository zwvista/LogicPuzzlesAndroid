package com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TentsGameState extends CellsGameState<TentsGame, TentsGameMove, TentsGameState> {
    public TentsObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public TentsGameState(TentsGame game, boolean allowedObjectsOnly) {
        super(game);
        objArray = new TentsObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new TentsEmptyObject();
        for (Position p : game.pos2tree)
            set(p, new TentsTreeObject());
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved(allowedObjectsOnly);
    }

    public TentsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public TentsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, TentsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, TentsObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved(boolean allowedObjectsOnly) {
        isSolved = true;
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++)
                if (get(r, c) instanceof TentsTentObject)
                    n1++;
            row2state[r] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++)
                if (get(r, c) instanceof TentsTentObject)
                    n1++;
            col2state[c] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                TentsObject o = get(r, c);
                if (o instanceof TentsForbiddenObject)
                    set(r, c, new TentsEmptyObject());
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                TentsObject o = get(r, c);
                if (o instanceof TentsTreeObject) continue;
                boolean hasTree = false, hasTent = false;
                for (Position os : TentsGame.offset) {
                    Position p2 = p.add(os);
                    if (isValid(p2) && get(p2) instanceof TentsTreeObject)
                        hasTree = true;
                }
                for (Position os : TentsGame.offset2) {
                    Position p2 = p.add(os);
                    if (isValid(p2) && get(p2) instanceof TentsTentObject)
                        hasTent = true;
                }
                if (o instanceof TentsTentObject) {
                    TentsTentObject o2 = (TentsTentObject)o;
                    o2.state = hasTree && !hasTent ? AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof TentsEmptyObject || o instanceof TentsMarkerObject) && allowedObjectsOnly &&
                        (col2state[c] != HintState.Normal || row2state[r] != HintState.Normal || !hasTree || hasTent))
                    set(r, c, new TentsForbiddenObject());
            }
   }

    public boolean setObject(TentsGameMove move, boolean allowedObjectsOnly) {
        if (!isValid(move.p) || get(move.p) == move.obj) return false;
        set(move.p, move.obj);
        updateIsSolved(allowedObjectsOnly);
        return true;
    }

    public boolean switchObject(TentsGameMove move, MarkerOptions markerOption, boolean allowedObjectsOnly) {
        F<TentsObject, TentsObject> f = obj -> {
            if (obj instanceof TentsEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TentsMarkerObject() : new TentsTentObject();
            if (obj instanceof TentsTentObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new TentsMarkerObject() : new TentsEmptyObject();
            if (obj instanceof TentsMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new TentsTentObject() : new TentsEmptyObject();
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move, allowedObjectsOnly);
    }
}
