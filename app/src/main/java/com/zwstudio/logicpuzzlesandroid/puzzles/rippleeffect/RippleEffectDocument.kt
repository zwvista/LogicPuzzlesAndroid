package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RippleEffectDocument(context: Context) : GameDocument<RippleEffectGameMove>(context) {
    override fun saveMove(move: RippleEffectGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress) =
        RippleEffectGameMove(Position(rec.row, rec.col), rec.intValue1)
}