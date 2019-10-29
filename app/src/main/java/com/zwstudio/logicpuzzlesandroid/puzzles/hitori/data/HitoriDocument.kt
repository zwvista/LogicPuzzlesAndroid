package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGame
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriObject

import org.androidannotations.annotations.EBean

@EBean
open class HitoriDocument : GameDocument<HitoriGame, HitoriGameMove>() {
    override fun saveMove(move: HitoriGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        HitoriGameMove(Position(rec.row, rec.col), HitoriObject.values()[rec.intValue1])
}
