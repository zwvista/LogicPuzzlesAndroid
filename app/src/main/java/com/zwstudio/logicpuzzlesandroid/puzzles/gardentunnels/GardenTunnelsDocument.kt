package com.zwstudio.logicpuzzlesandroid.puzzles.gardentunnels

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GardenTunnelsDocument(context: Context) : GameDocument<GardenTunnelsGameMove>(context) {
    override fun saveMove(move: GardenTunnelsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        GardenTunnelsGameMove(Position(rec.row, rec.col), rec.intValue1)
}