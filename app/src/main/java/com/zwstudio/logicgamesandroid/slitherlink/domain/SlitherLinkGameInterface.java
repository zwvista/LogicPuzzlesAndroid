package com.zwstudio.logicgamesandroid.slitherlink.domain;

/**
 * Created by TCC-2-9002 on 2016/09/30.
 */

public interface SlitherLinkGameInterface {
    void moveAdded(SlitherLinkGame game, SlitherLinkGameMove move);
    void levelInitilized(SlitherLinkGame game, SlitherLinkGameState state);
    void levelUpdated(SlitherLinkGame game, SlitherLinkGameState stateFrom, SlitherLinkGameState stateTo);
    void gameSolved(SlitherLinkGame game);
}
