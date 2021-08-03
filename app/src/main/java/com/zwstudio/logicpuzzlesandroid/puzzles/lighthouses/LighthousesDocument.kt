package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LighthousesDocument(context: Context) : GameDocument<LighthousesGameMove>(context) {
    override fun saveMove(move: LighthousesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        LighthousesGameMove(Position(rec.row, rec.col), LighthousesObject.objFromString(rec.strValue1!!))
}