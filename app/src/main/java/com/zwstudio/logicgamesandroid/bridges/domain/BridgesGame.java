package com.zwstudio.logicgamesandroid.bridges.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.CellsGame;
import com.zwstudio.logicgamesandroid.logicgames.domain.GameInterface;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

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

    public BridgesGame(List<String> layout, GameInterface<BridgesGame, BridgesGameMove, BridgesGameState> gi) {
        super(gi);
        size = new Position(layout.size(), layout.get(0).length());
        BridgesGameState state = new BridgesGameState(this);
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c);
                if (ch >= '0' && ch <= '9') {
                    BridgesIslandInfo info = new BridgesIslandInfo();
                    info.bridges = ch - '0';
                    islandsInfo.put(p, info);
                    state.set(r, c, new BridgesIslandObject());
                } else
                    state.set(r, c, new BridgesEmptyObject());
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
        states.add(state);
        levelInitilized(state);
    }

    public boolean switchBridges(Position pFrom, Position pTo) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        BridgesGameState state = cloner.deepClone(state());
        if (pTo.compareTo(pFrom) < 0) {
            Position t = pFrom;
            pFrom = pTo;
            pTo = t;
        }
        BridgesGameMove move = new BridgesGameMove();
        move.pFrom = pFrom; move.pTo = pTo;
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
