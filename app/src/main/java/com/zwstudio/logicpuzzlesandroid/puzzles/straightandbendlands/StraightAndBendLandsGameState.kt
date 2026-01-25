package com.zwstudio.logicpuzzlesandroid.puzzles.straightandbendlands

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class StraightAndBendLandsGameState(game: StraightAndBendLandsGame) : CellsGameState<StraightAndBendLandsGame, StraightAndBendLandsGameMove, StraightAndBendLandsGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

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
        if (!isValid(p2) || game[p] == StraightAndBendLandsGame.PUZ_TREE || game[p2] == StraightAndBendLandsGame.PUZ_TREE)
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
                if (dirs.size == 2)
                    // 2. Draw a loop that goes through villages (houses), but avoid trees.
                    pos2dirs[p] = dirs
                else if (!(dirs.isEmpty() && game[p] == StraightAndBendLandsGame.PUZ_TREE)) {
                    // The loop cannot cross itself.
                    isSolved = false; return
                }
            }
        val pos2dirs2 = pos2dirs.toMap()
        // Check the loop
        val p = pos2dirs.keys.firstOrNull()
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += StraightAndBendLandsGame.offset[n]
            if (p2 == p) break
        }
        // 1. This odd nation is divided into two types of regions. One where roads
        //    always turn on villages, and one where roads always go straight!
        // 3. While passing on villages, the road might turn or not, but if it turns
        //    then the road will turn on all villages in that region.
        // 4. Conversely if it goes straight, all villages of that region will have
        //    the road go straight through them.
        if (!game.areas.all { area ->
            val rng = area.filter { game[it] == StraightAndBendLandsGame.PUZ_HOUSE }
            rng.all {
                val dirs = pos2dirs2[it]!!
                dirs[1] - dirs[0] == 2
            } || rng.all {
                val dirs = pos2dirs2[it]!!
                dirs[1] - dirs[0] != 2
            }
        }) isSolved = false
    }
}