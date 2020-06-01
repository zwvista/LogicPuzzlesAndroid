package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain.LightBattleShipsObject
import org.androidannotations.annotations.EBean

@EBean
class LightBattleShipsDocument : GameDocument<LightBattleShipsGame?, LightBattleShipsGameMove?>() {
    protected override fun saveMove(move: LightBattleShipsGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.strValue1 = move.obj!!.objAsString()
    }

    override fun loadMove(rec: MoveProgress): LightBattleShipsGameMove {
        return object : LightBattleShipsGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = LightBattleShipsObject.objFromString(rec.strValue1)
            }
        }
    }
}