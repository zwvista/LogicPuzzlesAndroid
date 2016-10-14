package com.zwstudio.logicgamesandroid.lightup.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class GameObject implements Cloneable {
    public int lightness = 0;
    public abstract String objTypeAsString();
    public static GameObject objTypeFromString(String str) {
        switch (str) {
            case "lightbulb":
                return new LightbulbObject();
            case "marker":
                return new MarkerObject();
            default:
                return new EmptyObject();
        }
    }

    @Override
    public GameObject clone(){
        try {
            GameObject o = (GameObject)super.clone();
            o.lightness = lightness;
            return o;
        } catch(CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}
