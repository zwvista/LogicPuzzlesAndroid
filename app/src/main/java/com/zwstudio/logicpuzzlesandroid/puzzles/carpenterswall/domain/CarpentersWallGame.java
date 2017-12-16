package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.List;

import fj.F2;

public class CarpentersWallGame extends CellsGame<CarpentersWallGame, CarpentersWallGameMove, CarpentersWallGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };
    public static Position offset2[] = {
            new Position(0, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(1, 1),
    };

    public CarpentersWallObject[] objArray;

    public CarpentersWallObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public CarpentersWallObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, CarpentersWallObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, CarpentersWallObject obj) {
        set(p.row, p.col, obj);
    }

    public CarpentersWallGame(List<String> layout, GameInterface<CarpentersWallGame, CarpentersWallGameMove, CarpentersWallGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new CarpentersWallObject[rows() * cols()];
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch >= '0' && ch <= '9')
                    set(p, new CarpentersWallCornerObject() {{
                        tiles = ch - '0'; state = HintState.Normal;
                    }});
                else if (ch == 'O')
                    set(p, new CarpentersWallCornerObject() {{
                        tiles = 0; state = HintState.Normal;
                    }});
                else if (ch == '^')
                    set(p, new CarpentersWallUpObject());
                else if (ch == 'v')
                    set(p, new CarpentersWallDownObject());
                else if (ch == '<')
                    set(p, new CarpentersWallLeftObject());
                else if (ch == '>')
                    set(p, new CarpentersWallRightObject());
                else
                    set(p, new CarpentersWallEmptyObject());
            }
        }
        CarpentersWallGameState state = new CarpentersWallGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(CarpentersWallGameMove move, F2<CarpentersWallGameState, CarpentersWallGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        CarpentersWallGameState state = cloner.deepClone(state());
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

    public boolean switchObject(CarpentersWallGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(CarpentersWallGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public CarpentersWallObject getObject(Position p) {
        return state().get(p);
    }

    public CarpentersWallObject getObject(int row, int col) {
        return state().get(row, col);
    }
}
