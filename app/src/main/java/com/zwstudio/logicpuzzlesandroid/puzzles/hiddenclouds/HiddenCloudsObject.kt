package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenclouds

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class HiddenCloudsObject {
    Empty, Marker, Forbidden, Cloud
}

class HiddenCloudsGameMove(val p: Position, var obj: HiddenCloudsObject = HiddenCloudsObject.Empty)
