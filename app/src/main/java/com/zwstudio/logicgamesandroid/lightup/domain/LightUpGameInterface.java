package com.zwstudio.logicgamesandroid.lightup.domain;

/**
 * Created by TCC-2-9002 on 2016/09/30.
 */

public interface LightUpGameInterface {
    void moveAdded(LightUpGame game, LightUpGameMove move);
    void levelInitilized(LightUpGame game, LightUpGameState state);
    void levelUpdated(LightUpGame game, LightUpGameState stateFrom, LightUpGameState stateTo);
    void gameSolved(LightUpGame game);
}
