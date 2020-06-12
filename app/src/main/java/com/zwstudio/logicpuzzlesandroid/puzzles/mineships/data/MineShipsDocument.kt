package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain.MineShipsObject
import org.androidannotations.annotations.EBean

@EBean
class MineShipsDocument : GameDocument<MineShipsGame, MineShipsGameMove>() {
    override fun saveMove(move: MineShipsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        MineShipsGameMove(Position(rec.row, rec.col), MineShipsObject.objFromString(rec.strValue1!!))
}