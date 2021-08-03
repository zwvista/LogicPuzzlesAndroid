package com.zwstudio.logicpuzzlesandroid.puzzles.walls

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WallsDocument(context: Context) : GameDocument<WallsGameMove>(context) {
    override fun saveMove(move: WallsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        WallsGameMove(Position(rec.row, rec.col), WallsObject.objFromString(rec.strValue1!!))
}