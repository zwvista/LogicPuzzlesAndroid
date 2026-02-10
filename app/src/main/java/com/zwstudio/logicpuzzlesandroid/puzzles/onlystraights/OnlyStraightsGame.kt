package com.zwstudio.logicpuzzlesandroid.puzzles.onlystraights

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class OnlyStraightsGame(layout: List<String>, gi: GameInterface<OnlyStraightsGame, OnlyStraightsGameMove, OnlyStraightsGameState>, gdi: GameDocumentInterface) : CellsGame<OnlyStraightsGame, OnlyStraightsGameMove, OnlyStraightsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_TOWN = 'O'
    }

    var objArray: CharArray

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size * 2 - 1, layout[0].length * 2 - 1)
        objArray = CharArray(rows * cols) { ' ' }
        for (r in 0 until rows step 2) {
            val str = layout[r / 2]
            for (c in 0 until cols step 2) {
                val ch = str[c / 2]
                if (!ch.isDigit()) continue
                val n = ch - '0'
                if (n and 1 != 0) this[r, c] = PUZ_TOWN
                if (n and 2 != 0) this[r, c + 1] = PUZ_TOWN
                if (n and 4 != 0) this[r + 1, c] = PUZ_TOWN
            }
        }
        val state = OnlyStraightsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}
