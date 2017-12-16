package com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import fj.F;
import fj.F0;

public class TentsGameState extends CellsGameState<TentsGame, TentsGameMove, TentsGameState> {
    public TentsObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public TentsGameState(TentsGame game) {
        super(game);
        objArray = new TentsObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new TentsEmptyObject();
        for (Position p : game.pos2tree)
            set(p, new TentsTreeObject());
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved();
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

    public boolean setObject(TentsGameMove move) {
        if (!isValid(move.p) || get(move.p) == move.obj) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TentsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
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
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Tents

        Summary
        Each camper wants to put his Tent under the shade of a Tree. But he also
        wants his privacy!

        Description
        1. The board represents a camping field with many Trees. Campers want to set
           their Tent in the shade, horizontally or vertically adjacent to a Tree(not
           diagonally).
        2. At the same time they need their privacy, so a Tent can't have any other
           Tents near them, not even diagonally.
        3. The numbers on the borders tell you how many Tents there are in that row
           or column.
        4. Finally, each Tree has at least one Tent touching it, horizontally or
           vertically.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++)
                if (get(r, c) instanceof TentsTentObject)
                    n1++;
            // 3. The numbers on the borders tell you how many Tents there are in that row.
            row2state[r] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++)
                if (get(r, c) instanceof TentsTentObject)
                    n1++;
            // 3. The numbers on the borders tell you how many Tents there are in that column.
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
                F0<Boolean> hasTree = () -> {
                    for (Position os : TentsGame.offset) {
                        Position p2 = p.add(os);
                        if (isValid(p2) && get(p2) instanceof TentsTreeObject)
                            return true;
                    }
                    return false;
                };
                F<Boolean, Boolean> hasTent = isTree -> {
                    for (Position os : isTree ? TentsGame.offset : TentsGame.offset2) {
                        Position p2 = p.add(os);
                        if (isValid(p2) && get(p2) instanceof TentsTentObject)
                            return true;
                    }
                    return false;
                };
                if (o instanceof TentsTentObject) {
                    // 1. The board represents a camping field with many Trees. Campers want to set
                    // their Tent in the shade, horizontally or vertically adjacent to a Tree(not
                    // diagonally).
                    // 2. At the same time they need their privacy, so a Tent can't have any other
                    // Tents near them, not even diagonally.
                    TentsTentObject o2 = (TentsTentObject)o;
                    AllowedObjectState s = hasTree.f() && !hasTent.f(false) ? AllowedObjectState.Normal : AllowedObjectState.Error;
                    o2.state = s;
                    if (s == AllowedObjectState.Error) isSolved = false;
                } else if (o instanceof TentsTreeObject) {
                    // 4. Finally, each Tree has at least one Tent touching it, horizontally or
                    // vertically.
                    TentsTreeObject o2 = (TentsTreeObject)o;
                    AllowedObjectState s = hasTent.f(true) ? AllowedObjectState.Normal : AllowedObjectState.Error;
                    o2.state = s;
                    if (s == AllowedObjectState.Error) isSolved = false;
                } else if ((o instanceof TentsEmptyObject || o instanceof TentsMarkerObject) && allowedObjectsOnly &&
                        (col2state[c] != HintState.Normal || row2state[r] != HintState.Normal || !hasTree.f() || hasTent.f(false)))
                    set(r, c, new TentsForbiddenObject());
            }
    }
}
