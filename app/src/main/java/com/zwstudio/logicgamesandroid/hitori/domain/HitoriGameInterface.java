package com.zwstudio.logicgamesandroid.hitori.domain;

/**
 * Created by TCC-2-9002 on 2016/09/30.
 */

public interface HitoriGameInterface {
    void moveAdded(HitoriGame game, HitoriGameMove move);
    void levelInitilized(HitoriGame game, HitoriGameState state);
    void levelUpdated(HitoriGame game, HitoriGameState stateFrom, HitoriGameState stateTo);
    void gameSolved(HitoriGame game);
}
