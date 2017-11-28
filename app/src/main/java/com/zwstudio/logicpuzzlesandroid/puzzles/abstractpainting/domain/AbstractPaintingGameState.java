package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class AbstractPaintingGameState extends CellsGameState<AbstractPaintingGame, AbstractPaintingGameMove, AbstractPaintingGameState> {
    public AbstractPaintingObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public AbstractPaintingGameState(AbstractPaintingGame game) {
        super(game);
        objArray = new AbstractPaintingObject[rows() * cols()];
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        Arrays.fill(objArray, AbstractPaintingObject.Empty);
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
        // 3. The region of the painting can be entirely hidden or revealed.
        for (Position p2 : game.areas.get(game.pos2area.get(move.p)))
            set(p2, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(AbstractPaintingGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<AbstractPaintingObject, AbstractPaintingObject> f = obj -> {
            switch (obj) {
                case Empty:
                    return markerOption == MarkerOptions.MarkerFirst ?
                            AbstractPaintingObject.Marker : AbstractPaintingObject.Painting;
                case Painting:
                    return markerOption == MarkerOptions.MarkerLast ?
                            AbstractPaintingObject.Marker : AbstractPaintingObject.Empty;
                case Marker:
                    return markerOption == MarkerOptions.MarkerFirst ?
                            AbstractPaintingObject.Painting : AbstractPaintingObject.Empty;
            }
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
                if (o == AbstractPaintingObject.Forbidden)
                    set(r, c, AbstractPaintingObject.Empty);
            }
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == AbstractPaintingObject.Painting)
                    n1++;
            // 2. Outer numbers tell how many tiles form the painting on the row.
            row2state[r] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++)
                if (get(r, c) == AbstractPaintingObject.Painting)
                    n1++;
            // 2. Outer numbers tell how many tiles form the painting on the column.
            col2state[c] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                AbstractPaintingObject o = get(r, c);
                if ((o == AbstractPaintingObject.Empty || o == AbstractPaintingObject.Marker) && allowedObjectsOnly && (
                        row2state[r] != HintState.Normal || col2state[c] != HintState.Normal))
                    set(r, c, AbstractPaintingObject.Forbidden);
            }
    }
}
