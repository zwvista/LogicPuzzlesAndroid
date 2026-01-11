package com.zwstudio.logicpuzzlesandroid.puzzles.underground

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class UndergroundDocument(context: Context) : GameDocument<UndergroundGameMove>(context) {
    override fun saveMove(move: UndergroundGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        UndergroundGameMove(Position(rec.row, rec.col), UndergroundObject.entries[rec.intValue1])
}