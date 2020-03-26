package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState

class GardenerTreeObject : GardenerObject() {
    var state = AllowedObjectState.Normal
    override fun objAsString(): String {
        return "tree"
    }
}