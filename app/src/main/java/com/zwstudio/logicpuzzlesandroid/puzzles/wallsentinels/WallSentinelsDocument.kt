package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WallSentinelsDocument(context: Context) : GameDocument<WallSentinelsGameMove>(context) {
    override fun saveMove(move: WallSentinelsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        WallSentinelsGameMove(Position(rec.row, rec.col), WallSentinelsObject.entries[rec.intValue1])
}
