package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;

import fj.F;

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

    public boolean setObject(MagnetsGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.singles.contains(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(MagnetsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
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

    /*
        iOS Game: Logic Games/Puzzle Set 2/Magnets

        Summary
        Place Magnets on the board, respecting the orientation of poles

        Description
        1. Each Magnet has a positive(+) and a negative(-) pole.
        2. Every rectangle can either contain a Magnet or be empty.
        3. The number on the board tells you how many positive and negative poles
           you can see from there in a straight line.
        4. When placing a Magnet, you have to respect the rule that the same pole
           (+ and + / - and -) can't be adjacent horizontally or vertically.
        5. In some levels, a few numbers on the border can be hidden.
    */
    private void updateIsSolved() {
        isSolved = true;
        // 3. The number on the board tells you how many positive and negative poles
        // you can see from there in a straight line.
        for (int r = 0; r < rows(); r++) {
            int np1 = 0, np2 = game.row2hint[r * 2];
            int nn1 = 0, nn2 = game.row2hint[r * 2 + 1];
            for (int c = 0; c < cols(); c++)
                switch (get(r, c)) {
                    case Positive:
                        np1++;
                        break;
                    case Negative:
                        nn1++;
                        break;
                }
            row2state[r * 2] = np1 < np2 ? HintState.Normal : np1 == np2 ? HintState.Complete : HintState.Error;
            row2state[r * 2 + 1] = nn1 < nn2 ? HintState.Normal : nn1 == nn2 ? HintState.Complete : HintState.Error;
            if (np1 != np2 || nn1 != nn2) isSolved = false;
        }
        // 3. The number on the board tells you how many positive and negative poles
        // you can see from there in a straight line.
        for (int c = 0; c < cols(); c++) {
            int np1 = 0, np2 = game.col2hint[c * 2];
            int nn1 = 0, nn2 = game.col2hint[c * 2 + 1];
            for (int r = 0; r < rows(); r++)
                switch (get(r, c)) {
                    case Positive:
                        np1++;
                        break;
                    case Negative:
                        nn1++;
                        break;
                }
            col2state[c * 2] = np1 < np2 ? HintState.Normal : np1 == np2 ? HintState.Complete : HintState.Error;
            col2state[c * 2 + 1] = nn1 < nn2 ? HintState.Normal : nn1 == nn2 ? HintState.Complete : HintState.Error;
            if (np1 != np2 || nn1 != nn2) isSolved = false;
        }
        if (!isSolved) return;
        // 2. Every rectangle can either contain a Magnet or be empty.
        for (MagnetsArea a : game.areas)
            switch (a.type) {
                case Single:
                    continue;
                case Horizontal:
                case Vertical:
                    Position os = MagnetsGame.offset[a.type == MagnetsAreaType.Horizontal ? 1 : 2];
                    MagnetsObject o1 = get(a.p), o2 = get(a.p.add(os));
                    if (o1.isEmpty() != o2.isEmpty()) {
                        isSolved = false;
                        return;
                    }
                    break;
            }
        // 1. Each Magnet has a positive(+) and a negative(-) pole.
        // 4. When placing a Magnet, you have to respect the rule that the same pole
        // (+ and + / - and -) can't be adjacent horizontally or vertically.
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                MagnetsObject o = get(r, c);
                for (Position os : MagnetsGame.offset) {
                    Position p2 = p.add(os);
                    if (!isValid(p2)) continue;
                    MagnetsObject o2 = get(p2);
                    if (o.isPole() && o == o2) {
                        isSolved = false;
                        return;
                    }
                }
            }
    }
}
