package com.zwstudio.logicpuzzlesandroid.puzzles.bridges

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class BridgesObject

class BridgesBridgeObject : BridgesObject()

class BridgesEmptyObject : BridgesObject()

class BridgesIslandObject : BridgesObject() {
    var state = HintState.Normal
    var bridges = arrayOf(0, 0, 0, 0)
}

class BridgesIslandInfo {
    var bridges = 0
    var neighbors = arrayOfNulls<Position>(4)
}

class BridgesGameMove(var pFrom: Position, var pTo: Position)
