package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGame
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandObject
import org.androidannotations.annotations.EBean

@EBean
open class HolidayIslandDocument : GameDocument<HolidayIslandGame?, HolidayIslandGameMove?>() {
    protected override fun saveMove(move: HolidayIslandGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.strValue1 = move.obj!!.objAsString()
    }

    override fun loadMove(rec: MoveProgress): HolidayIslandGameMove {
        return object : HolidayIslandGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = HolidayIslandObject.Companion.objFromString(rec.strValue1)
            }
        }
    }
}