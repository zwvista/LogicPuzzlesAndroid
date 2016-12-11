package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class MagnetsGameState extends CellsGameState<MagnetsGame, MagnetsGameMove, MagnetsGameState> {
    public MagnetsObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public MagnetsGameState(MagnetsGame game) {
        super(game);
        objArray = new MagnetsObject[rows() * cols()];
        Arrays.fill(objArray, MagnetsObject.Empty);
        row2state = new HintState[rows() * 2];
        col2state = new HintState[cols() * 2];
        updateIsSolved();
    }

    public MagnetsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public MagnetsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, MagnetsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, MagnetsObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++) {
            int np1 = 0, np2 = game.row2hint[r * 2];
            int nn1 = 0, nn2 = game.row2hint[r * 2 + 1];
            for (int c = 0; c < cols(); c++)
                switch (get(r, c)) {
                case Positive:
                    np1++; break;
                case Negative:
                    nn1++; break;
                }
            row2state[r * 2] = np1 < np2 ? HintState.Normal : np1 == np2 ? HintState.Complete : HintState.Error;
            row2state[r * 2 + 1] = nn1 < nn2 ? HintState.Normal : nn1 == nn2 ? HintState.Complete : HintState.Error;
            if (np1 != np2 || nn1 != nn2) isSolved = false;
        }
        for (int c = 0; c < cols(); c++) {
            int np1 = 0, np2 = game.col2hint[c * 2];
            int nn1 = 0, nn2 = game.col2hint[c * 2 + 1];
            for (int r = 0; r < rows(); r++)
                switch (get(r, c)) {
                case Positive:
                    np1++; break;
                case Negative:
                    nn1++; break;
                }
            col2state[c * 2] = np1 < np2 ? HintState.Normal : np1 == np2 ? HintState.Complete : HintState.Error;
            col2state[c * 2 + 1] = nn1 < nn2 ? HintState.Normal : nn1 == nn2 ? HintState.Complete : HintState.Error;
            if (np1 != np2 || nn1 != nn2) isSolved = false;
        }
    }

    public boolean setObject(MagnetsGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(MarkerOptions markerOption, MagnetsGameMove move) {
        F<MagnetsObject, MagnetsObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        MagnetsObject.Marker : MagnetsObject.Positive;
            case Positive:
                return MagnetsObject.Negative;
            case Negative:
                return markerOption == MarkerOptions.MarkerLast ?
                        MagnetsObject.Marker : MagnetsObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        MagnetsObject.Positive : MagnetsObject.Empty;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }
}
