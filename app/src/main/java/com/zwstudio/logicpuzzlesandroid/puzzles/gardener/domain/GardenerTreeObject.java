package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class GardenerTreeObject extends GardenerObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
