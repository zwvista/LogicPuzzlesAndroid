package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LightenUpDocument(context: Context) : GameDocument<LightenUpGameMove>(context) {
    override fun saveMove(move: LightenUpGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.objType.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        LightenUpGameMove(Position(rec.row, rec.col), LightenUpObjectType.entries[rec.intValue1])
}