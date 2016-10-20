package com.zwstudio.logicgamesandroid.slitherlink.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SlitherLinkWallObject extends SlitherLinkObject {
    public enum WallState {
        Normal, Complete, Error
    }
    public WallState state = WallState.Normal;
    public int lightbulbs = 0;
    public String objTypeAsString() {
        return "wall";
    }
    @Override
    public SlitherLinkWallObject clone(){
        SlitherLinkWallObject o = (SlitherLinkWallObject)super.clone();
        o.state = state;
        o.lightbulbs = lightbulbs;
        return o;
    }
}
