package com.zwstudio.logicpuzzlesandroid.puzzles.thecityrises

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TheCityRisesDocument(context: Context) : GameDocument<TheCityRisesGameMove>(context) {
    override fun saveMove(move: TheCityRisesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TheCityRisesGameMove(Position(rec.row, rec.col), TheCityRisesObject.objFromString(rec.strValue1!!))
}