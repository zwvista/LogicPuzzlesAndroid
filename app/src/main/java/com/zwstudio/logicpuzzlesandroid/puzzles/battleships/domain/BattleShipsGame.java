package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

public class BattleShipsGame extends CellsGame<BattleShipsGame, BattleShipsGameMove, BattleShipsGameState> {
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

    public int[] row2hint;
    public int[] col2hint;
    public Map<Position, BattleShipsObject> pos2obj = new HashMap<>();

    public BattleShipsGame(List<String> layout, GameInterface<BattleShipsGame, BattleShipsGameMove, BattleShipsGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size() - 1, layout.get(0).length() - 1);
        row2hint = new int[rows()];
        col2hint = new int[cols()];

        for (int r = 0; r < rows() + 1; r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols() + 1; c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                switch(ch) {
                case '^':
                    pos2obj.put(p, BattleShipsObject.BattleShipTop); break;
                case 'v':
                    pos2obj.put(p, BattleShipsObject.BattleShipBottom); break;
                case '<':
                    pos2obj.put(p, BattleShipsObject.BattleShipLeft); break;
                case '>':
                    pos2obj.put(p, BattleShipsObject.BattleShipRight); break;
                case '+':
                    pos2obj.put(p, BattleShipsObject.BattleShipMiddle); break;
                case 'o':
                    pos2obj.put(p, BattleShipsObject.BattleShipUnit); break;
                case '.':
                    pos2obj.put(p, BattleShipsObject.Marker); break;
                default:
                    if (ch >= '0' && ch <= '9') {
                        int n = ch - '0';
                        if (r == rows())
                            col2hint[c] = n;
                        else if (c == cols())
                            row2hint[r] = n;
                    }
                    break;
                }
            }
        }

        BattleShipsGameState state = new BattleShipsGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(BattleShipsGameMove move, F2<BattleShipsGameState, BattleShipsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        BattleShipsGameState state = cloner.deepClone(state());
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

    public boolean switchObject(BattleShipsGameMove move) {
        return changeObject(move, BattleShipsGameState::switchObject);
    }

    public boolean setObject(BattleShipsGameMove move) {
        return changeObject(move, BattleShipsGameState::setObject);
    }

    public BattleShipsObject getObject(Position p) {
        return state().get(p);
    }

    public BattleShipsObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getRowState(int row) {
        return state().row2state[row];
    }

    public HintState getColState(int col) {
        return state().col2state[col];
    }
}
