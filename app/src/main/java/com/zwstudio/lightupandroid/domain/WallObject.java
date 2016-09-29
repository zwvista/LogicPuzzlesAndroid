package com.zwstudio.lightupandroid.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class WallObject extends GameObject {
    public enum WallState {
        Normal, Complete, Error
    }
    public WallState state = WallState.Normal;
    public int lightbulbs = 0;
}
