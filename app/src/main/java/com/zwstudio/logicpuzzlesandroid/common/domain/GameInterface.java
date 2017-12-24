package com.zwstudio.logicpuzzlesandroid.common.domain;

public interface GameInterface<G extends Game<G, GM, GS>, GM, GS extends GameState> {
    void moveAdded(G game, GM move);
    void levelInitilized(G game, GS state);
    void levelUpdated(G game, GS stateFrom, GS stateTo);
    void gameSolved(G game);
}
