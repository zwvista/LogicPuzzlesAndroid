package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain.OrchardsObject
import org.androidannotations.annotations.EBean

@EBean
class OrchardsDocument : GameDocument<OrchardsGame, OrchardsGameMove>() {
    override fun saveMove(move: OrchardsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        OrchardsGameMove(Position(rec.row, rec.col), OrchardsObject.objFromString(rec.strValue1!!))
}