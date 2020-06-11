package com.zwstudio.logicpuzzlesandroid.puzzles.overunder.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain.OverUnderGame
import com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain.OverUnderGameMove
import org.androidannotations.annotations.EBean

@EBean
class OverUnderDocument : GameDocument<OverUnderGame, OverUnderGameMove>() {
    override fun saveMove(move: OverUnderGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        OverUnderGameMove(Position(rec.row, rec.col), rec.intValue1, GridLineObject.values()[rec.intValue2])
}