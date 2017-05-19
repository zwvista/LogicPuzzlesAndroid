package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class MinesweeperGameState extends CellsGameState<MinesweeperGame, MinesweeperGameMove, MinesweeperGameState> {
    public MinesweeperObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public MinesweeperGameState(MinesweeperGame game) {
        super(game);
        objArray = new MinesweeperObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new MinesweeperEmptyObject();
        for (Position p : game.pos2hint.keySet())
            set(p, new MinesweeperHintObject());
        updateIsSolved();
    }

    public MinesweeperObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public MinesweeperObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, MinesweeperObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, MinesweeperObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(MinesweeperGameMove move) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(MinesweeperGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<MinesweeperObject, MinesweeperObject> f = obj -> {
            if (obj instanceof MinesweeperEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new MinesweeperMarkerObject() : new MinesweeperMineObject();
            else if(obj instanceof MinesweeperMineObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new MinesweeperMarkerObject() : new MinesweeperEmptyObject();
            else if(obj instanceof MinesweeperMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new MinesweeperMineObject() : new MinesweeperEmptyObject();
            return obj;
        };
        MinesweeperObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 6/Minesweeper

        Summary
        You know the drill :)

        Description
        1. Find the mines on the field.
        2. Numbers tell you how many mines there are close by, touching that
           number horizontally, vertically or diagonally.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                if (get(r, c) instanceof MinesweeperForbiddenObject)
                    set(r, c, new MinesweeperEmptyObject());
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            List<Position> rng = new ArrayList<>();
            for (Position os : MinesweeperGame.offset) {
                Position p2 = p.add(os);
                if (!isValid(p2)) continue;
                MinesweeperObject o = get(p2);
                if (o instanceof MinesweeperMineObject)
                    n1++;
                else if (o instanceof MinesweeperEmptyObject)
                    rng.add(p2.plus());
            }
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2)
                isSolved = false;
            else if(allowedObjectsOnly)
                for (Position p2 : rng)
                    set(p2, new MinesweeperForbiddenObject());
        }
    }
}
