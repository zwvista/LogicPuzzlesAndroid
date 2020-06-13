package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BootyIslandGame(layout: List<String>, gi: GameInterface<BootyIslandGame, BootyIslandGameMove, BootyIslandGameState>, gdi: GameDocumentInterface) : CellsGame<BootyIslandGame, BootyIslandGameMove, BootyIslandGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1)
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
                if (ch in '0'..'9')
                    pos2hint[p] = ch - '0'
            }
        }
        val state = BootyIslandGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    fun switchObject(move: BootyIslandGameMove) = changeObject(move, BootyIslandGameState::switchObject)
    fun setObject(move: BootyIslandGameMove) = changeObject(move, BootyIslandGameState::setObject)
    fun getObject(p: Position): BootyIslandObject = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}
