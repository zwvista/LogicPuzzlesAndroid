package com.zwstudio.logicpuzzlesandroid.puzzles.cloudsandclears

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.East
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.North
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.NorthEast
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.NorthWest
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.South
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.SouthEast
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.SouthWest
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.West
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.Zero

class CloudsAndClearsGame(layout: List<String>, gi: GameInterface<CloudsAndClearsGame, CloudsAndClearsGameMove, CloudsAndClearsGameState>, gdi: GameDocumentInterface) : CellsGame<CloudsAndClearsGame, CloudsAndClearsGameMove, CloudsAndClearsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            North,
            NorthEast,
            East,
            SouthEast,
            South,
            SouthWest,
            West,
            NorthWest,
            Zero,
        )
    }

    val pos2hint = mutableMapOf<Position, Int>();

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ')
                    pos2hint[p] = ch - '0'
            }
        }
        val state = CloudsAndClearsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position): CloudsAndClearsObject = currentState[p]
    fun getObject(row: Int, col: Int): CloudsAndClearsObject = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
