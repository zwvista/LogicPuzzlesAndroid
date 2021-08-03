package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TennerGridDocument(context: Context) : GameDocument<TennerGridGameMove>(context) {
    override fun saveMove(move: TennerGridGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        TennerGridGameMove(Position(rec.row, rec.col), rec.intValue1)
}