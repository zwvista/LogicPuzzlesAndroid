package com.zwstudio.logicpuzzlesandroid.puzzles.zenlandscaper

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ZenLandscaperDocument(context: Context) : GameDocument<ZenLandscaperGameMove>(context) {
    override fun saveMove(move: ZenLandscaperGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.strValue1 = move.obj.objAsString()
    }

    override fun loadMove(rec: MoveProgress) =
        ZenLandscaperGameMove(Position(rec.row, rec.col), ZenLandscaperObject.objFromString(rec.strValue1!!))
}