package com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fj.F;
import fj.F2;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SnailGame extends CellsGame<SnailGame, SnailGameMove, SnailGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public char[] objArray;
    public List<Position> snailPathGrid, snailPathLine;

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

    public SnailGame(List<String> layout, GameInterface<SnailGame, SnailGameMove, SnailGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new char[rows() * cols()];
        Arrays.fill(objArray, ' ');
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                char ch = str.charAt(c);
                if (ch >= '0' && ch <= '9')
                    set(r, c, ch);
            }
        }

        F<Integer, List<Position>> snailPath = n -> {
            List<Position> path = new ArrayList<>();
            Set<Position> rng = new HashSet<>();
            for (int r = 0; r < n; r++)
                for (int c = 0; c < n; c++)
                    rng.add(new Position(r, c));
            Position p = new Position(0, -1);
            int dir = 1;
            while (!rng.isEmpty()) {
                Position p2 = p.add(SnailGame.offset[dir]);
                if (rng.contains(p2))
                    rng.remove(p = p2);
                else {
                    dir = (dir + 1) % 4;
                    p.addBy(SnailGame.offset[dir]);
                    rng.remove(p);
                }
                path.add(p.plus());
            }
            return path;
        };
        snailPathGrid = snailPath.f(rows());
        snailPathLine = snailPath.f(rows() + 1);

        SnailGameState state = new SnailGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(SnailGameMove move, F2<SnailGameState, SnailGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        SnailGameState state = cloner.deepClone(state());
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

    public boolean switchObject(SnailGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(SnailGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public char getObject(Position p) {
        return state().get(p);
    }

    public char getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getRowState(int row) {
        return state().row2state[row];
    }

    public HintState getColState(int col) {
        return state().col2state[col];
    }

    public HintState getPositionState(int row, int col) {
        return state().pos2state.get(new Position(row, col));
    }
}
