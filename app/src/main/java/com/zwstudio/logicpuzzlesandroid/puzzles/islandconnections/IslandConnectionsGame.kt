package com.zwstudio.logicpuzzlesandroid.puzzles.islandconnections

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class IslandConnectionsGame(layout: List<String>, gi: GameInterface<IslandConnectionsGame, IslandConnectionsGameMove, IslandConnectionsGameState>, gdi: GameDocumentInterface) : CellsGame<IslandConnectionsGame, IslandConnectionsGameMove, IslandConnectionsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_UNKNOWN = -1
    }

    val islandsInfo = mutableMapOf<Position, IslandConnectionsIslandInfo>()
    val shaded = mutableSetOf<Position>()
    fun isIsland(p: Position) = islandsInfo.containsKey(p)
    fun isShaded(p: Position) = shaded.contains(p)

    init {
        size = Position(layout.size * 2 - 1, layout[0].length * 2 - 1)
        for (r in 0 until rows step 2) {
            var str = layout[r / 2]
            for (c in 0 until cols step 2) {
                val p = Position(r, c)
                when (val ch = str[c / 2]) {
                    'S' -> shaded.add(p)
                    'O' -> islandsInfo[p] = IslandConnectionsIslandInfo(PUZ_UNKNOWN)
                    ' ' -> {}
                    else -> islandsInfo[p] = IslandConnectionsIslandInfo(ch - '0')
                }
            }
        }
        for ((p, info) in islandsInfo) {
            for (i in 0 until 4) {
                val os = offset[i]
                var p2 = p + os
                while (isValid(p2) && !isShaded(p2)) {
                    if (isIsland(p2)) {
                        info.neighbors[i] = p2
                        break
                    }
                    p2 += os
                }
            }
        }
        val state = IslandConnectionsGameState(this)
        levelInitialized(state)
    }

    fun switchIslandConnections(move: IslandConnectionsGameMove) =
        changeObject(move) { state, move ->
            if (move.pTo < move.pFrom) {
                val t = move.pFrom
                move.pFrom = move.pTo
                move.pTo = t
            }
            state.switchIslandConnections(move)
        }

    override fun setObject(move: IslandConnectionsGameMove) = switchIslandConnections(move)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}