package com.zwstudio.logicgamesandroid.clouds.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class CloudsWallObject extends CloudsObject {
    public LogicGamesHintState state = LogicGamesHintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
