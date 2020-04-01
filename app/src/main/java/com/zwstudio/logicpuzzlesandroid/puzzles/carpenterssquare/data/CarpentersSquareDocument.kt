package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGame
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGameMove
import org.androidannotations.annotations.EBean

@EBean
class CarpentersSquareDocument : GameDocument<CarpentersSquareGame?, CarpentersSquareGameMove?>() {
    protected override fun saveMove(move: CarpentersSquareGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj!!.ordinal
    }

    override fun loadMove(rec: MoveProgress): CarpentersSquareGameMove {
        return object : CarpentersSquareGameMove() {
            init {
                p = Position(rec.row, rec.col)
                dir = rec.intValue1
                obj = GridLineObject.values()[rec.intValue2]
            }
        }
    }
}