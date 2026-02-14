package com.zwstudio.logicpuzzlesandroid.puzzles.cloudsandclears

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class CloudsAndClearsObject {
    Empty, Marker,
    Cloud
}

class CloudsAndClearsGameMove(val p: Position, var obj: CloudsAndClearsObject = CloudsAndClearsObject.Empty)
