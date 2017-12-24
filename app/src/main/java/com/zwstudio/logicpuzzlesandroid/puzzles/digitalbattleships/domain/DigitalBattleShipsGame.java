package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.F2;

public class DigitalBattleShipsGame extends CellsGame<DigitalBattleShipsGame, DigitalBattleShipsGameMove, DigitalBattleShipsGameState> {
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

    public int[] objArray;
    public int[] row2hint;
    public int[] col2hint;

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

    public DigitalBattleShipsGame(List<String> layout, GameInterface<DigitalBattleShipsGame, DigitalBattleShipsGameMove, DigitalBattleShipsGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size() - 1, layout.get(0).length() / 2 - 1);
        objArray = new int[rows() * cols()];
        row2hint = new int[rows()];
        col2hint = new int[cols()];

        for (int r = 0; r < rows() + 1; r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols() + 1; c++) {
                String s = str.substring(c * 2, c * 2 + 2);
                if (s.equals("  ")) continue;
                int n = Integer.parseInt(s.trim());
                if (r == rows())
                    col2hint[c] = n;
                else if (c == cols())
                    row2hint[r] = n;
                else
                    set(r, c, n);
            }
        }

        DigitalBattleShipsGameState state = new DigitalBattleShipsGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(DigitalBattleShipsGameMove move, F2<DigitalBattleShipsGameState, DigitalBattleShipsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        DigitalBattleShipsGameState state = cloner.deepClone(state());
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

    public boolean switchObject(DigitalBattleShipsGameMove move) {
        return changeObject(move, DigitalBattleShipsGameState::switchObject);
    }

    public boolean setObject(DigitalBattleShipsGameMove move) {
        return changeObject(move, DigitalBattleShipsGameState::setObject);
    }

    public DigitalBattleShipsObject getObject(Position p) {
        return state().get(p);
    }

    public DigitalBattleShipsObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getRowState(int row) {
        return state().row2state[row];
    }

    public HintState getColState(int col) {
        return state().col2state[col];
    }
}
