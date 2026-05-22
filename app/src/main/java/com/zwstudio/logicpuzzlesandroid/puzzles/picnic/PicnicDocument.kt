package com.zwstudio.logicpuzzlesandroid.puzzles.picnic

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PicnicDocument(context: Context) : GameDocument<PicnicGameMove>(context) {
    override fun saveMove(move: PicnicGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        PicnicGameMove(Position(rec.row, rec.col), rec.intValue1)
}