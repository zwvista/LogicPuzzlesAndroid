package com.zwstudio.logicpuzzlesandroid.common.domain

open class CellsGameState<G : CellsGame<G, GM, GS>?, GM, GS : GameState?>(var game: G) : GameState() {
    fun size(): Position? {
        return game!!.size
    }

    fun rows(): Int {
        return game!!.rows()
    }

    fun cols(): Int {
        return game!!.cols()
    }

    fun isValid(row: Int, col: Int): Boolean {
        return game!!.isValid(row, col)
    }

    fun isValid(p: Position?): Boolean {
        return game!!.isValid(p!!)
    }

}