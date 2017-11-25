package com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.Ord;

import static fj.data.List.iterableList;
import static fj.data.Stream.range;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TatamiGameState extends CellsGameState<TatamiGame, TatamiGameMove, TatamiGameState> {
    public char[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public TatamiGameState(TatamiGame game) {
        super(game);
        objArray = new char[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
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

    public boolean setObject(TatamiGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != ' ' || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TatamiGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != ' ') return false;
        char o = get(p);
        move.obj = o == ' ' ? '1' : o == '3' ? ' ' : (char)(o + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Tatami

        Summary
        1,2,3... 1,2,3... Fill the mats

        Description
        1. Each rectangle represents a mat(Tatami) which is of the same size.
           You must fill each Tatami with a number ranging from 1 to size.
        2. Each number can appear only once in each Tatami.
        3. In one row or column, each number must appear the same number of times.
        4. You can't have two identical numbers touching horizontally or vertically.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        List<Character> chars2 = Arrays.asList('1', '2', '3');
        List<Character> chars3 = iterableList(chars2).bind(ch -> iterableList(Collections.nCopies(rows() / 3, ch))).toJavaList();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) == ' ') isSolved = false;
                pos2state.put(p, HintState.Normal);
            }
        for (int r = 0; r < rows(); r++) {
            boolean lineSolved = true;
            for (int c = 0; c < cols() - 1; c++) {
                Position p1 = new Position(r, c), p2 = new Position(r, c + 1);
                char ch1 = get(p1), ch2 = get(p2);
                if (ch1 != ' ' && ch2 != ' ' && ch1 == ch2) {
                    isSolved = lineSolved = false;
                    pos2state.put(p1, HintState.Error);
                    pos2state.put(p2, HintState.Error);
                }
            }
            int r2 = r;
            List<Character> chars = range(0, cols()).map(c -> get(r2, c)).sort(Ord.charOrd).toJavaList();
            if (chars.get(0) != ' ' && !chars.equals(chars3)) {
                isSolved = lineSolved = false;
                for (int c = 0; c < cols(); c++)
                    pos2state.put(new Position(r, c), HintState.Error);
            }
            if (lineSolved)
                for (int c = 0; c < cols(); c++)
                    pos2state.put(new Position(r, c), HintState.Complete);
        }
        for (int c = 0; c < cols(); c++) {
            boolean lineSolved = true;
            for (int r = 0; r < rows() - 1; r++) {
                Position p1 = new Position(r, c), p2 = new Position(r + 1, c);
                char ch1 = get(p1), ch2 = get(p2);
                if (ch1 != ' ' && ch2 != ' ' && ch1 == ch2) {
                    isSolved = lineSolved = false;
                    pos2state.put(p1, HintState.Error);
                    pos2state.put(p2, HintState.Error);
                }
            }
            int c2 = c;
            List<Character> chars = range(0, rows()).map(r -> get(r, c2)).sort(Ord.charOrd).toJavaList();
            if (chars.get(0) != ' ' && !chars.equals(chars3)) {
                isSolved = lineSolved = false;
                for (int r = 0; r < rows(); r++)
                    pos2state.put(new Position(r, c), HintState.Error);
            }
            if (lineSolved)
                for (int r = 0; r < rows(); r++)
                    pos2state.put(new Position(r, c), HintState.Complete);
        }
        for (List<Position> a : game.areas) {
            List<Character> chars = iterableList(a).map(p -> get(p)).sort(Ord.charOrd).toJavaList();
            if (chars.get(0) != ' ' && !chars.equals(chars2)) {
                isSolved = false;
                for (Position p : a)
                    pos2state.put(p, HintState.Error);
            }
        }
    }
}
