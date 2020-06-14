package com.zwstudio.logicpuzzlesandroid.common.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface

open class CellsGameState<G : CellsGame<G, GM, GS>, GM, GS : GameState<GM>>(val game: G) : GameState<GM>() {
    val rows get() = game.rows
    val cols get() = game.cols
    fun isValid(row: Int, col: Int) = game.isValid(row, col)
    fun isValid(p: Position) = game.isValid(p)
}

open class CellsGame<G : Game<G, GM, GS>, GM, GS : GameState<GM>>(gi: GameInterface<G, GM, GS>, gdi: GameDocumentInterface) : Game<G, GM, GS>(gi, gdi) {
    lateinit var size: Position
    val rows get() = size.row
    val cols get() = size.col
    open fun isValid(row: Int, col: Int) = row >= 0 && col >= 0 && row < size.row && col < size.col
    fun isValid(p: Position) = isValid(p.row, p.col)
}