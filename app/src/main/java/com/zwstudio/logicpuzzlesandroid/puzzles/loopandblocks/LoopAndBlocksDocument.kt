package com.zwstudio.logicpuzzlesandroid.puzzles.loopandblocks

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LoopAndBlocksDocument(context: Context) : GameDocument<LoopAndBlocksGameMove>(context) {
    override fun saveMove(move: LoopAndBlocksGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        LoopAndBlocksGameMove(Position(rec.row, rec.col), rec.intValue1)
}