package com.zwstudio.logicpuzzlesandroid.puzzles.trafficwardenrevenge

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class TrafficWardenRevengeHint(val light: Char, val len: Int)

class TrafficWardenRevengeGameMove(val p: Position, var dir: Int = 0)
