package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class TapARowHintObject : TapARowObject() {
    var state = HintState.Normal
    override fun objTypeAsString(): String {
        return "hint"
    }
}