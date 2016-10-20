package com.zwstudio.logicgamesandroid.lightup.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightUpWallObject extends LightUpObject {
    public LogicGamesHintState state = LogicGamesHintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
    @Override
    public LightUpWallObject clone(){
        LightUpWallObject o = (LightUpWallObject)super.clone();
        o.state = state;
        return o;
    }
}
