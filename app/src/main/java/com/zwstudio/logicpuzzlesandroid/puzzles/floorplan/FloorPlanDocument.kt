package com.zwstudio.logicpuzzlesandroid.puzzles.floorplan

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FloorPlanDocument(context: Context) : GameDocument<FloorPlanGameMove>(context) {
    override fun saveMove(move: FloorPlanGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        FloorPlanGameMove(Position(rec.row, rec.col), rec.intValue1)
}