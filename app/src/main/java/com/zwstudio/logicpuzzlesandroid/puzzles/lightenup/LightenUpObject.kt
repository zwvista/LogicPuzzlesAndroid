package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class LightenUpObjectType {
    Empty, Marker, Lightbulb, Wall
}

data class LightenUpObject(var objType: LightenUpObjectType = LightenUpObjectType.Empty, var lightness: Int = 0)

class LightenUpGameMove(val p: Position, var objType: LightenUpObjectType = LightenUpObjectType.Empty)
