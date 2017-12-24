package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.List;

import fj.F2;
import fj.function.Effect1;

public class RobotCrosswordsGame extends CellsGame<RobotCrosswordsGame, RobotCrosswordsGameMove, RobotCrosswordsGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public int[] objArray;
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

    public List<List<Position>> areas = new ArrayList<>();
    public int horzAreaCount = 0;

    public RobotCrosswordsGame(List<String> layout, GameInterface<RobotCrosswordsGame, RobotCrosswordsGameMove, RobotCrosswordsGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        objArray = new int[rows() * cols()];

        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                char ch = str.charAt(c);
                set(r, c, ch == '.' ? -1 : ch == ' ' ? 0 : ch - '0');
            }
        }

        List<Position> area = new ArrayList<>();
        Effect1<Boolean> f = isHorz -> {
            if (area.isEmpty()) return;
            if (area.size() > 1) {
                areas.add(new ArrayList<>(area));
                if (isHorz) horzAreaCount++;
            }
            area.clear();
        };
        for (int r = 0; r < rows(); r++) {
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) == -1)
                    f.f(true);
                else
                    area.add(p);
            }
            f.f(true);
        }
        for (int c = 0; c < cols(); c++) {
            for (int r = 0; r < rows(); r++) {
                Position p = new Position(r, c);
                if (get(p) == -1)
                    f.f(false);
                else
                    area.add(p);
            }
            f.f(false);
        }

        RobotCrosswordsGameState state = new RobotCrosswordsGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(RobotCrosswordsGameMove move, F2<RobotCrosswordsGameState, RobotCrosswordsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        RobotCrosswordsGameState state = cloner.deepClone(state());
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

    public boolean switchObject(RobotCrosswordsGameMove move) {
        return changeObject(move, RobotCrosswordsGameState::switchObject);
    }

    public boolean setObject(RobotCrosswordsGameMove move) {
        return changeObject(move, RobotCrosswordsGameState::setObject);
    }

    public int getObject(Position p) {
        return state().get(p);
    }

    public int getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getHorzState(Position p) {
        return state().pos2horzState.get(p);
    }

    public HintState getVertState(Position p) {
        return state().pos2vertState.get(p);
    }
}
