package com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGame
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGameMove
import org.androidannotations.annotations.EBean

@EBean
open class BoxItAroundDocument : GameDocument<BoxItAroundGame?, BoxItAroundGameMove?>() {
    protected override fun saveMove(move: BoxItAroundGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj!!.ordinal
    }

    override fun loadMove(rec: MoveProgress): BoxItAroundGameMove {
        return object : BoxItAroundGameMove() {
            init {
                p = Position(rec.row, rec.col)
                dir = rec.intValue1
                obj = GridLineObject.values()[rec.intValue2]
            }
        }
    }
}