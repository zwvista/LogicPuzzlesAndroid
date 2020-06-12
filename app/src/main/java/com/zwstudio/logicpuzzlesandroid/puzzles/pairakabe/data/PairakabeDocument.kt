package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeObject
import org.androidannotations.annotations.EBean

@EBean
class PairakabeDocument : GameDocument<PairakabeGame, PairakabeGameMove>() {
    override fun saveMove(move: PairakabeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        PairakabeGameMove(Position(rec.row, rec.col), PairakabeObject.objTypeFromString(rec.strValue1!!))
}