package com.zwstudio.logicpuzzlesandroid.puzzles.fields

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class FieldsObject {
    Empty, Meadow, Soil
}

class FieldsGameMove(val p: Position, var obj: FieldsObject = FieldsObject.Empty)
