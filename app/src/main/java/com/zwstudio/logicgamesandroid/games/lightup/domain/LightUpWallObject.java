package com.zwstudio.logicgamesandroid.games.lightup.domain;

import com.zwstudio.logicgamesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightUpWallObject extends LightUpObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
