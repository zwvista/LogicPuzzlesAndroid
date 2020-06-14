package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LightenUpGame(layout: List<String>, gi: GameInterface<LightenUpGame, LightenUpGameMove, LightenUpGameState>, gdi: GameDocumentInterface) : CellsGame<LightenUpGame, LightenUpGameMove, LightenUpGameState>(gi, gdi) {
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
                if (ch == 'W' || ch in '0'..'9') {
                    val n = if (ch == 'W') -1 else ch - '0'
                    pos2hint[p] = n
                }
            }
        }
        val state = LightenUpGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}
