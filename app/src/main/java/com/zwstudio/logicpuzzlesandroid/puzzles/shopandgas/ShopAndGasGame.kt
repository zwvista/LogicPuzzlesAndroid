package com.zwstudio.logicpuzzlesandroid.puzzles.shopandgas

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ShopAndGasGame(layout: List<String>, gi: GameInterface<ShopAndGasGame, ShopAndGasGameMove, ShopAndGasGameState>, gdi: GameDocumentInterface) : CellsGame<ShopAndGasGame, ShopAndGasGameMove, ShopAndGasGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val PUZ_HOME = 'H'
        val PUZ_SHOP = 'S'
        val PUZ_GAS = 'G'
    }

    var objArray: CharArray
    var home = Position.Zero

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows * cols)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                val p = Position(r, c)
                this[p] = ch
                if (ch == PUZ_HOME)
                    home = p
            }
        }
        val state = ShopAndGasGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}
