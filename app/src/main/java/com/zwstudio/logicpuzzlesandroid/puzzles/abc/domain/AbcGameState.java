package com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwvista on 2016/09/29.
 */

public class AbcGameState extends CellsGameState<AbcGame, AbcGameMove, AbcGameState> {
    private char[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public AbcGameState(AbcGame game) {
        super(game);
        objArray = new char[rows() * cols()];
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                set(r, c, game.get(r, c));
        row2state = new HintState[rows() * 2];
        col2state = new HintState[cols() * 2];
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

    public HintState getState(int row, int col) {
        return row == 0 && col >= 1 && col < cols() - 1 ? col2state[col * 2] :
                row == rows() - 1 && col >= 1 && col < cols() - 1 ? col2state[col * 2 + 1] :
                col == 0 && row >= 1 && row < rows() - 1 ? row2state[row * 2] :
                col == cols() - 1 && row >= 1 && row < rows() - 1 ? row2state[row * 2 + 1] :
                HintState.Normal;
    }

    private void updateIsSolved() {
        isSolved = true;
        List<Character> chars = new ArrayList<>();
        for (int r = 1; r < rows() - 1; r++) {
            char h1 = get(r, 0), h2 = get(r, cols() - 1);
            char ch11 = ' ', ch21 = ' ';
            chars.clear();
            for (int c = 1; c < cols() - 1; c++) {
                char ch12 = get(r, c), ch22 = get(r, cols() - 1 - c);
                if (ch11 == ' ' && ch12 != ' ') ch11 = ch12;
                if (ch21 == ' ' && ch22 != ' ') ch21 = ch22;
                if (ch12 == ' ') continue;
                if (chars.contains(ch12))
                    isSolved = false;
                else
                    chars.add(ch12);
            }
            HintState s1 = ch11 == ' ' ? HintState.Normal : ch11 == h1 ? HintState.Complete : HintState.Error;
            HintState s2 = ch21 == ' ' ? HintState.Normal : ch21 == h2 ? HintState.Complete : HintState.Error;
            row2state[r * 2] = s1; row2state[r * 2 + 1] = s2;
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false;
            if (chars.size() != game.chMax - 'A' + 1) isSolved = false;
        }
        for (int c = 1; c < cols() - 1; c++) {
            char h1 = get(0, c), h2 = get(rows() - 1, c);
            char ch11 = ' ', ch21 = ' ';
            chars.clear();
            for (int r = 1; r < rows() - 1; r++) {
                char ch12 = get(r, c), ch22 = get(rows() - 1 - r, c);
                if (ch11 == ' ' && ch12 != ' ') ch11 = ch12;
                if (ch21 == ' ' && ch22 != ' ') ch21 = ch22;
                if (ch12 == ' ') continue;
                if (chars.contains(ch12))
                    isSolved = false;
                else
                    chars.add(ch12);
            }
            HintState s1 = ch11 == ' ' ? HintState.Normal : ch11 == h1 ? HintState.Complete : HintState.Error;
            HintState s2 = ch21 == ' ' ? HintState.Normal : ch21 == h2 ? HintState.Complete : HintState.Error;
            col2state[c * 2] = s1; col2state[c * 2 + 1] = s2;
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false;
            if (chars.size() != game.chMax - 'A' + 1) isSolved = false;
        }
    }

    public boolean setObject(AbcGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(AbcGameMove move) {
        Position p = move.p;
        if (!isValid(p)) return false;
        char o = get(p);
        move.obj = o == ' ' ? 'A' : o == game.chMax ? ' ' : (char)(o + 1);
        return setObject(move);
    }
}
