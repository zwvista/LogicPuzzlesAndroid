package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FutoshikiDocument(context: Context) : GameDocument<FutoshikiGameMove>(context) {
    override fun saveMove(move: FutoshikiGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.toString()
    }

    override fun loadMove(rec: MoveProgress) =
        FutoshikiGameMove(Position(rec.row, rec.col), rec.strValue1!![0])
}