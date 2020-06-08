package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class NumberLinkGameState(game: NumberLinkGame) : CellsGameState<NumberLinkGame, NumberLinkGameMove, NumberLinkGameState>(game) {
    var objArray = Array(rows() * cols()) { Array(4) { false } }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Array<Boolean>) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: NumberLinkGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p = move.p
        val p2 = p.add(NumberLinkGame.offset[dir])
        if (!isValid(p2)) return false
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return true
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/NumberLink

        Summary
        Connect the same numbers without the crossing paths

        Description
        1. Connect the couples of equal numbers (i.e. 2 with 2, 3 with 3 etc)
           with a continuous line.
        2. The line can only go horizontally or vertically and can't cross
           itself or other lines.
        3. Lines must originate on a number and must end in the other equal
           number.
        4. At the end of the puzzle, you must have covered ALL the squares with
           lines and no line can cover a 2*2 area (like a 180 degree turn).
        5. In other words you can't turn right and immediately right again. The
           same happens on the left, obviously. Be careful not to miss this rule.

        Variant
        6. In some levels there will be a note that tells you don't need to cover
           all the squares.
        7. In some levels you will have more than a couple of the same number.
           In these cases, you must connect ALL the same numbers together.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val pos2indexes = mutableMapOf<Position, MutableList<Int>>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val n = this[p].filter { it }.size
                val b = game.pos2hint[p] != null
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (b && n == 1 || !b && n == 2)
                    pos2indexes[p] = (0 until 4).filter { this[p][it] }.toMutableList()
                else  // 3. Lines must originate on a number and must end in the other equal
                // number.
                    isSolved = false
            }
        for ((p, node) in pos2node) {
            pos2state[p] = HintState.Normal
            val indexes = pos2indexes[p] ?: continue
            for (i in indexes) {
                val p2 = p.add(NumberLinkGame.offset[i])
                val node2 = pos2node[p2]
                g.connectNode(node, node2)
            }
            if (indexes.size != 2) continue
            val i1 = indexes[0]
            val i2 = indexes[1]
            // 4. At the end of the puzzle, no line can cover a 2*2 area (like a 180 degree turn).
            // 5. In other words you can't turn right and immediately right again. The
            // same happens on the left, obviously. Be careful not to miss this rule.
            fun f(i: Int, isRight: Boolean) {
                val p2 = p.add(NumberLinkGame.offset[i])
                val indexes2 = pos2indexes[p2]
                if (indexes2 == null || indexes2.size != 2) return
                val i3 = (i + 2) % 4
                indexes2.remove(i1)
                val i4 = indexes2[0]
                if (isRight && (i3 + 3) % 4 == i4 || !isRight && (i3 + 1) % 4 == i4) {
                    pos2state[p] = HintState.Error
                    isSolved = false
                }
            }
            if ((i1 + 3) % 4 == i2) f(i2, true)
            if ((i2 + 3) % 4 == i1) f(i1, true)
            if ((i1 + 1) % 4 == i2) f(i2, false)
            if ((i2 + 1) % 4 == i1) f(i1, false)
        }
        while (!pos2node.isEmpty()) {
            g.setRootNode(pos2node.values.first())
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng1 = area.filter { game.pos2hint.containsKey(it) }
            if (rng1.isEmpty()) {
                isSolved = false
                continue
            }
            val rng2 = game.pos2rng[game.pos2hint[rng1[0]]]!!
            val b1 = rng1.minus(rng2).isEmpty()
            val b2 = rng2.minus(rng1).isEmpty()
            val b3 = area.all { pos2state[it] != HintState.Error }
            // 3. Lines must originate on a number and must end in the other equal
            // number.
            // 4. At the end of the puzzle, you must have covered ALL the squares with
            // lines.
            val s = if (!b1 || !b3) HintState.Error else if (b2) HintState.Complete else HintState.Normal
            if (s != HintState.Complete) isSolved = false
            for (p in rng1)
                pos2state[p] = s
        }
    }
}