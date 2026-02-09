package com.zwstudio.logicpuzzlesandroid.puzzles.culturetrip

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CultureTripDocument(context: Context) : GameDocument<CultureTripGameMove>(context) {
    override fun saveMove(move: CultureTripGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        CultureTripGameMove(Position(rec.row, rec.col), rec.intValue1)
}