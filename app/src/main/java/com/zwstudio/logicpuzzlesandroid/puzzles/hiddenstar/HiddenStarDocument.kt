package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstar

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HiddenStarDocument(context: Context) : GameDocument<HiddenStarGameMove>(context) {
    override fun saveMove(move: HiddenStarGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        HiddenStarGameMove(Position(rec.row, rec.col), HiddenStarObject.objFromString(rec.strValue1!!))
}