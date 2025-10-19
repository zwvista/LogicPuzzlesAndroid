package com.zwstudio.logicpuzzlesandroid.puzzles.gems

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class GemsObject {
    Empty, Gem, Marker, Pebble
}

class GemsGameMove(val p: Position, var obj: GemsObject = GemsObject.Empty)
