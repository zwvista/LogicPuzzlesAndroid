package com.zwstudio.lightupandroid.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightbulbObject extends GameObject {
    public enum LightbulbState {
        Normal, Error
    }
    public LightbulbState state = LightbulbState.Normal;
    public String objTypeAsString() {
        return "lightbulb";
    }
    @Override
    public LightbulbObject clone(){
        LightbulbObject o = (LightbulbObject)super.clone();
        o.state = state;
        return o;
    }
}
