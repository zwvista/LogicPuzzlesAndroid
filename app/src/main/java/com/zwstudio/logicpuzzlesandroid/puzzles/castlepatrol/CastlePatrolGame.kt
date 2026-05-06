package com.zwstudio.logicpuzzlesandroid.puzzles.castlepatrol

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CastlePatrolGame(layout: List<String>, gi: GameInterface<CastlePatrolGame, CastlePatrolGameMove, CastlePatrolGameState>, gdi: GameDocumentInterface) : CellsGame<CastlePatrolGame, CastlePatrolGameMove, CastlePatrolGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val pos2obj = mutableMapOf<Position, CastlePatrolObject>()
    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length / 2)
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val (ch1, ch2) = str[c * 2] to str[c * 2 + 1]
                fun f(obj: CastlePatrolObject) {
                    pos2obj[p] = obj
                    pos2hint[p] = if (ch1.isDigit()) ch1 - '0' else ch1 - 'A' + 10
                }
                when (ch2) {
                    '.' -> f(CastlePatrolObject.EmptyHint)
                    'W' -> f(CastlePatrolObject.WallHint)
                }
            }
        }
        val state = CastlePatrolGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
