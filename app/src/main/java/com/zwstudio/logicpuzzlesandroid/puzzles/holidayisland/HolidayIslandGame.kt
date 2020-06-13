package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HolidayIslandGame(layout: List<String>, gi: GameInterface<HolidayIslandGame, HolidayIslandGameMove, HolidayIslandGameState>, gdi: GameDocumentInterface) : CellsGame<HolidayIslandGame, HolidayIslandGameMove, HolidayIslandGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ') pos2hint[p] = ch - '0'
            }
        }
        val state = HolidayIslandGameState(this)
        levelInitilized(state)
    }

    fun switchObject(move: HolidayIslandGameMove) = changeObject(move, HolidayIslandGameState::switchObject)
    fun setObject(move: HolidayIslandGameMove) = changeObject(move, HolidayIslandGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}