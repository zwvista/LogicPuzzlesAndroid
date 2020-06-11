package com.zwstudio.logicpuzzlesandroid.common.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface

class CellsGame<G : Game<G, GM, GS>, GM, GS : GameState>(gi: GameInterface<G, GM, GS>, gdi: GameDocumentInterface) : Game<G, GM, GS>(gi, gdi) {
    var size: Position? = null
    fun rows() = size!!.row
    fun cols() = size!!.col
    fun isValid(row: Int, col: Int) =
        row >= 0 && col >= 0 && row < size!!.row && col < size!!.col
    fun isValid(p: Position) = isValid(p.row, p.col)
}