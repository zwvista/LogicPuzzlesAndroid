package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class CloudsObject {
    Empty, Forbidden, Marker, Cloud
}

class CloudsGameMove(val p: Position, var obj: CloudsObject = CloudsObject.Empty)
