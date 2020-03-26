package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGame
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameMove
import org.androidannotations.annotations.EBean

@EBean
open class KropkiDocument : GameDocument<KropkiGame?, KropkiGameMove?>() {
    protected override fun saveMove(move: KropkiGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.obj
    }

    override fun loadMove(rec: MoveProgress): KropkiGameMove {
        return object : KropkiGameMove() {
            init {
                p = Position(rec.row, rec.col)
                obj = rec.intValue1
            }
        }
    }
}