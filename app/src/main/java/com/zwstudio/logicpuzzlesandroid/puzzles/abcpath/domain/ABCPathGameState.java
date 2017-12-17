package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.Ord;
import fj.P;
import fj.P2;
import fj.data.Stream;

import static fj.data.Array.array;
import static fj.data.List.iterableList;
import static fj.data.TreeMap.iterableTreeMap;

public class ABCPathGameState extends CellsGameState<ABCPathGame, ABCPathGameMove, ABCPathGameState> {
    private char[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public ABCPathGameState(ABCPathGame game) {
        super(game);
        objArray = new char[rows() * cols()];
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                set(r, c, game.get(r, c));
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

    public boolean setObject(ABCPathGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != ' ' || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(ABCPathGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != ' ') return false;
        char o = get(p);
        // 1.  Enter every letter from A to Y into the grid.
        List<Character> chars = Stream.range('A', 'Z').map(i -> (char)i.intValue()).toJavaList();
        for (int r = 1; r < rows() - 1; r++)
            for (int c = 1; c < cols() - 1; c++) {
                Position p2 = new Position(r, c);
                if (!p2.equals(p)) chars.remove(Character.valueOf(get(p2)));
            }
        int i = chars.contains(o) ? chars.indexOf(o) : chars.size() - 1;
        move.obj = o == ' ' ? chars.get(0) : i == chars.size() - 1 ? ' ' : chars.get(chars.size() - 1);
        return setObject(move);
    }

    /*
        https://www.brainbashers.com/showabcpath.asp
        ABC Path

        Description
        1.  Enter every letter from A to Y into the grid.
        2.  Each letter is next to the previous letter either horizontally, vertically or diagonally.
        3.  The clues around the edge tell you which row, column or diagonal each letter is in.
    */
    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                pos2state.put(new Position(r, c), HintState.Normal);
        List<P2<Position, Character>> pos2ch = new ArrayList<>();
        for (int r = 1; r < rows() - 1; r++)
            for (int c = 1; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                char ch = get(p);
                if (ch == ' ')
                    isSolved = false;
                else
                    pos2ch.add(P.p(p, ch));
            }
        Map<Character, List<Position>> ch2rng = iterableTreeMap(Ord.charOrd, iterableList(pos2ch).groupBy(kv -> kv._2(), Ord.charOrd)
                .map(kvs -> kvs.map(kv -> kv._1()).toJavaList())
                .toStream().filter(kv -> kv._2().size() > 1).toList()).toMutableMap();
        if (!ch2rng.isEmpty()) isSolved = false;
        for (List<Position> rng : ch2rng.values())
            for (Position p : rng)
                pos2state.put(p, HintState.Error);
        // 2.  Each letter is next to the previous letter either horizontally, vertically or diagonally.
        for (int r = 1; r < rows() - 1; r++)
            for (int c = 1; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                char ch = get(p);
                if (pos2state.get(p) == HintState.Normal && ch == 'A' || array(ABCPathGame.offset).exists(os -> {
                    Position p2 = p.add(os);
                    return isValid(p2) && get(p2) == ch - 1;
                }))
                    pos2state.put(p, HintState.Complete);
                else
                    isSolved = false;
            }
        // 3.  The clues around the edge tell you which row, column or diagonal each letter is in.
        for (Map.Entry<Character, Position> entry : game.ch2pos.entrySet()) {
            char ch = entry.getKey();
            Position p = entry.getValue();
            int r = p.row, c = p.col;
            if ((r == 0 || r == rows() - 1) && r == c && Stream.range(1, rows() - 1).exists(r2 -> get(r2, r2) == ch) ||
                    (r == 0 || r == rows() - 1) && r == rows() - 1 - c && Stream.range(1, rows() - 1).exists(r2 -> get(r2, rows() - 1 - r2) == ch) ||
                    (r == 0 || r == rows() - 1) && Stream.range(1, rows() - 1).exists(r2 -> get(r2, c) == ch) ||
                    (c == 0 || c == cols() - 1) && Stream.range(1, cols() - 1).exists(c2 -> get(r, c2) == ch))
                pos2state.put(p, HintState.Complete);
            else
                isSolved = false;
        }
    }

}
