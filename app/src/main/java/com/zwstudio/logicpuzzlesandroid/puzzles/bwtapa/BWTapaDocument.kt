package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class BWTapaDocument : GameDocument<BWTapaGame, BWTapaGameMove>() {
    override fun saveMove(move: BWTapaGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        BWTapaGameMove(Position(rec.row, rec.col), BWTapaObject.objTypeFromString(rec.strValue1!!))
}
