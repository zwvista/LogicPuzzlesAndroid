package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsObject
import org.androidannotations.annotations.EBean

@EBean
class TapaIslandsDocument : GameDocument<TapaIslandsGame, TapaIslandsGameMove>() {
    override fun saveMove(move: TapaIslandsGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objTypeAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        TapaIslandsGameMove(Position(rec.row, rec.col), TapaIslandsObject.objTypeFromString(rec.strValue1))
}