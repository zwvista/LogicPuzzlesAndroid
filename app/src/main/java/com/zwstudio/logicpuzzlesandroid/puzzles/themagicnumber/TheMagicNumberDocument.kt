package com.zwstudio.logicpuzzlesandroid.puzzles.themagicnumber

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TheMagicNumberDocument(context: Context) : GameDocument<TheMagicNumberGameMove>(context) {
    override fun saveMove(move: TheMagicNumberGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        TheMagicNumberGameMove(Position(rec.row, rec.col), TheMagicNumberObject.entries[rec.intValue1])
}