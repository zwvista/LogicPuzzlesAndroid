package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class FenceLitsGame extends CellsGame<FenceLitsGame, FenceLitsGameMove, FenceLitsGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };
    public static Position offset2[] = {
            new Position(0, 0),
            new Position(1, 1),
            new Position(1, 1),
            new Position(0, 0),
    };
    public static int dirs[] = { 1, 0, 3, 2 };

    public static Position tetrominoes[][] = {
        // L
        {new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(2, 1)},
        {new Position(0, 1), new Position(1, 1), new Position(2, 0), new Position(2, 1)},
        {new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(1, 0)},
        {new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(1, 2)},
        {new Position(0, 0), new Position(0, 1), new Position(1, 0), new Position(2, 0)},
        {new Position(0, 0), new Position(0, 1), new Position(1, 1), new Position(2, 1)},
        {new Position(0, 0), new Position(1, 0), new Position(1, 1), new Position(1, 2)},
        {new Position(0, 2), new Position(1, 0), new Position(1, 1), new Position(1, 2)},
        // I
        {new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(3, 0)},
        {new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(0, 3)},
        // T
        {new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(1, 1)},
        {new Position(0, 1), new Position(1, 0), new Position(1, 1), new Position(2, 1)},
        {new Position(0, 1), new Position(1, 0), new Position(1, 1), new Position(1, 2)},
        {new Position(0, 0), new Position(1, 0), new Position(1, 1), new Position(2, 0)},
        // S
        {new Position(0, 0), new Position(0, 1), new Position(1, 1), new Position(1, 2)},
        {new Position(0, 1), new Position(0, 2), new Position(1, 0), new Position(1, 1)},
        {new Position(0, 0), new Position(1, 0), new Position(1, 1), new Position(2, 1)},
        {new Position(0, 1), new Position(1, 0), new Position(1, 1), new Position(2, 0)},
        // O
        {new Position(0, 0), new Position(0, 1), new Position(1, 0), new Position(1, 1)},
    };

    public GridLineObject[][] objArray;
    public Map<Position, Integer> pos2hint = new HashMap<>();

    public GridLineObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public GridLineObject[] get(Position p) {
        return get(p.row, p.col);
    }

    public FenceLitsGame(List<String> layout, GameInterface<FenceLitsGame, FenceLitsGameMove, FenceLitsGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size() + 1, layout.get(0).length() + 1);
        objArray = new GridLineObject[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new GridLineObject[4];
            Arrays.fill(objArray[i], GridLineObject.Empty);
        }
        for (int r = 0; r < rows() - 1; r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch >= '0' && ch <= '9') {
                    int n = ch - '0';
                    pos2hint.put(p, n);
                }
            }
        }
        for (int r = 0; r < rows() - 1; r++) {
            get(r, 0)[2] = GridLineObject.Line;
            get(r + 1, 0)[0] = GridLineObject.Line;
            get(r, cols() - 1)[2] = GridLineObject.Line;
            get(r + 1, cols() - 1)[0] = GridLineObject.Line;
        }
        for (int c = 0; c < cols() - 1; c++) {
            get(0, c)[1] = GridLineObject.Line;
            get(0, c + 1)[3] = GridLineObject.Line;
            get(rows() - 1, c)[1] = GridLineObject.Line;
            get(rows() - 1, c + 1)[3] = GridLineObject.Line;
        }
        FenceLitsGameState state = new FenceLitsGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(FenceLitsGameMove move, F2<FenceLitsGameState, FenceLitsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        FenceLitsGameState state = cloner.deepClone(state());
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

    public boolean switchObject(FenceLitsGameMove move) {
        return changeObject(move, FenceLitsGameState::switchObject);
    }

    public boolean setObject(FenceLitsGameMove move) {
        return changeObject(move, FenceLitsGameState::setObject);
    }

    public GridLineObject[] getObject(Position p) {
        return state().get(p);
    }

    public GridLineObject[] getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState pos2State(Position p) {
        return state().pos2state.get(p);
    }
}
