package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BootyIslandDocument(context: Context) : GameDocument<BootyIslandGameMove>(context) {
    override fun saveMove(move: BootyIslandGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        BootyIslandGameMove(Position(rec.row, rec.col), BootyIslandObject.objFromString(rec.strValue1!!))
}
