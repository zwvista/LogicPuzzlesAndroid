package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WallSentinelsDocument(context: Context) : GameDocument<WallSentinelsGameMove>(context) {
    override fun saveMove(move: WallSentinelsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        WallSentinelsGameMove(Position(rec.row, rec.col), WallSentinelsObject.objFromString(rec.strValue1!!))
}
