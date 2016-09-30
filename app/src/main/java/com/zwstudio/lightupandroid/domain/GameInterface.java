package com.zwstudio.lightupandroid.domain;

/**
 * Created by TCC-2-9002 on 2016/09/30.
 */

public interface GameInterface {
    void moveAdded(Game game, GameMove move);
    void levelInitilized(Game game, GameState state);
    void levelUpdated(Game game, GameState stateFrom, GameState stateTo);
    void gameSolved(Game game);
}
