package com.zwstudio.logicpuzzlesandroid.puzzles.gardener

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GardenerDocument(context: Context) : GameDocument<GardenerGameMove>(context) {
    override fun saveMove(move: GardenerGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        GardenerGameMove(Position(rec.row, rec.col), GardenerObject.objFromString(rec.strValue1!!))
}