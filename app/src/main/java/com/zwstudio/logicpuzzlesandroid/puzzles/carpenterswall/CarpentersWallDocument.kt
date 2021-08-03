package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CarpentersWallDocument(context: Context) : GameDocument<CarpentersWallGameMove>(context) {
    override fun saveMove(move: CarpentersWallGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        CarpentersWallGameMove(Position(rec.row, rec.col), CarpentersWallObject.objTypeFromString(rec.strValue1!!))
}