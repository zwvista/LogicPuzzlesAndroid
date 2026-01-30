package com.zwstudio.logicpuzzlesandroid.puzzles.culturedbranches

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CulturedBranchesDocument(context: Context) : GameDocument<CulturedBranchesGameMove>(context) {
    override fun saveMove(move: CulturedBranchesGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        CulturedBranchesGameMove(Position(rec.row, rec.col), CulturedBranchesObject.entries[rec.intValue1])
}