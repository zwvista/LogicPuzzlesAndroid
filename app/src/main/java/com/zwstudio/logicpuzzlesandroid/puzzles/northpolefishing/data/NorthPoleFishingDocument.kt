package com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing.domain.NorthPoleFishingGame
import com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing.domain.NorthPoleFishingGameMove
import org.androidannotations.annotations.EBean

@EBean
class NorthPoleFishingDocument : GameDocument<NorthPoleFishingGame, NorthPoleFishingGameMove>() {
    override fun saveMove(move: NorthPoleFishingGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        NorthPoleFishingGameMove(Position(rec.row, rec.col), rec.intValue1, GridLineObject.values()[rec.intValue2])
}
