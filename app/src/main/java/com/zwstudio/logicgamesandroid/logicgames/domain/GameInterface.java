package com.zwstudio.logicgamesandroid.logicgames.domain;

/**
 * Created by TCC-2-9002 on 2016/10/27.
 */

public interface GameInterface<G extends Game, GM, GS extends GameState> {
    void moveAdded(G game, GM move);
    void levelInitilized(G game, GS state);
    void levelUpdated(G game, GS stateFrom, GS stateTo);
    void gameSolved(G game);
}
