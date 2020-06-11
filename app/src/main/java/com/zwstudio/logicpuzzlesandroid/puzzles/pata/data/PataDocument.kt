package com.zwstudio.logicpuzzlesandroid.puzzles.pata.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGame
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain.PataObject
import org.androidannotations.annotations.EBean

@EBean
class PataDocument : GameDocument<PataGame, PataGameMove>() {
    override fun saveMove(move: PataGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        PataGameMove(Position(rec.row, rec.col), PataObject.objTypeFromString(rec.strValue1))
}