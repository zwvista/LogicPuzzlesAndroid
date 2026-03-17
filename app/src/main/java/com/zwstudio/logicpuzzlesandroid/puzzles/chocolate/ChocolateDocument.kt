package com.zwstudio.logicpuzzlesandroid.puzzles.chocolate

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ChocolateDocument(context: Context) : GameDocument<ChocolateGameMove>(context) {
    override fun saveMove(move: ChocolateGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        ChocolateGameMove(Position(rec.row, rec.col), ChocolateObject.entries[rec.intValue1])
}