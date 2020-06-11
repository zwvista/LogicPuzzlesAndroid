package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class BalancedTapasGameState(game: BalancedTapasGame) : CellsGameState<BalancedTapasGame, BalancedTapasGameMove, BalancedTapasGameState>(game) {
    var objArray = Array<BalancedTapasObject>(rows() * cols()) { BalancedTapasEmptyObject() }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BalancedTapasObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: BalancedTapasObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = BalancedTapasHintObject()
        updateIsSolved()
    }

    fun setObject(move: BalancedTapasGameMove): Boolean {
        val p = move.p
        val objOld = this[p]
        val objNew = move.obj
        if (objOld is BalancedTapasHintObject || objOld == objNew)
            return false
        this[p] = objNew
        updateIsSolved()
        return true
    }

    fun switchObject(move: BalancedTapasGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is BalancedTapasEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) BalancedTapasMarkerObject() else BalancedTapasWallObject()
            is BalancedTapasWallObject -> if (markerOption == MarkerOptions.MarkerLast) BalancedTapasMarkerObject() else BalancedTapasEmptyObject()
            is BalancedTapasMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) BalancedTapasWallObject() else BalancedTapasEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Balanced Tapas

        Summary
        Healthy Spanish diet

        Description
        1. Plays with the same rules as Tapa with these variations.
        2. The board is divided in two vertical parts.
        3. The filled cell count in both parts must be equal.
    */
    private fun updateIsSolved() {
        isSolved = true
        fun computeHint(filled: List<Int>): List<Int> {
            if (filled.isEmpty()) return listOf(0)
            val hint = mutableListOf<Int>()
            for (j in filled.indices)
                if (j == 0 || filled[j] - filled[j - 1] != 1)
                    hint.add(1)
                else
                    hint[hint.size - 1]++
            if (filled.size > 1 && hint.size > 1 && filled.last() - filled.first() == 7) {
                hint[0] += hint.last()
                hint.removeAt(hint.size - 1)
            }
            hint.sort()
            return hint
        }
        fun isCompatible(computedHint: List<Int>, givenHint: List<Int>): Boolean {
            if (computedHint == givenHint) return true
            if (computedHint.size != givenHint.size) return false
            val h1 = computedHint.toHashSet()
            val h2 = givenHint.toHashSet()
            h2.remove(-1)
            return h1.containsAll(h2)
        }
        for ((p, arr2) in game.pos2hint) {
            val filled = (0 until 8).filter {
                val p2 = p.add(BalancedTapasGame.offset[it])
                isValid(p2) && this[p2] is BalancedTapasWallObject
            }
            val arr = computeHint(filled)
            val s = if (arr == listOf(0)) HintState.Normal else if (isCompatible(arr, arr2)) HintState.Complete else HintState.Error
            this[p] = BalancedTapasHintObject(s)
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        for (r in 0 until rows() - 1)
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                if (BalancedTapasGame.offset2.all { this[p.add(it)] is BalancedTapasWallObject }) {
                    isSolved = false
                    return
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (get(p) is BalancedTapasWallObject)
                    rngWalls.add(p)
            }
        for (p in rngWalls)
            for (os in BalancedTapasGame.offset) {
                val p2 = p.add(os)
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p], pos2node[p2])
            }
        g.setRootNode(pos2node[rngWalls[0]])
        val nodeList = g.bfs()
        if (rngWalls.size != nodeList.size) {
            isSolved = false
            return
        }
        fun computeWalls(from: Int, to: Int): Int {
            var n = 0
            for (c in 0 until cols())
                for (r in 0 until rows())
                    if (this[r, c] is BalancedTapasWallObject)
                        n++
            return n
        }
        val n1 = computeWalls(0, game.left)
        val n2 = computeWalls(game.right, cols())
        if (n1 != n2) isSolved = false
    }
}
