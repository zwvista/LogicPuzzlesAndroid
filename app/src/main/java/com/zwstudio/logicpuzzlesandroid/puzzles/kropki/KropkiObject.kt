package com.zwstudio.logicpuzzlesandroid.puzzles.kropki

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class KropkiHint {
    None, Consecutive, Twice
}

class KropkiGameMove(val p: Position, var obj: Int = 0)
