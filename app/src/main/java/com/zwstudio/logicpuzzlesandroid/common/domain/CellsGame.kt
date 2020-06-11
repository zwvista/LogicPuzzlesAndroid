package com.zwstudio.logicpuzzlesandroid.common.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface

open class CellsGame<G : Game<G, GM, GS>?, GM, GS : GameState?>(gi: GameInterface<G, GM, GS>?, gdi: GameDocumentInterface) : Game<G, GM, GS>(gi, gdi) {
    var size: Position? = null
    fun rows(): Int {
        return size!!.row
    }

    fun cols(): Int {
        return size!!.col
    }

    open fun isValid(row: Int, col: Int): Boolean {
        return row >= 0 && col >= 0 && row < size!!.row && col < size!!.col
    }

    fun isValid(p: Position): Boolean {
        return isValid(p.row, p.col)
    }
}