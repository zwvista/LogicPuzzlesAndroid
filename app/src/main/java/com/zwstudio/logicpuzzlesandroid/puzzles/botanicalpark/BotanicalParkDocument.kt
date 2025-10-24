package com.zwstudio.logicpuzzlesandroid.puzzles.botanicalpark

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BotanicalParkDocument(context: Context) : GameDocument<BotanicalParkGameMove>(context) {
    override fun saveMove(move: BotanicalParkGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        BotanicalParkGameMove(Position(rec.row, rec.col), BotanicalParkObject.objFromString(rec.strValue1!!))
}