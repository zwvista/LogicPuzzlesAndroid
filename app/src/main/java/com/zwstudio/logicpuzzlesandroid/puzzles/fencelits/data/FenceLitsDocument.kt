package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain.FenceLitsGameMove
import org.androidannotations.annotations.EBean

@EBean
class FenceLitsDocument : GameDocument<FenceLitsGame, FenceLitsGameMove>() {
    override fun saveMove(move: FenceLitsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        FenceLitsGameMove(Position(rec.row, rec.col), rec.intValue1, GridLineObject.values()[rec.intValue2])
}