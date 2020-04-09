package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class KropkiHint {
    None, Consecutive, Twice
}

class KropkiGameMove(val p: Position, var obj: Int = 0)
