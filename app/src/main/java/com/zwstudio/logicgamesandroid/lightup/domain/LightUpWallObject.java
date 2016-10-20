package com.zwstudio.logicgamesandroid.lightup.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightUpWallObject extends LightUpObject {
    public enum WallState {
        Normal, Complete, Error
    }
    public WallState state = WallState.Normal;
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
