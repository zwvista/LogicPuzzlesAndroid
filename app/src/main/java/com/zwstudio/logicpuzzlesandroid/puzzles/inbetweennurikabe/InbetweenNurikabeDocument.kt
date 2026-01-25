package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweennurikabe

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class InbetweenNurikabeDocument(context: Context) : GameDocument<InbetweenNurikabeGameMove>(context) {
    override fun saveMove(move: InbetweenNurikabeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        InbetweenNurikabeGameMove(Position(rec.row, rec.col), InbetweenNurikabeObject.entries[rec.intValue1])
}