package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.function.Effect0;

/**
 * Created by zwvista on 2016/09/29.
 */

public class DisconnectFourGameState extends CellsGameState<DisconnectFourGame, DisconnectFourGameMove, DisconnectFourGameState> {
    public DisconnectFourObject[] objArray;
    public Map<Position, AllowedObjectState> pos2state = new HashMap<>();

    public DisconnectFourGameState(DisconnectFourGame game) {
        super(game);
        objArray = new DisconnectFourObject[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
        updateIsSolved();
    }

    public DisconnectFourObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public DisconnectFourObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, DisconnectFourObject dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, DisconnectFourObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(DisconnectFourGameMove move) {
        if (!isValid(move.p) || game.get(move.p) != DisconnectFourObject.Empty || get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(DisconnectFourGameMove move) {
        if (!isValid(move.p) || game.get(move.p) != DisconnectFourObject.Empty) return false;
        DisconnectFourObject o = get(move.p);
        move.obj = o == DisconnectFourObject.Empty ? DisconnectFourObject.Yellow :
            o == DisconnectFourObject.Yellow ? DisconnectFourObject.Red:
            DisconnectFourObject.Empty;
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 11/Disconnect Four

        Summary
        Win by not winning!

        Description
        1. The opposite of the famous game 'Connect Four', where you must line
           up four tokens of the same colour.
        2. In this puzzle you have to ensure that there are no more than three
           tokens of the same colour lined up horizontally, vertically or
           diagonally.
    */
    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                pos2state.put(p, AllowedObjectState.Normal);
            }
        DisconnectFourObject oLast = DisconnectFourObject.Empty;
        List<Position> trees = new ArrayList<>();
        Effect0 checkTrees = () -> {
            if (trees.size() > 3) {
                isSolved = false;
                for (Position p : trees)
                    pos2state.put(p, AllowedObjectState.Error);
            }
            trees.clear();
        };
        for (int r = 0; r < rows(); r++) {
            oLast = DisconnectFourObject.Empty;
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                DisconnectFourObject o = get(p);
                if (o != oLast) {
                    checkTrees.f();
                    oLast = o;
                }
                if (o == DisconnectFourObject.Empty)
                    isSolved = false;
                else
                    trees.add(p);
            }
            checkTrees.f();
        }
        for (int c = 0; c < cols(); c++) {
            oLast = DisconnectFourObject.Empty;
            for (int r = 0; r < rows(); r++) {
                Position p = new Position(r, c);
                DisconnectFourObject o = get(p);
                if (o != oLast) {
                    checkTrees.f();
                    oLast = o;
                }
                if (o == DisconnectFourObject.Empty)
                    isSolved = false;
                else
                    trees.add(p);
            }
            checkTrees.f();
        }
    }
}
