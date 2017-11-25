package com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain;

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

import static fj.data.Array.array;

/**
 * Created by zwvista on 2016/09/29.
 */

public class WallsGameState extends CellsGameState<WallsGame, WallsGameMove, WallsGameState> {
    public WallsObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public WallsGameState(WallsGame game) {
        super(game);
        objArray = new WallsObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new WallsEmptyObject();
    }

    public WallsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public WallsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, WallsObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, WallsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(WallsGameMove move) {
        if (!isValid(move.p) || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(WallsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<WallsObject, WallsObject> f = obj -> {
            if (obj instanceof WallsEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new WallsMarkerObject() : new WallsTreeObject();
            if (obj instanceof WallsTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new WallsMarkerObject() : new WallsEmptyObject();
            if (obj instanceof WallsMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new WallsTreeObject() : new WallsEmptyObject();
            return obj;
        };
        WallsObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Walls

        Summary
        Put one Tree in each Park, row and column.(two in bigger levels)

        Description
        1. In Walls, you have many differently coloured areas(Walls) on the board.
        2. The goal is to plant Trees, following these rules:
        3. A Tree can't touch another Tree, not even diagonally.
        4. Each park must have exactly ONE Tree.
        5. There must be exactly ONE Tree in each row and each column.
        6. Remember a Tree CANNOT touch another Tree diagonally,
           but it CAN be on the same diagonal line.
        7. Larger puzzles have TWO Trees in each park, each row and each column.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                WallsObject o = get(r, c);
                if (o instanceof WallsForbiddenObject)
                    set(r, c, new WallsEmptyObject());
            }
        // 3. A Tree can't touch another Tree, not even diagonally.
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasNeighbor = () -> {
                    return array(WallsGame.offset).exists(os -> {
                        Position p2 = p.add(os);
                        return isValid(p2) && get(p2) instanceof WallsTreeObject;
                    });
                };
                WallsObject o = get(r, c);
                if (o instanceof WallsTreeObject) {
                    WallsTreeObject o2 = (WallsTreeObject)o;
                    o2.state = !hasNeighbor.f() ? AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof WallsEmptyObject || o instanceof WallsMarkerObject) && allowedObjectsOnly && hasNeighbor.f())
                    set(r, c, new WallsForbiddenObject());
            }
        int n2 = game.treesInEachArea;
        // 5. There must be exactly ONE Tree in each row.
        for (int r = 0; r < rows(); r++) {
            int n1 = 0;
            for (int c = 0; c < cols(); c++)
                if (get(r, c) instanceof WallsTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (int c = 0; c < cols(); c++) {
                WallsObject o = get(r, c);
                if (o instanceof WallsTreeObject) {
                    WallsTreeObject o2 = (WallsTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof WallsEmptyObject || o instanceof WallsMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    set(r, c, new WallsForbiddenObject());
            }
        }
        // 5. There must be exactly ONE Tree in each column.
        for (int c = 0; c < cols(); c++) {
            int n1 = 0;
            for (int r = 0; r < rows(); r++)
                if (get(r, c) instanceof WallsTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (int r = 0; r < rows(); r++) {
                WallsObject o = get(r, c);
                if (o instanceof WallsTreeObject) {
                    WallsTreeObject o2 = (WallsTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof WallsEmptyObject || o instanceof WallsMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    set(r, c, new WallsForbiddenObject());
            }
        }
        // 4. Each park must have exactly ONE Tree.
        for (List<Position> a : game.areas) {
            int n1 = 0;
            for (Position p : a)
                if (get(p) instanceof WallsTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (Position p : a) {
                WallsObject o = get(p);
                if (o instanceof WallsTreeObject) {
                    WallsTreeObject o2 = (WallsTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof WallsEmptyObject || o instanceof WallsMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    set(p, new WallsForbiddenObject());
            }
        }
    }
}
