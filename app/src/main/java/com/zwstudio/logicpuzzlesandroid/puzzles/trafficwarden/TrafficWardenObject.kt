package com.zwstudio.logicpuzzlesandroid.puzzles.trafficwarden

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class TrafficWardenHint(val light: Char, val len: Int)

class TrafficWardenGameMove(val p: Position, var dir: Int = 0)
