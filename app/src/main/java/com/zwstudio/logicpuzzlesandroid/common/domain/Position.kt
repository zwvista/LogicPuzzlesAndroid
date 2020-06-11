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

    override fun hashCode(): Int {
        return row * 100 + col
    }

    override fun toString(): String {
        return String.format("(%d,%d)", row, col)
    }

    fun add(x: Position): Position {
        return Position(row + x.row, col + x.col)
    }

    fun subtract(x: Position): Position {
        return Position(row - x.row, col - x.col)
    }

    fun plus(): Position {
        return Position(+row, +col)
    }

    fun minus(): Position {
        return Position(-row, -col)
    }

    fun addBy(x: Position) {
        row += x.row
        col += x.col
    }

    fun subtractBy(x: Position) {
        row -= x.row
        col -= x.col
    }

    override fun compareTo(other: Any?): Int {
        val x = other as Position?
        return hashCode() - x.hashCode()
    }

}