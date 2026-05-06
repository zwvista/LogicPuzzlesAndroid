package com.zwstudio.logicpuzzlesandroid.puzzles.plugitin

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.tracenumbers.TraceNumbersGame.Companion.PUZ_ONE

class PlugItInGame(layout: List<String>, gi: GameInterface<PlugItInGame, PlugItInGameMove, PlugItInGameState>, gdi: GameDocumentInterface) : CellsGame<PlugItInGame, PlugItInGameMove, PlugItInGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_LIGHTBULB = 'L'
        const val PUZ_BATTERY = 'B'
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
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
                val ch = str[c]
                this[r, c] = ch
                if (chMax < ch) chMax = ch
            }
        }
        var ch = PUZ_ONE
        while (ch != chMax)
            expectedChars += ++ch
        val state = PlugItInGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}
