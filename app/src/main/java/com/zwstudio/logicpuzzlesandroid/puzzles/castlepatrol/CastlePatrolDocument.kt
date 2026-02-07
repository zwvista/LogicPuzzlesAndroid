package com.zwstudio.logicpuzzlesandroid.puzzles.castlepatrol

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CastlePatrolDocument(context: Context) : GameDocument<CastlePatrolGameMove>(context) {
    override fun saveMove(move: CastlePatrolGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        CastlePatrolGameMove(Position(rec.row, rec.col), CastlePatrolObject.objTypeFromString(rec.strValue1!!))
}