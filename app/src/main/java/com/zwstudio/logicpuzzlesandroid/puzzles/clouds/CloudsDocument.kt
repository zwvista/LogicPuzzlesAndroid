package com.zwstudio.logicpuzzlesandroid.puzzles.clouds

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CloudsDocument(context: Context) : GameDocument<CloudsGameMove>(context) {
    override fun saveMove(move: CloudsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        CloudsGameMove(Position(rec.row, rec.col), CloudsObject.values()[rec.intValue1])
}