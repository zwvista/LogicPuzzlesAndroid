package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadblocks

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CrossroadBlocksDocument(context: Context) : GameDocument<CrossroadBlocksGameMove>(context) {
    override fun saveMove(move: CrossroadBlocksGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        CrossroadBlocksGameMove(Position(rec.row, rec.col), rec.intValue1)
}