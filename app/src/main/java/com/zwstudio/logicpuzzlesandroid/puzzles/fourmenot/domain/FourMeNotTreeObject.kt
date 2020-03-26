package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState

class FourMeNotTreeObject : FourMeNotObject() {
    var state = AllowedObjectState.Normal
    override fun objAsString(): String {
        return "tree"
    }
}