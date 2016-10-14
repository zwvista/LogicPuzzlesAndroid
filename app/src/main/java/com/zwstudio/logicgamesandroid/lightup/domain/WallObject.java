package com.zwstudio.logicgamesandroid.lightup.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class WallObject extends GameObject {
    public enum WallState {
        Normal, Complete, Error
    }
    public WallState state = WallState.Normal;
    public int lightbulbs = 0;
    public String objTypeAsString() {
        return "wall";
    }
    @Override
    public WallObject clone(){
        WallObject o = (WallObject)super.clone();
        o.state = state;
        o.lightbulbs = lightbulbs;
        return o;
    }
}
