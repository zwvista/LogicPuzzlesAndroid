package com.zwstudio.logicgamesandroid.clouds.domain;

/**
 * Created by TCC-2-9002 on 2016/09/30.
 */

public interface CloudsGameInterface {
    void moveAdded(CloudsGame game, CloudsGameMove move);
    void levelInitilized(CloudsGame game, CloudsGameState state);
    void levelUpdated(CloudsGame game, CloudsGameState stateFrom, CloudsGameState stateTo);
    void gameSolved(CloudsGame game);
}
