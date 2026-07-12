package com.zwstudio.logicpuzzlesandroid.puzzles.mirrorsextended

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MirrorsExtendedDocument(context: Context) : GameDocument<MirrorsExtendedGameMove>(context) {
    override fun saveMove(move: MirrorsExtendedGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        MirrorsExtendedGameMove(Position(rec.row, rec.col), MirrorsExtendedObject.entries[rec.intValue1])
}