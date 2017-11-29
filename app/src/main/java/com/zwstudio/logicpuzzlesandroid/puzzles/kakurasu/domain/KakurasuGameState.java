package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class KakurasuGameState extends CellsGameState<KakurasuGame, KakurasuGameMove, KakurasuGameState> {
    public KakurasuObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public KakurasuGameState(KakurasuGame game) {
        super(game);
        objArray = new KakurasuObject[rows() * cols()];
        Arrays.fill(objArray, KakurasuObject.Empty);
        row2state = new HintState[rows() * 2];
        col2state = new HintState[cols() * 2];
        updateIsSolved();
    }

    public KakurasuObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public KakurasuObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, KakurasuObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, KakurasuObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(KakurasuGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(KakurasuGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<KakurasuObject, KakurasuObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        KakurasuObject.Marker : KakurasuObject.Cloud;
            case Cloud:
                return markerOption == MarkerOptions.MarkerLast ?
                        KakurasuObject.Marker : KakurasuObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        KakurasuObject.Cloud : KakurasuObject.Empty;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 8/Kakurasu

        Summary
        Cloud Kakuro on a Skyscraper

        Description
        1. On the bottom and right border, you see the value of (respectively)
           the columns and rows.
        2. On the other borders, on the top and the left, you see the hints about
           which tile have to be filled on the board. These numbers represent the
           sum of the values mentioned above.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == KakurasuObject.Forbidden)
                    set(r, c, KakurasuObject.Empty);
        for (int r = 1; r < rows() - 1; r++) {
            // 1. On the bottom and right border, you see the value of (respectively)
            // the rows.
            int n1 = 0, n2 = game.row2hint[r * 2];
            for (int c = 1; c < cols() - 1; c++)
                if (get(r, c) == KakurasuObject.Cloud)
                    // 2. On the other borders, on the top and the left, you see the hints about
                    // which tile have to be filled on the board.
                    n1 += game.col2hint[c * 2 + 1];
            // 2. These numbers represent the sum of the values mentioned above.
            HintState s = n1 == 0 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            row2state[r * 2] = s;
            if (s != HintState.Complete) isSolved = false;
            if (n1 >= n2 && allowedObjectsOnly)
                for (int c = 1; c < cols() - 1; c++)
                    switch (get(r, c)) {
                    case Empty: case Marker: set(r, c, KakurasuObject.Forbidden); break;
                    }
        }
        for (int c = 1; c < cols() - 1; c++) {
            // 1. On the bottom and right border, you see the value of (respectively)
            // the columns.
            int n1 = 0, n2 = game.col2hint[c * 2];
            for (int r = 1; r < rows() - 1; r++)
                if (get(r, c) == KakurasuObject.Cloud)
                    // 2. On the other borders, on the top and the left, you see the hints about
                    // which tile have to be filled on the board.
                    n1 += game.row2hint[r * 2 + 1];
            // 2. These numbers represent the sum of the values mentioned above.
            HintState s = n1 == 0 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            col2state[c * 2] = s;
            if (s != HintState.Complete) isSolved = false;
            if (n1 >= n2 && allowedObjectsOnly)
                for (int r = 1; r < rows() - 1; r++)
                    switch (get(r, c)) {
                    case Empty: case Marker: set(r, c, KakurasuObject.Forbidden); break;
                    }
        }
    }
}
