package com.zwstudio.logicgamesandroid.lightup.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightUpLightbulbObject extends LightUpObject {
    public enum LightbulbState {
        Normal, Error
    }
    public LightbulbState state = LightbulbState.Normal;
    public String objTypeAsString() {
        return "lightbulb";
    }
    @Override
    public LightUpLightbulbObject clone(){
        LightUpLightbulbObject o = (LightUpLightbulbObject)super.clone();
        o.state = state;
        return o;
    }
}
