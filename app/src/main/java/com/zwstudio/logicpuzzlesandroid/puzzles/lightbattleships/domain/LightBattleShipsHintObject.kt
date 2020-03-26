package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class LightBattleShipsHintObject : LightBattleShipsObject() {
    var state = HintState.Normal
    override fun objAsString(): String {
        return "hint"
    }
}