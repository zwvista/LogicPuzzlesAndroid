package com.zwstudio.logicpuzzlesandroid.puzzles.yalooniq

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class YalooniqHint(val num: Int, val dir: Int)

class YalooniqGameMove(val p: Position, var dir: Int = 0)
