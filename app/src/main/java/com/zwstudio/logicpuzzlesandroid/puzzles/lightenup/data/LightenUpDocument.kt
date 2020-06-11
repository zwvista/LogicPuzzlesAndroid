package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpObject
import org.androidannotations.annotations.EBean

@EBean
class LightenUpDocument : GameDocument<LightenUpGame, LightenUpGameMove>() {
    protected override fun saveMove(move: LightenUpGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress): LightenUpGameMove {
        return object : LightenUpGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = LightenUpObject.objTypeFromString(rec.strValue1)
            }
        }
    }
}