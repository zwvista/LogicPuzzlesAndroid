package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain.BattleShipsObject

import org.androidannotations.annotations.EBean

@EBean
open class BattleShipsDocument : GameDocument<BattleShipsGame, BattleShipsGameMove>() {
    override fun saveMove(move: BattleShipsGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.obj!!.ordinal
    }

    override fun loadMove(rec: MoveProgress): BattleShipsGameMove {
        return object : BattleShipsGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = BattleShipsObject.values()[rec.intValue1]
            }
        }
    }
}
