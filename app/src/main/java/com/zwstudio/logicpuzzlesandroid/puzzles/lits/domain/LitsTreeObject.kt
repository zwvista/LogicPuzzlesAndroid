package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState

class LitsTreeObject : LitsObject() {
    var state = AllowedObjectState.Normal
    override fun objAsString(): String {
        return "tree"
    }
}