package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadblocks

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class CrossroadBlocksHint(val isBlack: Boolean, val num: Int, val dir: Int)

class CrossroadBlocksGameMove(val p: Position, var dir: Int = 0)
