package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class HolidayIslandObject {
    Empty, Forbidden, Marker, Hint, Water
}

class HolidayIslandGameMove(val p: Position, var obj: HolidayIslandObject = HolidayIslandObject.Empty)
