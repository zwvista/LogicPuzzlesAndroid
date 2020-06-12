package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain.TapDifferentlyObject
import org.androidannotations.annotations.EBean

@EBean
class TapDifferentlyDocument : GameDocument<TapDifferentlyGame, TapDifferentlyGameMove>() {
    override fun saveMove(move: TapDifferentlyGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TapDifferentlyGameMove(Position(rec.row, rec.col), TapDifferentlyObject.objTypeFromString(rec.strValue1!!))
}