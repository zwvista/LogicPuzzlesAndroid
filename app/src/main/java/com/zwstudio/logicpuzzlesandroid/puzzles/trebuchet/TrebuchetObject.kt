package com.zwstudio.logicpuzzlesandroid.puzzles.trebuchet

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TrebuchetObject {
    Empty, Forbidden, Hint, Marker, Target
}

class TrebuchetGameMove(val p: Position, var obj: TrebuchetObject = TrebuchetObject.Empty)
