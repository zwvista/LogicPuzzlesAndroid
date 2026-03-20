package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class PowerGridObject {
    Empty, Forbidden, Marker, Post
}

class PowerGridGameMove(val p: Position, var obj: PowerGridObject = PowerGridObject.Empty)
