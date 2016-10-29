package com.zwstudio.logicpuzzlesandroid.games.bridges.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BridgesIslandObject extends BridgesObject {
    public HintState state = HintState.Normal;
    public Integer[] bridges = {0, 0, 0, 0};
}
