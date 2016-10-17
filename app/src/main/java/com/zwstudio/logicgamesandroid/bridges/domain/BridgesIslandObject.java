package com.zwstudio.logicgamesandroid.bridges.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BridgesIslandObject extends BridgesObject {
    public enum IslandState {
        Normal, Complete, Error
    }
    public IslandState state = IslandState.Normal;
    public Integer[] bridges = {0, 0, 0, 0};
    @Override
    public BridgesIslandObject clone(){
        BridgesIslandObject o = (BridgesIslandObject)super.clone();
        o.state = state;
        for (int i = 0; i < 4; i++)
            o.bridges[i] = bridges[i];
        return o;
    }
}
