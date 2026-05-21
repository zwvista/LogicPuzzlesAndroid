package com.zwstudio.logicpuzzlesandroid.puzzles.bentbridges

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BentBridgesDocument(context: Context) : GameDocument<BentBridgesGameMove>(context) {
    override fun saveMove(move: BentBridgesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        BentBridgesGameMove(Position(rec.row, rec.col), rec.intValue1)
}
