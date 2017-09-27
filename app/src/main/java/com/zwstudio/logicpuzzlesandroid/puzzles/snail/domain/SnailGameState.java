package com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SnailGameState extends CellsGameState<SnailGame, SnailGameMove, SnailGameState> {
    private char[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();
    public HintState[] row2state;
    public HintState[] col2state;

    public SnailGameState(SnailGame game) {
        super(game);
        objArray = new char[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved();
    }

    public char get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public char get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, char obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, char obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(SnailGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(SnailGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        Position p = move.p;
        if (!isValid(p)) return false;
        char o = get(p);
        move.obj =
                o == ' ' ? markerOption == MarkerOptions.MarkerFirst ? '.' : '1' :
                o == '.' ? markerOption == MarkerOptions.MarkerFirst ? '1' : ' ' :
                o == '3' ? markerOption == MarkerOptions.MarkerLast ? '.' : ' ' :
                (char)(o + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Snail

        Summary
        Darken some tiles so no number appears in a row or column more than once

        Description
        1. The goal is to shade squares so that a number appears only once in a
           row or column.
        2. While doing that, you must take care that shaded squares don't touch
           horizontally or vertically between them.
        3. In the end all the un-shaded squares must form a single continuous area.
    */
    private void updateIsSolved() {
        isSolved = true;
        String chars;
        for (int r = 0; r < rows(); r++) {
            chars = "";
            row2state[r] = HintState.Complete;
            for (int c = 0; c < cols(); c++) {
                char ch = get(r, c);
                if (ch == ' ') continue;
                if (chars.contains(String.valueOf(ch)))
                    break;
                else
                    chars += ch;
            }
            if (chars.length() != 3) {
                row2state[r] = HintState.Error; isSolved = false;
            }
        }
        for (int c = 0; c < cols(); c++) {
            chars = "";
            col2state[c] = HintState.Complete;
            for (int r = 0; r < rows(); r++) {
                char ch = get(r, c);
                if (ch == ' ') continue;
                if (chars.contains(String.valueOf(ch)))
                    break;
                else
                    chars += ch;
            }
            if (chars.length() != 3) {
                col2state[c] = HintState.Error; isSolved = false;
            }
        }
        List<Position> rng = new ArrayList<>();
        chars = "";
        for (Position p : game.snailPathGrid) {
            char ch = get(p);
            pos2state.put(p, HintState.Error);
            if (ch == ' ') continue;
            rng.add(p);
            chars += ch;
            pos2state.put(p, HintState.Complete);
        }
        int cnt = chars.length();
        if (chars.charAt(0) != '1') {
            pos2state.put(rng.get(0), HintState.Error); isSolved = false;
        }
        if (chars.charAt(cnt - 1) != '3') {
            pos2state.put(rng.get(cnt - 1), HintState.Error); isSolved = false;
        }
        for (int i = 0; i < cnt - 1; i++) {
            char ch1 = chars.charAt(i), ch2 = chars.charAt(i + 1);
            if (!(ch1 == '1' && ch2 == '2' || ch1 == '2' && ch2 == '3' || ch1 == '3' && ch2 == '1')) {
                pos2state.put(rng.get(i), HintState.Error); isSolved = false;
            }
        }
    }
}
