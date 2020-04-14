package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.List;

import fj.F2;

public class WallSentinelsGame extends CellsGame<WallSentinelsGame, WallSentinelsGameMove, WallSentinelsGameState> {
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

    public WallSentinelsObject[] objArray;

    public WallSentinelsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public WallSentinelsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, WallSentinelsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, WallSentinelsObject obj) {
        set(p.row, p.col, obj);
    }

    public WallSentinelsGame(List<String> layout, GameInterface<WallSentinelsGame, WallSentinelsGameMove, WallSentinelsGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length() / 2);
        objArray = new WallSentinelsObject[rows() * cols()];
        Arrays.fill(objArray, new WallSentinelsEmptyObject());
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                String s = str.substring(c * 2, c * 2 + 2);
                if (!s.equals("  ")) {
                    int n = s.charAt(1) - '0';
                    WallSentinelsObject o = s.charAt(0) == '.' ?
                            new WallSentinelsHintLandObject(){{tiles = n; state = HintState.Normal;}} :
                            new WallSentinelsHintWallObject(){{tiles = n; state = HintState.Normal;}};
                    set(p, o);
                }
            }
        }
        WallSentinelsGameState state = new WallSentinelsGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(WallSentinelsGameMove move, F2<WallSentinelsGameState, WallSentinelsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        WallSentinelsGameState state = cloner.deepClone(state());
        boolean changed = f(state, move);
        if (changed) {
            states.add(state);
            stateIndex++;
            moves.add(move);
            moveAdded(move);
            levelUpdated(states.get(stateIndex - 1), state);
        }
        return changed;
   }

    public boolean switchObject(WallSentinelsGameMove move) {
        return changeObject(move, WallSentinelsGameState::switchObject);
    }

    public boolean setObject(WallSentinelsGameMove move) {
        return changeObject(move, WallSentinelsGameState::setObject);
    }

    public WallSentinelsObject getObject(Position p) {
        return state().get(p);
    }

    public WallSentinelsObject getObject(int row, int col) {
        return state().get(row, col);
    }
}
