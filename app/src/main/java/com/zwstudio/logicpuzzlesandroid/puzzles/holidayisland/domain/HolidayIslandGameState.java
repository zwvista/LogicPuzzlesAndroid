package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain;

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

public class HolidayIslandGameState extends CellsGameState<HolidayIslandGame, HolidayIslandGameMove, HolidayIslandGameState> {
    public HolidayIslandObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public HolidayIslandGameState(HolidayIslandGame game) {
        super(game);
        objArray = new HolidayIslandObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new HolidayIslandEmptyObject();
    }

    public HolidayIslandObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public HolidayIslandObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, HolidayIslandObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, HolidayIslandObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(HolidayIslandGameMove move) {
        if (!isValid(move.p) || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(HolidayIslandGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<HolidayIslandObject, HolidayIslandObject> f = obj -> {
            if (obj instanceof HolidayIslandEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new HolidayIslandMarkerObject() : new HolidayIslandTreeObject();
            if (obj instanceof HolidayIslandTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new HolidayIslandMarkerObject() : new HolidayIslandEmptyObject();
            if (obj instanceof HolidayIslandMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new HolidayIslandTreeObject() : new HolidayIslandEmptyObject();
            return obj;
        };
        HolidayIslandObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/HolidayIsland

        Summary
        Put one Tree in each Park, row and column.(two in bigger levels)

        Description
        1. In HolidayIsland, you have many differently coloured areas(HolidayIsland) on the board.
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
                HolidayIslandObject o = get(r, c);
                if (o instanceof HolidayIslandForbiddenObject)
                    set(r, c, new HolidayIslandEmptyObject());
            }
        // 3. A Tree can't touch another Tree, not even diagonally.
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasNeighbor = () -> {
                    return array(HolidayIslandGame.offset).exists(os -> {
                        Position p2 = p.add(os);
                        return isValid(p2) && get(p2) instanceof HolidayIslandTreeObject;
                    });
                };
                HolidayIslandObject o = get(r, c);
                if (o instanceof HolidayIslandTreeObject) {
                    HolidayIslandTreeObject o2 = (HolidayIslandTreeObject)o;
                    o2.state = !hasNeighbor.f() ? AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof HolidayIslandEmptyObject || o instanceof HolidayIslandMarkerObject) && allowedObjectsOnly && hasNeighbor.f())
                    set(r, c, new HolidayIslandForbiddenObject());
            }
        int n2 = game.treesInEachArea;
        // 5. There must be exactly ONE Tree in each row.
        for (int r = 0; r < rows(); r++) {
            int n1 = 0;
            for (int c = 0; c < cols(); c++)
                if (get(r, c) instanceof HolidayIslandTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (int c = 0; c < cols(); c++) {
                HolidayIslandObject o = get(r, c);
                if (o instanceof HolidayIslandTreeObject) {
                    HolidayIslandTreeObject o2 = (HolidayIslandTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof HolidayIslandEmptyObject || o instanceof HolidayIslandMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    set(r, c, new HolidayIslandForbiddenObject());
            }
        }
        // 5. There must be exactly ONE Tree in each column.
        for (int c = 0; c < cols(); c++) {
            int n1 = 0;
            for (int r = 0; r < rows(); r++)
                if (get(r, c) instanceof HolidayIslandTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (int r = 0; r < rows(); r++) {
                HolidayIslandObject o = get(r, c);
                if (o instanceof HolidayIslandTreeObject) {
                    HolidayIslandTreeObject o2 = (HolidayIslandTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof HolidayIslandEmptyObject || o instanceof HolidayIslandMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    set(r, c, new HolidayIslandForbiddenObject());
            }
        }
        // 4. Each park must have exactly ONE Tree.
        for (List<Position> a : game.areas) {
            int n1 = 0;
            for (Position p : a)
                if (get(p) instanceof HolidayIslandTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (Position p : a) {
                HolidayIslandObject o = get(p);
                if (o instanceof HolidayIslandTreeObject) {
                    HolidayIslandTreeObject o2 = (HolidayIslandTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof HolidayIslandEmptyObject || o instanceof HolidayIslandMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    set(p, new HolidayIslandForbiddenObject());
            }
        }
    }
}
