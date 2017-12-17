package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class ABCPathGame extends CellsGame<ABCPathGame, ABCPathGameMove, ABCPathGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(-1, 1),
            new Position(0, 1),
            new Position(1, 1),
            new Position(1, 0),
            new Position(1, -1),
            new Position(0, -1),
            new Position(-1, -1),
    };

    public boolean isValid(int row, int col) {
        return row >= 1 && col >= 1 && row < size.row - 1 && col < size.col - 1;
    }

    public Map<Character, Position> ch2pos = new HashMap<>();

    public char[] objArray;
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

    public ABCPathGame(List<String> layout, GameInterface<ABCPathGame, ABCPathGameMove, ABCPathGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new char[rows() * cols()];

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                set(p, ch);
                if (r == 0 || r == rows() - 1 || c == 0 || c == cols() - 1)
                    ch2pos.put(ch, p);
            }
        }

        ABCPathGameState state = new ABCPathGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(ABCPathGameMove move, F2<ABCPathGameState, ABCPathGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        ABCPathGameState state = cloner.deepClone(state());
        boolean changed = f.f(state, move);
        if (changed) {
            states.add(state);
            stateIndex++;
            moves.add(move);
            moveAdded(move);
            levelUpdated(states.get(stateIndex - 1), state);
        }
        return changed;
   }

    public boolean switchObject(ABCPathGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(ABCPathGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public char getObject(Position p) {
        return state().get(p);
    }

    public char getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getState(int row, int col) {
        return state().pos2state.get(new Position(row, col));
    }
}
