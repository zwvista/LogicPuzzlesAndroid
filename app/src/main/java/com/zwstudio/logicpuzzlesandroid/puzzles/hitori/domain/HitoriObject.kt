package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class HitoriObject {
    Normal, Darken, Marker
}

class HitoriGameMove(val p: Position, var obj: HitoriObject = HitoriObject.Normal)
