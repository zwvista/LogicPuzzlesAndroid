package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGame
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerObject
import org.androidannotations.annotations.EBean

@EBean
class GardenerDocument : GameDocument<GardenerGame, GardenerGameMove>() {
    override fun saveMove(move: GardenerGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        GardenerGameMove(Position(rec.row, rec.col), GardenerObject.objFromString(rec.strValue1))
}