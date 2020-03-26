package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGame
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain.BWTapaObject
import org.androidannotations.annotations.EBean

@EBean
open class BWTapaDocument : GameDocument<BWTapaGame?, BWTapaGameMove?>() {
    protected override fun saveMove(move: BWTapaGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.strValue1 = move.obj!!.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress): BWTapaGameMove {
        return object : BWTapaGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = BWTapaObject.Companion.objTypeFromString(rec.strValue1)
            }
        }
    }
}