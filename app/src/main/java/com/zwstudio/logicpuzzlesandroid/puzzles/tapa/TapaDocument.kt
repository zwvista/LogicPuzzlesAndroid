package com.zwstudio.logicpuzzlesandroid.puzzles.tapa

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class TapaDocument : GameDocument<TapaGame, TapaGameMove>() {
    override fun saveMove(move: TapaGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TapaGameMove(Position(rec.row, rec.col), TapaObject.objTypeFromString(rec.strValue1!!))
}