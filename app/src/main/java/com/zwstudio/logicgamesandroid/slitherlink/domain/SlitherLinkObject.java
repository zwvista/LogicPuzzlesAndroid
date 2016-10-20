package com.zwstudio.logicgamesandroid.slitherlink.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class SlitherLinkObject implements Cloneable {
    public int lightness = 0;
    public abstract String objTypeAsString();
    public static SlitherLinkObject objTypeFromString(String str) {
        switch (str) {
            case "lightbulb":
                return new SlitherLinkLightbulbObject();
            case "marker":
                return new SlitherLinkMarkerObject();
            default:
                return new SlitherLinkEmptyObject();
        }
    }

    @Override
    public SlitherLinkObject clone(){
        try {
            SlitherLinkObject o = (SlitherLinkObject)super.clone();
            o.lightness = lightness;
            return o;
        } catch(CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}
