package com.zwstudio.logicpuzzlesandroid.puzzles.archipelago

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ArchipelagoDocument(context: Context) : GameDocument<ArchipelagoGameMove>(context) {
    override fun saveMove(move: ArchipelagoGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        ArchipelagoGameMove(Position(rec.row, rec.col), ArchipelagoObject.objFromString(rec.strValue1!!))
}