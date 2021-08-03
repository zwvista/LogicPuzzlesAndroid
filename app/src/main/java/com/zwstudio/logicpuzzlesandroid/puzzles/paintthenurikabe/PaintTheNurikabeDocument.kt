package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PaintTheNurikabeDocument(context: Context) : GameDocument<PaintTheNurikabeGameMove>(context) {
    override fun saveMove(move: PaintTheNurikabeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        PaintTheNurikabeGameMove(Position(rec.row, rec.col), PaintTheNurikabeObject.values()[rec.intValue1])
}