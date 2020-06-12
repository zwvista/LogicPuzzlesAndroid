package com.zwstudio.logicpuzzlesandroid.common.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface

open class CellsGameState<G : CellsGame<G, GM, GS>, GM, GS : GameState>(var game: G) : GameState() {
    fun size() = game.size
    fun rows() = game.rows()
    fun cols() = game.cols()
    fun isValid(row: Int, col: Int) = game.isValid(row, col)
    fun isValid(p: Position) = game.isValid(p)
}

open class CellsGame<G : Game<G, GM, GS>, GM, GS : GameState>(gi: GameInterface<G, GM, GS>, gdi: GameDocumentInterface) : Game<G, GM, GS>(gi, gdi) {
    lateinit var size: Position
    fun rows() = size.row
    fun cols() = size.col
    open fun isValid(row: Int, col: Int) = row >= 0 && col >= 0 && row < size.row && col < size.col
    fun isValid(p: Position) = isValid(p.row, p.col)
}