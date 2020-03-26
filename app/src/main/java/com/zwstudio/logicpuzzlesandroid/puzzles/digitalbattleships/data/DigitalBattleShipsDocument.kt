package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsObject
import org.androidannotations.annotations.EBean

@EBean
open class DigitalBattleShipsDocument : GameDocument<DigitalBattleShipsGame?, DigitalBattleShipsGameMove?>() {
    protected override fun saveMove(move: DigitalBattleShipsGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.obj!!.ordinal
    }

    override fun loadMove(rec: MoveProgress): DigitalBattleShipsGameMove {
        return object : DigitalBattleShipsGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = DigitalBattleShipsObject.values()[rec.intValue1]
            }
        }
    }
}