package com.zwstudio.logicpuzzlesandroid.puzzles.parkinglot

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ParkingLotGame(layout: List<String>, gi: GameInterface<ParkingLotGame, ParkingLotGameMove, ParkingLotGameState>, gdi: GameDocumentInterface) : CellsGame<ParkingLotGame, ParkingLotGameMove, ParkingLotGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(-1, -1),
            Position(-1, 0),
            Position(0, 0),
            Position(0, -1)
        )
    }

    val pos2hint = mutableMapOf<Position, Int>();

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        for (r in 0 until rows + 1) {
            val str = layout[r]
            for (c in 0 until cols + 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ')
                    pos2hint[p] = ch - '0'
            }
        }
        val state = ParkingLotGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position): ParkingLotObject = currentState[p]
    fun getObject(row: Int, col: Int): ParkingLotObject = currentState[row, col]
    fun getPosState(p: Position) = currentState.pos2state[p]
}
