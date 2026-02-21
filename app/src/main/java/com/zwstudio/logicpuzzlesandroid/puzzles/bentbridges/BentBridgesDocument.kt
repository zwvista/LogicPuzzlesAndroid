package com.zwstudio.logicpuzzlesandroid.puzzles.bentbridges

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BentBridgesDocument(context: Context) : GameDocument<BentBridgesGameMove>(context) {
    override fun saveMove(move: BentBridgesGameMove, rec: MoveProgress) {
        rec.row = move.pFrom.row
        rec.col = move.pFrom.col
        rec.row2 = move.pTo.row
        rec.col2 = move.pTo.col
    }

    override fun loadMove(rec: MoveProgress) =
        BentBridgesGameMove(Position(rec.row, rec.col), Position(rec.row2, rec.col2))
}
