package com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class Square100GameState extends CellsGameState<Square100Game, Square100GameMove, Square100GameState> {
    public String[] objArray;
    public int[] row2hint;
    public int[] col2hint;

    public Square100GameState(Square100Game game) {
        super(game);
        objArray = new String[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
        row2hint = new int[rows()];
        col2hint = new int[cols()];
        updateIsSolved();
    }

    public String get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public String get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, String obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, String obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(Square100GameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p).equals(move.obj)) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(Square100GameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        Position p = move.p;
        if (!isValid(p)) return false;
        String n = get(p);
        char o = n.charAt(move.isRightPart ? 2 : 0);
        o = o == ' ' ? markerOption == MarkerOptions.MarkerFirst ? '.' : '0' :
            o == '.' ? markerOption == MarkerOptions.MarkerFirst ? '0' : ' ' :
            o == '9' ? markerOption == MarkerOptions.MarkerLast ? '.' : ' ' :
            (char)(o + 1);
        if (!move.isRightPart && o == '0') o = '1';
        move.obj = move.isRightPart ? n.substring(0, 2) + o : o + n.substring(1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Square100

        Summary
        Exactly one hundred

        Description
        1. You are given a 3*3 or 4*4 square with digits in it.
        2. You have to add digits to some (or all) tiles, in order to produce
           the sum of 100 for every row and column.
        3. You can add digits before or after the given one.
    */
    private void updateIsSolved() {
        isSolved = true;
        F2<Integer, Integer, Integer> f = (r, c) -> {
            String o = get(r, c);
            int n = o.charAt(1) - '0';
            // 3. You can add digits before or after the given one.
            if (Character.isDigit(o.charAt(0))) n += (o.charAt(0) - '0') * 10;
            if (Character.isDigit(o.charAt(2))) n = n * 10 + (o.charAt(2) - '0');
            return n;
        };
        // 2. You have to add digits to some (or all) tiles, in order to produce
        // the sum of 100 for every row.
        for (int r = 0; r < rows(); r++) {
            int n = 0;
            for (int c = 0; c < cols(); c++)
                n += f.f(r, c);
            row2hint[r] = n;
            if (n != 100) isSolved = false;
        }
        // 2. You have to add digits to some (or all) tiles, in order to produce
        // the sum of 100 for every column.
        for (int c = 0; c < cols(); c++) {
            int n = 0;
            for (int r = 0; r < rows(); r++)
                n += f.f(r, c);
            col2hint[c] = n;
            if (n != 100) isSolved = false;
        }
    }
}
