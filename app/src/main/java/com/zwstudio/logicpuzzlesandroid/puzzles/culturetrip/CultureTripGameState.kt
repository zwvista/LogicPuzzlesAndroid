package com.zwstudio.logicpuzzlesandroid.puzzles.culturetrip

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CultureTripGameState(game: CultureTripGame) : CellsGameState<CultureTripGame, CultureTripGameMove, CultureTripGameState>(game) {
    val objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: CultureTripGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + CultureTripGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2))
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 1/Culture Trip

        Summary
        Or how to make a culture trip complicated

        Description
        1. The board represents a City of Art and Culture, divided in neighborhoods.
        2. During a Culture Trip, in order to make everybody happy, you devise a
           convoluted method to visit a city:
        3. All neighborhoods must be visited exactly and only once.
        4. You have to set foot in a neighborhood only once and can't come back after
           you leave it.
        5. In a neighborhood, you either visit All Museums or All Monuments.
        6. If you visit Monuments, you can't pass over Museums and vice-versa.
        7. You have to alternate between neighborhoods where you visit Museums and
           those where you visit Monuments.
        8. After visiting Museums, you should visit Monuments, then Museums again, etc.
        9. The Trip must form a closed loop, in the end returning to the starting
           neighborhood.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] }
                if (dirs.size == 2)
                    // 9. The Trip must form a closed loop, in the end returning to the starting
                    //    neighborhood.
                    pos2dirs[p] = dirs
                else if (dirs.isNotEmpty()) {
                    // The loop cannot cross itself.
                    isSolved = false; return
                }
            }
        // Check the loop
        val p = pos2dirs.keys.firstOrNull { game[it] != ' ' }
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        var lastArea = -1
        val area2count = mutableMapOf<Int, Int>()
        val area2chars = mutableMapOf<Int, MutableList<Char>>()
        var ch = ' '
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            val area = game.pos2area[p2]!!
            val ch2 = game[p2]
            if (ch2 != ' ') {
                area2chars.getOrPut(area) { mutableListOf() }.add(ch2)
                // 7. You have to alternate between neighborhoods where you visit Museums and
                //    those where you visit Monuments.
                // 8. After visiting Museums, you should visit Monuments, then Museums again, etc.
                if (area != lastArea && ch2 == ch) {
                    isSolved = false; return
                }
                ch = ch2
            }
            if (area != lastArea) {
                area2count[area] = (area2count[area] ?: 0) + 1
                lastArea = area
            }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += CultureTripGame.offset[n]
            if (p2 == p) {
                if (area == game.pos2area[p]!!)
                    area2count[area] = area2count[area]!! - 1
                break
            }
        }
        // 3. All neighborhoods must be visited exactly and only once.
        // 4. You have to set foot in a neighborhood only once and can't come back after
        //    you leave it.
        // 5. In a neighborhood, you either visit All Museums or All Monuments.
        // 6. If you visit Monuments, you can't pass over Museums and vice-versa.
        if (!(area2count.size == game.areas.size && area2count.all { it.value == 1 } &&
            area2chars.size == game.areas.size && area2chars.all { (area, chars) ->
            val s = chars.toSet()
            if (s.size != 1)
                false
            else {
                val ch = s.first()
                chars.size == game.areas[area].count { game[it] == ch }
            }
        })) isSolved = false
    }
}