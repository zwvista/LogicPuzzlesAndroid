package com.zwstudio.logicpuzzlesandroid.puzzles.straightandbendlands

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class StraightAndBendLandsGameState(game: StraightAndBendLandsGame) : CellsGameState<StraightAndBendLandsGame, StraightAndBendLandsGameMove, StraightAndBendLandsGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: StraightAndBendLandsGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + StraightAndBendLandsGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2))
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 3/Straight and Bend Lands

        Summary
        Our land of curves is better than your straight one!

        Description
        1. This odd nation is divided into two types of regions. One where roads
           always turn on villages, and one where roads always go straight!
        2. Draw a loop that goes through villages (houses), but avoid trees.
        3. While passing on villages, the road might turn or not, but if it turns
           then the road will turn on all villages in that region.
        4. Conversely if it goes straight, all villages of that region will have
           the road go straight through them.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val dirs = (0 until 4).filter { this[p][it] }
                pos2dirs[p] = dirs
                if (!(dirs.size == 2 || dirs.isEmpty())) isSolved = false
            }
        // 3. the number on a Field tells you how many tiles you should go through it.
        for ((p, n2) in game.pos2hint) {
            val area = game.areas[game.pos2area[p]!!]
            val n1 = area.fold(0) { acc, p -> acc + (if (pos2dirs[p]!!.isEmpty()) 0 else 1) }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            pos2state[p] = s
        }
        if (!isSolved) return
        // Check the loop
        val p = pos2dirs.keys.firstOrNull()
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        var lastArea = -1
        val area2count = mutableMapOf<Int, Int>()
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            val area = game.pos2area[p2]!!
            if (area != lastArea) {
                area2count[area] = (area2count[area] ?: 0) + 1
                lastArea = area
            }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += StraightAndBendLandsGame.offset[n]
            if (p2 == p) {
                area2count[area] = area2count[area]!! - 1
                break
            }
        }
        // 1. The board represents a map of the Countryside, divided in Fields.
        // 2. The object is to have a walk around the Countryside, passing through
        //    each Field just once.
        // 4. A Field with no number can be passed through in any number of tiles,
        //    at least one.
        if (!(area2count.size == game.areas.size && area2count.all { it.value == 1 })) { isSolved = false; return }
        // 5. If you avoid two adjacent tiles in your path, they should be in the
        //    same Fields.
        // 6. Or in other words, two adjacent empty tiles cannot be in two different
        //    Fields.
        val rng = pos2dirs.filter { (p, dirs) -> dirs.isEmpty() }.keys
        if (rng.any { p ->
            val area = game.pos2area[p]!!
            StraightAndBendLandsGame.offset.any {
                val p2 = p + it
                rng.contains(p2) && area != game.pos2area[p2]!!
            }
        }) isSolved = false
    }
}