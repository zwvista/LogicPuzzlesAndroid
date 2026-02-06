package com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CrosstownTrafficDocument(context: Context) : GameDocument<CrosstownTrafficGameMove>(context) {
    override fun saveMove(move: CrosstownTrafficGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        CrosstownTrafficGameMove(Position(rec.row, rec.col), CrosstownTrafficObject.objFromString(rec.strValue1!!))
}