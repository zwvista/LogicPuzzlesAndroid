package com.zwstudio.logicpuzzlesandroid.puzzles.bentbridges

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class BentBridgesObject

object BentBridgesBridgeObject : BentBridgesObject()

object BentBridgesEmptyObject : BentBridgesObject()

class BentBridgesIslandObject : BentBridgesObject() {
    var state = HintState.Normal
    var bridges = arrayOf(0, 0, 0, 0)
}

class BentBridgesIslandInfo(var bridges: Int) {
    var neighbors = arrayOfNulls<Position>(4)
}

class BentBridgesGameMove(var pFrom: Position, var pTo: Position)
