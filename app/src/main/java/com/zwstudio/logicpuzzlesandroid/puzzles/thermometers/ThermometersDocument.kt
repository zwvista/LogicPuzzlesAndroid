package com.zwstudio.logicpuzzlesandroid.puzzles.thermometers

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ThermometersDocument(context: Context) : GameDocument<ThermometersGameMove>(context) {
    override fun saveMove(move: ThermometersGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        ThermometersGameMove(Position(rec.row, rec.col), ThermometersObject.objFromString(rec.strValue1!!))
}