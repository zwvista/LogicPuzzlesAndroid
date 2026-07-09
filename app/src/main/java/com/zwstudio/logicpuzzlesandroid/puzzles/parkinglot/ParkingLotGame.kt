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
        val car_offset = listOf(
            listOf(Position(0, 0), Position(0, 1)),
            listOf(Position(0, 0), Position(0, 1), Position(0, 2)),
            listOf(Position(0, 0), Position(1, 0)),
            listOf(Position(0, 0), Position(1, 0), Position(2, 0)),
        )
        val car_objects = listOf(
            listOf(ParkingLotObject.Left, ParkingLotObject.Right),
            listOf(ParkingLotObject.Left, ParkingLotObject.Horizontal, ParkingLotObject.Right),
            listOf(ParkingLotObject.Top, ParkingLotObject.Bottom),
            listOf(ParkingLotObject.Top, ParkingLotObject.Vertical, ParkingLotObject.Bottom),
        )
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
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
    fun getStateHint(p: Position) = currentState.pos2stateHint[p]
    fun getStateAllowed(p: Position) = currentState.pos2stateAllowed[p]
}
