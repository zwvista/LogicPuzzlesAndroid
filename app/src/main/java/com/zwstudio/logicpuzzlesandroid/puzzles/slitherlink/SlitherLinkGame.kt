package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SlitherLinkGame(layout: List<String>, gi: GameInterface<SlitherLinkGame, SlitherLinkGameMove, SlitherLinkGameState>, gdi: GameDocumentInterface) : CellsGame<SlitherLinkGame, SlitherLinkGameMove, SlitherLinkGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        var dirs = intArrayOf(1, 0, 3, 2)
    }

    var pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        for (r in 0 until rows - 1) {
            val str = layout[r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch in '0'..'9') {
                    val n = ch - '0'
                    pos2hint[p] = n
                }
            }
        }
        val state = SlitherLinkGameState(this)
        levelInitilized(state)
    }

    fun switchObject(move: SlitherLinkGameMove) = changeObject(move, SlitherLinkGameState::switchObject)
    fun setObject(move: SlitherLinkGameMove) = changeObject(move, SlitherLinkGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}