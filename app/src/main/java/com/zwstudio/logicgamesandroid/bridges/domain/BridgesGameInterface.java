package com.zwstudio.logicgamesandroid.bridges.domain;

/**
 * Created by TCC-2-9002 on 2016/09/30.
 */

public interface BridgesGameInterface {
    void moveAdded(BridgesGame game, BridgesGameMove move);
    void levelInitilized(BridgesGame game, BridgesGameState state);
    void levelUpdated(BridgesGame game, BridgesGameState stateFrom, BridgesGameState stateTo);
    void gameSolved(BridgesGame game);
}
