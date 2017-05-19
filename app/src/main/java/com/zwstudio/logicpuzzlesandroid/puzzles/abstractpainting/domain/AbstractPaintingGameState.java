package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain;

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

import static fj.data.Array.arrayArray;

/**
 * Created by zwvista on 2016/09/29.
 */

public class AbstractPaintingGameState extends CellsGameState<AbstractPaintingGame, AbstractPaintingGameMove, AbstractPaintingGameState> {
    public AbstractPaintingObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public AbstractPaintingGameState(AbstractPaintingGame game) {
        super(game);
        objArray = new AbstractPaintingObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new AbstractPaintingEmptyObject();
    }

    public AbstractPaintingObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public AbstractPaintingObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, AbstractPaintingObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, AbstractPaintingObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(AbstractPaintingGameMove move) {
        if (!isValid(move.p) || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(AbstractPaintingGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<AbstractPaintingObject, AbstractPaintingObject> f = obj -> {
            if (obj instanceof AbstractPaintingEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new AbstractPaintingMarkerObject() : new AbstractPaintingTreeObject();
            if (obj instanceof AbstractPaintingTreeObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new AbstractPaintingMarkerObject() : new AbstractPaintingEmptyObject();
            if (obj instanceof AbstractPaintingMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new AbstractPaintingTreeObject() : new AbstractPaintingEmptyObject();
            return obj;
        };
        AbstractPaintingObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Abstract Painting

        Summary
        Abstract Logic

        Description
        1. The goal is to reveal part of the abstract painting behind the board.
        2. Outer numbers tell how many tiles form the painting on the row and column.
        3. The region of the painting can be entirely hidden or revealed.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                AbstractPaintingObject o = get(r, c);
                if (o instanceof AbstractPaintingForbiddenObject)
                    set(r, c, new AbstractPaintingEmptyObject());
            }
        // 3. A Tree can't touch another Tree, not even diagonally.
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasNeighbor = () -> {
                    return arrayArray(AbstractPaintingGame.offset).exists(os -> {
                        Position p2 = p.add(os);
                        return isValid(p2) && get(p2) instanceof AbstractPaintingTreeObject;
                    });
                };
                AbstractPaintingObject o = get(r, c);
                if (o instanceof AbstractPaintingTreeObject) {
                    AbstractPaintingTreeObject o2 = (AbstractPaintingTreeObject)o;
                    o2.state = !hasNeighbor.f() ? AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof AbstractPaintingEmptyObject || o instanceof AbstractPaintingMarkerObject) && allowedObjectsOnly && hasNeighbor.f())
                    set(r, c, new AbstractPaintingForbiddenObject());
            }
        int n2 = game.treesInEachArea;
        // 5. There must be exactly ONE Tree in each row.
        for (int r = 0; r < rows(); r++) {
            int n1 = 0;
            for (int c = 0; c < cols(); c++)
                if (get(r, c) instanceof AbstractPaintingTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (int c = 0; c < cols(); c++) {
                AbstractPaintingObject o = get(r, c);
                if (o instanceof AbstractPaintingTreeObject) {
                    AbstractPaintingTreeObject o2 = (AbstractPaintingTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof AbstractPaintingEmptyObject || o instanceof AbstractPaintingMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    set(r, c, new AbstractPaintingForbiddenObject());
            }
        }
        // 5. There must be exactly ONE Tree in each column.
        for (int c = 0; c < cols(); c++) {
            int n1 = 0;
            for (int r = 0; r < rows(); r++)
                if (get(r, c) instanceof AbstractPaintingTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (int r = 0; r < rows(); r++) {
                AbstractPaintingObject o = get(r, c);
                if (o instanceof AbstractPaintingTreeObject) {
                    AbstractPaintingTreeObject o2 = (AbstractPaintingTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof AbstractPaintingEmptyObject || o instanceof AbstractPaintingMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    set(r, c, new AbstractPaintingForbiddenObject());
            }
        }
        // 4. Each park must have exactly ONE Tree.
        for (List<Position> a : game.areas) {
            int n1 = 0;
            for (Position p : a)
                if (get(p) instanceof AbstractPaintingTreeObject) n1++;
            if (n1 != n2) isSolved = false;
            for (Position p : a) {
                AbstractPaintingObject o = get(p);
                if (o instanceof AbstractPaintingTreeObject) {
                    AbstractPaintingTreeObject o2 = (AbstractPaintingTreeObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof AbstractPaintingEmptyObject || o instanceof AbstractPaintingMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    set(p, new AbstractPaintingForbiddenObject());
            }
        }
    }
}
