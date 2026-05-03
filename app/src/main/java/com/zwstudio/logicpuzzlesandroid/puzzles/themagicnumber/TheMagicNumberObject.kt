package com.zwstudio.logicpuzzlesandroid.puzzles.themagicnumber

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TheMagicNumberObject {
    Empty, Fv1, Fv2, Fv3
}

class TheMagicNumberGameMove(val p: Position, var obj: TheMagicNumberObject = TheMagicNumberObject.Empty)
