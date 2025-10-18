package com.zwstudio.logicpuzzlesandroid.puzzles.fields

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FieldsDocument(context: Context) : GameDocument<FieldsGameMove>(context) {
    override fun saveMove(move: FieldsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        FieldsGameMove(Position(rec.row, rec.col), FieldsObject.entries[rec.intValue1])
}