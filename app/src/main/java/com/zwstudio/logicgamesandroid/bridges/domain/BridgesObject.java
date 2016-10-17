package com.zwstudio.logicgamesandroid.bridges.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class BridgesObject implements Cloneable {
    @Override
    public BridgesObject clone(){
        try {
            BridgesObject o = (BridgesObject)super.clone();
            return o;
        } catch(CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}
