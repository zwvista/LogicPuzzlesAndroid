package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenclouds

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HiddenCloudsDocument(context: Context) : GameDocument<HiddenCloudsGameMove>(context) {
    override fun saveMove(move: HiddenCloudsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        HiddenCloudsGameMove(Position(rec.row, rec.col), HiddenCloudsObject.objFromString(rec.strValue1!!))
}