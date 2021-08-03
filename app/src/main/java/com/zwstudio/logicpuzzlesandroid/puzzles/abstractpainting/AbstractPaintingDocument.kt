package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class AbstractPaintingDocument(context: Context) : GameDocument<AbstractPaintingGameMove>(context) {
    override fun saveMove(move: AbstractPaintingGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        AbstractPaintingGameMove(Position(rec.row, rec.col), AbstractPaintingObject.values()[rec.intValue1])
}
