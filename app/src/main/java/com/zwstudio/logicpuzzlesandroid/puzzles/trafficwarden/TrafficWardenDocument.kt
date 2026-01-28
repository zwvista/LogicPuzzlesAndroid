package com.zwstudio.logicpuzzlesandroid.puzzles.trafficwarden

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TrafficWardenDocument(context: Context) : GameDocument<TrafficWardenGameMove>(context) {
    override fun saveMove(move: TrafficWardenGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
    }

    override fun loadMove(rec: MoveProgress) =
        TrafficWardenGameMove(Position(rec.row, rec.col), rec.intValue1)
}