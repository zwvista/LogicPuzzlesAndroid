package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class PaintTheNurikabeDocument : GameDocument<PaintTheNurikabeGameMove>() {
    override fun saveMove(move: PaintTheNurikabeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        PaintTheNurikabeGameMove(Position(rec.row, rec.col), PaintTheNurikabeObject.values()[rec.intValue1])
}