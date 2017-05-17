package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BridgesGame extends CellsGame<BridgesGame, BridgesGameMove, BridgesGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };

    public Map<Position, BridgesIslandInfo> islandsInfo = new HashMap<>();
    public boolean isIsland(Position p) {return islandsInfo.containsKey(p);}

    public BridgesGame(List<String> layout, GameInterface<BridgesGame, BridgesGameMove, BridgesGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length());
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch >= '0' && ch <= '9') {
                    BridgesIslandInfo info = new BridgesIslandInfo();
                    info.bridges = ch - '0';
                    islandsInfo.put(p, info);
                }
            }
        }
        for (Map.Entry<Position, BridgesIslandInfo> entry : islandsInfo.entrySet()) {
            Position p = entry.getKey();
            BridgesIslandInfo info = entry.getValue();
            for (int i = 0; i < 4; i++) {
                Position os = offset[i];
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os))
                    if (isIsland(p2)) {
                        info.neighbors[i] = p2;
                        break;
                    }
            }
        }
        BridgesGameState state = new BridgesGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    public boolean switchBridges(BridgesGameMove move) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        BridgesGameState state = cloner.deepClone(state());
        if (move.pTo.compareTo(move.pFrom) < 0) {
            Position t = move.pFrom;
            move.pFrom = move.pTo;
            move.pTo = t;
        }
        if (!state.switchBridges(move)) return false;
        states.add(state);
        stateIndex++;
        moves.add(move);
        moveAdded(move);
        levelUpdated(states.get(stateIndex - 1), state);
        return true;
   }

    public BridgesObject getObject(Position p) {
        return state().get(p);
    }

    public BridgesObject getObject(int row, int col) {
        return state().get(row, col);
    }
}
