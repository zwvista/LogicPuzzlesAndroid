package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.Map;

import fj.data.HashSet;

import static fj.data.HashSet.iterableHashSet;
import static fj.data.List.iterableList;
import static fj.data.Stream.range;

/**
 * Created by zwvista on 2016/09/29.
 */

public class KropkiGameState extends CellsGameState<KropkiGame, KropkiGameMove, KropkiGameState> {
    public int[] objArray;
    public Map<Position, HintState> pos2horzHint = new HashMap<>();
    public Map<Position, HintState> pos2vertHint = new HashMap<>();

    public KropkiGameState(KropkiGame game) {
        super(game);
        objArray = new int[rows() * cols()];
        updateIsSolved();
    }

    public int get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public int get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, int obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, int obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(KropkiGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(KropkiGameMove move) {
        Position p = move.p;
        if (!isValid(p)) return false;
        int o = get(p);
        move.obj = (o + 1) % (game.cols() + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 6/Kropki

        Summary
        Fill the rows and columns with numbers, respecting the relations

        Description
        1. The Goal is to enter numbers 1 to board size once in every row and
           column.
        2. A Dot between two tiles give you hints about the two numbers:
        3. Black Dot - one number is twice the other.
        4. White Dot - the numbers are consecutive.
        5. Where the numbers are 1 and 2, there can be either a Black Dot(2 is
           1*2) or a White Dot(1 and 2 are consecutive).
        6. Please note that when two numbers are either consecutive or doubles,
           there MUST be a Dot between them!

        Variant
        7. In later 9*9 levels you will also have bordered and coloured areas,
           which must also contain all the numbers 1 to 9.
    */
    private void updateIsSolved() {
        isSolved = true;
        range(0, rows()).foreachDoEffect(r -> {
            HashSet<Integer> nums = iterableHashSet(range(0, cols()).map(c -> get(r, c)));
            if (nums.contains(0) || nums.size() != cols()) isSolved = false;
        });
        range(0, cols()).foreachDoEffect(c -> {
            HashSet<Integer> nums = iterableHashSet(range(0, rows()).map(r -> get(r, c)));
            if (nums.contains(0) || nums.size() != rows()) isSolved = false;
        });
        if (game.bordered)
            iterableList(game.areas).foreachDoEffect(a -> {
                HashSet<Integer> nums = iterableHashSet(iterableList(a).map(p -> get(p)));
                if (nums.contains(0) || nums.size() != a.size()) isSolved = false;
            });
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                for (int i = 0; i < 2; i++) {
                    if (i == 0 && c == cols() - 1 || i == 1 && r == rows() - 1) continue;
                    int n1 = get(p), n2 = get(r + i, c + 1 - i);
                    if (n1 == 0 || n2 == 0) {
                        (i == 0 ? pos2horzHint : pos2vertHint).put(p, HintState.Normal);
                        isSolved = false;
                        continue;
                    }
                    if (n1 > n2) {int temp = n1; n1 = n2; n2 = temp;}
                    KropkiHint kh = (i == 0 ? game.pos2horzHint : game.pos2vertHint).get(p);
                    HintState s =
                            n2 != n1 + 1 && n2 != n1 * 2 && kh == KropkiHint.None ||
                            n2 == n1 + 1 && kh == KropkiHint.Consecutive ||
                            n2 == n1 * 2 && kh == KropkiHint.Twice ? HintState.Complete : HintState.Error;
                    (i == 0 ? pos2horzHint : pos2vertHint).put(p, s);
                    if (s != HintState.Complete) isSolved = false;
                }
            }
    }
}
