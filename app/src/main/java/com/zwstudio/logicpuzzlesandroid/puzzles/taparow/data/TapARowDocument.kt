package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGame
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowObject
import org.androidannotations.annotations.EBean

@EBean
class TapARowDocument : GameDocument<TapARowGame, TapARowGameMove>() {
    override fun saveMove(move: TapARowGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TapARowGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = TapARowObject.objTypeFromString(rec.strValue1)
            }
        }
    }
}