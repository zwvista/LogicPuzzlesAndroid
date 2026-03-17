package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CarpentersWallDocument(context: Context) : GameDocument<CarpentersWallGameMove>(context) {
    override fun saveMove(move: CarpentersWallGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        CarpentersWallGameMove(Position(rec.row, rec.col), CarpentersWallObject.entries[rec.intValue1])
}