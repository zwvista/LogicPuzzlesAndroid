package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeObject
import org.androidannotations.annotations.EBean

@EBean
class NurikabeDocument : GameDocument<NurikabeGame, NurikabeGameMove>() {
    override fun saveMove(move: NurikabeGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        NurikabeGameMove(Position(rec.row, rec.col), NurikabeObject.objTypeFromString(rec.strValue1!!))
}