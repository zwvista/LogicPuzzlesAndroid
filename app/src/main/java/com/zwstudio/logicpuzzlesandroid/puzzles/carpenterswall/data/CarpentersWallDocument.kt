package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGame
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.CarpentersWallObject
import org.androidannotations.annotations.EBean

@EBean
class CarpentersWallDocument : GameDocument<CarpentersWallGame?, CarpentersWallGameMove?>() {
    protected override fun saveMove(move: CarpentersWallGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.strValue1 = move.obj!!.objAsString()
    }

    override fun loadMove(rec: MoveProgress): CarpentersWallGameMove {
        return object : CarpentersWallGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = CarpentersWallObject.Companion.objTypeFromString(rec.strValue1)
            }
        }
    }
}