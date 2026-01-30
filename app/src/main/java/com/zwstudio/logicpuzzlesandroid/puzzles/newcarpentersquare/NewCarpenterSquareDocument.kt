package com.zwstudio.logicpuzzlesandroid.puzzles.newcarpentersquare

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NewCarpenterSquareDocument(context: Context) : GameDocument<NewCarpenterSquareGameMove>(context) {
    override fun saveMove(move: NewCarpenterSquareGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        NewCarpenterSquareGameMove(Position(rec.row, rec.col), rec.intValue1, GridLineObject.entries[rec.intValue2])
}