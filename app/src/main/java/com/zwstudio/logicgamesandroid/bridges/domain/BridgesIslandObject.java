package com.zwstudio.logicgamesandroid.bridges.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BridgesIslandObject extends BridgesObject {
    public LogicGamesHintState state = LogicGamesHintState.Normal;
    public Integer[] bridges = {0, 0, 0, 0};
}
