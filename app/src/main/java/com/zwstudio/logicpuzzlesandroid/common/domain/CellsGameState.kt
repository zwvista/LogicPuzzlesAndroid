package com.zwstudio.logicpuzzlesandroid.common.domain

open class CellsGameState<G : CellsGame<G, GM, GS>, GM, GS : GameState>(var game: G) : GameState() {
    fun size() = game.size
    fun rows() = game.rows()
    fun cols() = game.cols()
    fun isValid(row: Int, col: Int) = game.isValid(row, col)
    fun isValid(p: Position) = game.isValid(p)
}