package com.zwstudio.logicpuzzlesandroid.puzzles.numbercrosswords

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class NumberCrosswordsObject {
    Normal, Darken, Marker
}

class NumberCrosswordsGameMove(val p: Position, var obj: NumberCrosswordsObject = NumberCrosswordsObject.Normal)
