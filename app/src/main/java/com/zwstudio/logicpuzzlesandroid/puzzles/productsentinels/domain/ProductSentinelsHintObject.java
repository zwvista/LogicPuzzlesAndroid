package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class ProductSentinelsHintObject extends ProductSentinelsObject {
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hint";
    }
}
