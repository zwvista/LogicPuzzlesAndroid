package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ParkLakesDocument(context: Context) : GameDocument<ParkLakesGameMove>(context) {
    override fun saveMove(move: ParkLakesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        ParkLakesGameMove(Position(rec.row, rec.col), ParkLakesObject.objFromString(rec.strValue1!!))
}