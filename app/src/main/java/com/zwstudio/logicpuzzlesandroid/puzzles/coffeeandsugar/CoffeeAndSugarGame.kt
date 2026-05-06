package com.zwstudio.logicpuzzlesandroid.puzzles.coffeeandsugar

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.tracenumbers.TraceNumbersGame.Companion.PUZ_ONE

class CoffeeAndSugarGame(layout: List<String>, gi: GameInterface<CoffeeAndSugarGame, CoffeeAndSugarGameMove, CoffeeAndSugarGameState>, gdi: GameDocumentInterface) : CellsGame<CoffeeAndSugarGame, CoffeeAndSugarGameMove, CoffeeAndSugarGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_COFFEE = 'C'
        const val PUZ_SUGAR = 'S'
    }

    val objArray: CharArray
    var chMax = PUZ_ONE
    var expectedChars = PUZ_ONE.toString()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows * cols)
        for (r in 0 until rows) {
            var str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                this[r, c] = ch
                if (chMax < ch) chMax = ch
            }
        }
        var ch = PUZ_ONE
        while (ch != chMax)
            expectedChars += ++ch
        val state = CoffeeAndSugarGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}
