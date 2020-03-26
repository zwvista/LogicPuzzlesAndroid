package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGame
import com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain.KakuroGameMove
import org.androidannotations.annotations.EBean

@EBean
open class KakuroDocument : GameDocument<KakuroGame?, KakuroGameMove?>() {
    protected override fun saveMove(move: KakuroGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress): KakuroGameMove {
        return object : KakuroGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = rec.intValue1
            }
        }
    }
}