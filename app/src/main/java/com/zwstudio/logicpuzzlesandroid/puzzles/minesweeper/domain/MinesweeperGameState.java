package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
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
        Arrays.fill(objArray, MinesweeperObject.Empty);
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            pos2state.put(p, n == 0 ? HintState.Complete : HintState.Normal);
        }
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
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        MinesweeperObject.Marker : MinesweeperObject.Filled;
            case Filled:
                return markerOption == MarkerOptions.MarkerLast ?
                        MinesweeperObject.Marker : MinesweeperObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        MinesweeperObject.Filled : MinesweeperObject.Empty;
            }
            return obj;
        };
        MinesweeperObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 4/Minesweeper

        Summary
        Paint the mosaic, filling squares with the numbered hints

        Description
        1. In Minesweeper, there is a hidden image which can be discovered using the
           numbered hints.
        2. A number tells you how many tiles must be filled in the 3*3 area formed
           by the tile itself and the ones surrounding it.
        3. Thus the numbers can go from 0, where no tiles is filled, to 9, where
           every tile is filled in a 3*3 area around the tile with the number.
        4. Every number in between denotes that some of the tiles in that 3*3
           area are filled and some are not.
    */
    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            for (Position os : MinesweeperGame.offset) {
                Position p2 = p.add(os);
                if (!isValid(p2)) continue;
                if (get(p2) == MinesweeperObject.Filled) n1++;
            }
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
    }
}
