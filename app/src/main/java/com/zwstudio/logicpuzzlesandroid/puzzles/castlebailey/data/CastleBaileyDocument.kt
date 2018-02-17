package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyGame
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyObject

import org.androidannotations.annotations.EBean

@EBean
class CastleBaileyDocument : GameDocument<CastleBaileyGame, CastleBaileyGameMove>() {
    override fun saveMove(move: CastleBaileyGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        CastleBaileyGameMove(Position(rec.row, rec.col), CastleBaileyObject.values()[rec.intValue1])
}
