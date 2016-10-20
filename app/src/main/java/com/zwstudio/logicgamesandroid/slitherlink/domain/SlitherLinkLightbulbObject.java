package com.zwstudio.logicgamesandroid.slitherlink.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SlitherLinkLightbulbObject extends SlitherLinkObject {
    public enum LightbulbState {
        Normal, Error
    }
    public LightbulbState state = LightbulbState.Normal;
    public String objTypeAsString() {
        return "lightbulb";
    }
    @Override
    public SlitherLinkLightbulbObject clone(){
        SlitherLinkLightbulbObject o = (SlitherLinkLightbulbObject)super.clone();
        o.state = state;
        return o;
    }
}
