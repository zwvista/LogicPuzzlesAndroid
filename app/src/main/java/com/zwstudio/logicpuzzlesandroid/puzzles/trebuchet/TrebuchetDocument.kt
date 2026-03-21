package com.zwstudio.logicpuzzlesandroid.puzzles.trebuchet

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TrebuchetDocument(context: Context) : GameDocument<TrebuchetGameMove>(context) {
    override fun saveMove(move: TrebuchetGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        TrebuchetGameMove(Position(rec.row, rec.col), TrebuchetObject.entries[rec.intValue1])
}