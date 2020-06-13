package com.zwstudio.logicpuzzlesandroid.common.domain

class Position @JvmOverloads constructor(var row: Int = 0, var col: Int = 0) : Comparable<Any?> {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is Position) {
            val x = other
            return row == x.row && col == x.col
        }
        return super.equals(other)
    }
    override fun compareTo(other: Any?): Int {
        val x = other as Position?
        return hashCode() - x.hashCode()
    }
    override fun hashCode() = row * 100 + col
    override fun toString() = String.format("(%d,%d)", row, col)

    operator fun plus(x: Position) = Position(row + x.row, col + x.col)
    operator fun minus(x: Position) = Position(row - x.row, col - x.col)
    operator fun unaryPlus() = Position(+row, +col)
    operator fun unaryMinus() = Position(-row, -col)
}