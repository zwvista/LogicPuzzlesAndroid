package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HolidayIslandDocument(context: Context) : GameDocument<HolidayIslandGameMove>(context) {
    override fun saveMove(move: HolidayIslandGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        HolidayIslandGameMove(Position(rec.row, rec.col), HolidayIslandObject.objFromString(rec.strValue1!!))
}