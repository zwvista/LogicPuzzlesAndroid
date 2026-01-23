package com.zwstudio.logicpuzzlesandroid.puzzles.makethedifference

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MakeTheDifferenceDocument(context: Context) : GameDocument<MakeTheDifferenceGameMove>(context) {
    override fun saveMove(move: MakeTheDifferenceGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        MakeTheDifferenceGameMove(
            Position(rec.row, rec.col),
            rec.intValue1,
            GridLineObject.entries[rec.intValue2]
        )
}
