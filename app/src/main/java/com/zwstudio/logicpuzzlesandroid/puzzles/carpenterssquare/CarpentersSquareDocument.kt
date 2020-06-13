package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import org.androidannotations.annotations.EBean

@EBean
class CarpentersSquareDocument : GameDocument<CarpentersSquareGame, CarpentersSquareGameMove>() {
    override fun saveMove(move: CarpentersSquareGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        CarpentersSquareGameMove(Position(rec.row, rec.col), rec.intValue1, GridLineObject.values()[rec.intValue2])
}