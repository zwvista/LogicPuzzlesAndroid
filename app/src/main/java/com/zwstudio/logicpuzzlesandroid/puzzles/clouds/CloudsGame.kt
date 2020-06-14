package com.zwstudio.logicpuzzlesandroid.puzzles.clouds

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CloudsGame(layout: List<String>, gi: GameInterface<CloudsGame, CloudsGameMove, CloudsGameState>, gdi: GameDocumentInterface) : CellsGame<CloudsGame, CloudsGameMove, CloudsGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var row2hint: IntArray
    var col2hint: IntArray
    var pos2cloud = mutableListOf<Position>()

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)
        for (r in 0 until rows + 1) {
            val str = layout[r]
            for (c in 0 until cols + 1) {
                val p = Position(r, c)
                when (val ch = str[c]) {
                    'C' -> pos2cloud.add(p)
                    in '0'..'9' -> {
                        val n = ch - '0'
                        if (r == rows)
                            col2hint[c] = n
                        else if (c == cols)
                            row2hint[r] = n
                    }
                }
            }
        }
        val state = CloudsGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
}
