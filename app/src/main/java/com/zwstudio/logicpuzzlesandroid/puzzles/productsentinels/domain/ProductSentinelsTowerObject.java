package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class ProductSentinelsTowerObject extends ProductSentinelsObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tower";
    }
}
