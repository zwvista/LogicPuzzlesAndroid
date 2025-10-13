package com.zwstudio.logicpuzzlesandroid.common.domain

data class Position @JvmOverloads constructor(var row: Int = 0, var col: Int = 0) : Comparable<Any?> {
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

    companion object {
        val North = Position(-1,  0)
        val NorthEast = Position(-1,  1)
        val East = Position(0,  1)
        val SouthEast = Position(1,  1)
        val South = Position(1,  0)
        val SouthWest = Position(1, -1)
        val West = Position(0, -1)
        val NorthWest = Position(-1, -1)
        val Zero = Position(0,  0)

        val Directions4 = listOf(
            North,
            East,
            South,
            West,
        )
        val Directions8 = listOf(
            North,
            NorthEast,
            East,
            SouthEast,
            South,
            SouthWest,
            West,
            NorthWest,
        )
        val WallsOffset4 = listOf(
            Zero, // North
            East,
            South,
            Zero, // West
        )
        val Square2x2Offset = listOf(
            Zero,        // 2*2 nw
            East,        // 2*2 ne
            South,       // 2*2 sw
            SouthEast,   // 2*2 se
        )
    }
}