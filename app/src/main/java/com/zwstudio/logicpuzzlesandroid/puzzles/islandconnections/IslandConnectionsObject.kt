package com.zwstudio.logicpuzzlesandroid.puzzles.islandconnections

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class IslandConnectionsObject

object IslandConnectionsBridgeObject : IslandConnectionsObject()

object IslandConnectionsEmptyObject : IslandConnectionsObject()

class IslandConnectionsIslandObject : IslandConnectionsObject() {
    var state = HintState.Normal
    var bridges = arrayOf(0, 0, 0, 0)
}

class IslandConnectionsIslandInfo(var bridges: Int) {
    var neighbors = arrayOfNulls<Position>(4)
}

class IslandConnectionsGameMove(var pFrom: Position, var pTo: Position)
