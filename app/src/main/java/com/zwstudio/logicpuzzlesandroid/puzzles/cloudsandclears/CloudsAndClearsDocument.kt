package com.zwstudio.logicpuzzlesandroid.puzzles.cloudsandclears

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CloudsAndClearsDocument(context: Context) : GameDocument<CloudsAndClearsGameMove>(context) {
    override fun saveMove(move: CloudsAndClearsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        CloudsAndClearsGameMove(Position(rec.row, rec.col), CloudsAndClearsObject.entries[rec.intValue1])
}
