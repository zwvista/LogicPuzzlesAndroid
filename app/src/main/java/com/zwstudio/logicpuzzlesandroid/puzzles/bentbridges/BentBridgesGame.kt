package com.zwstudio.logicpuzzlesandroid.puzzles.bentbridges

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BentBridgesGame(layout: List<String>, gi: GameInterface<BentBridgesGame, BentBridgesGameMove, BentBridgesGameState>, gdi: GameDocumentInterface) : CellsGame<BentBridgesGame, BentBridgesGameMove, BentBridgesGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val islandsInfo = mutableMapOf<Position, BentBridgesIslandInfo>()
    fun isIsland(p: Position) = islandsInfo.containsKey(p)

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch.isDigit())
                    islandsInfo[p] = BentBridgesIslandInfo(ch - '0')
            }
        }
        for ((p, info) in islandsInfo) {
            for (i in 0 until 4) {
                val os = offset[i]
                var p2 = p + os
                while (isValid(p2)) {
                    if (isIsland(p2)) {
                        info.neighbors[i] = p2
                        break
                    }
                    p2 += os
                }
            }
        }
        val state = BentBridgesGameState(this)
        levelInitialized(state)
    }

    fun switchBentBridges(move: BentBridgesGameMove) =
        changeObject(move) { state, move ->
            if (move.pTo < move.pFrom) {
                val t = move.pFrom
                move.pFrom = move.pTo
                move.pTo = t
            }
            state.switchBentBridges(move)
        }

    override fun setObject(move: BentBridgesGameMove) = switchBentBridges(move)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}