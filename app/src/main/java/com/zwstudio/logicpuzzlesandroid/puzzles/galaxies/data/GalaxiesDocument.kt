package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGame
import com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain.GalaxiesGameMove
import org.androidannotations.annotations.EBean

@EBean
class GalaxiesDocument : GameDocument<GalaxiesGame, GalaxiesGameMove>() {
    override fun saveMove(move: GalaxiesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        GalaxiesGameMove(Position(rec.row, rec.col), rec.intValue1, GridLineObject.values()[rec.intValue2])
}