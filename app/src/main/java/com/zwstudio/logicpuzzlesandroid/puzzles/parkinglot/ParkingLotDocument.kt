package com.zwstudio.logicpuzzlesandroid.puzzles.parkinglot

import android.content.Context
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ParkingLotDocument(context: Context) : GameDocument<ParkingLotGameMove>(context) {
    override fun saveMove(move: ParkingLotGameMove, rec: MoveProgress) {
        rec.row = move.p.row
        rec.col = move.p.col
        rec.intValue1 = move.obj.ordinal
    }

    override fun loadMove(rec: MoveProgress) =
        ParkingLotGameMove(Position(rec.row, rec.col), ParkingLotObject.entries[rec.intValue1])
}
