package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGame
import java.util.*

class TapDifferentlyGameState(game: TapDifferentlyGame) : CellsGameState<TapDifferentlyGame, TapDifferentlyGameMove, TapDifferentlyGameState>(game) {
    var objArray = Array<TapDifferentlyObject>(rows() * cols()) { TapDifferentlyEmptyObject() }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: TapDifferentlyObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: TapDifferentlyObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = TapDifferentlyHintObject()
        updateIsSolved()
    }

    fun setObject(move: TapDifferentlyGameMove): Boolean {
        val p = move.p
        val objOld = this[p]
        val objNew = move.obj
        if (objOld is TapDifferentlyHintObject || objOld == objNew) return false
        this[p] = objNew
        updateIsSolved()
        return true
    }

    fun switchObject(move: TapDifferentlyGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is TapDifferentlyEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) TapDifferentlyMarkerObject() else TapDifferentlyWallObject()
            is TapDifferentlyWallObject -> if (markerOption == MarkerOptions.MarkerLast) TapDifferentlyMarkerObject() else TapDifferentlyEmptyObject()
            is TapDifferentlyMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) TapDifferentlyWallObject() else TapDifferentlyEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Tap Differently

        Summary
        Tapa Landscaper

        Description
        1. Plays with the same rules as Tapa with these variations:
        2. Each row must have a different number of filled cells.
        3. Each column must have a different number of filled cells.
    */
    private fun updateIsSolved() {
        isSolved = true
        // A number indicates how many of the surrounding tiles are filled. If a
        // tile has more than one number, it hints at multiple separated groups
        // of filled tiles.
        fun computeHint(filled: List<Int>): List<Int> {
            val hint = mutableListOf<Int>()
            if (filled.isEmpty())
                hint.add(0)
            else {
                for (j in filled.indices)
                    if (j == 0 || filled[j] - filled[j - 1] != 1)
                        hint.add(1)
                    else
                        hint[hint.size - 1] = hint[hint.size - 1] + 1
                if (filled.size > 1 && hint.size > 1 && filled[filled.size - 1] - filled[0] == 7) {
                    hint[0] = hint[0] + hint[hint.size - 1]
                    hint.removeAt(hint.size - 1)
                }
                hint.sort()
            }
            return hint
        }
        fun isCompatible(computedHint: List<Int>, givenHint: List<Int>): Boolean {
            if (computedHint == givenHint) return true
            if (computedHint.size != givenHint.size) return false
            val h1: Set<Int> = HashSet(computedHint)
            val h2: MutableSet<Int> = HashSet(givenHint)
            h2.remove(-1)
            return h1.containsAll(h2)
        }
        for ((p, arr2) in game.pos2hint) {
            val filled = (0 until 8).filter {
                val p2 = p.add(TapDifferentlyGame.offset[it])
                isValid(p2) && this[p2] is TapDifferentlyWallObject
            }
            val arr = computeHint(filled)
            val s = if (arr.size == 1 && arr[0] == 0) HintState.Normal else if (isCompatible(arr, arr2)) HintState.Complete else HintState.Error
            this[p] = TapDifferentlyHintObject(s)
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
        // Tiles with numbers can be considered 'empty'.
        for (r in 0 until rows() - 1)
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                if (TapDifferentlyGame.offset2.all {
                    val o = this[p.add(it)]
                    o is TapDifferentlyWallObject
                }) {
                    isSolved = false
                    return
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                if (this[p] is TapDifferentlyWallObject) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in TapaGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
        // The goal is to fill some tiles forming a single orthogonally continuous
        // path. Just like Nurikabe.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
        // 2. Each row must have a different number of filled cells.
        val nums = mutableSetOf<Int>()
        for (r in 0 until rows()) {
            var n = 0
            for (c in 0 until cols())
                if (this[r, c] is TapDifferentlyWallObject)
                    n++
            nums.add(n)
        }
        if (nums.size != rows()) {
            isSolved = false
            return
        }
        // 3. Each column must have a different number of filled cells.
        nums.clear()
        for (c in 0 until cols()) {
            var n = 0
            for (r in 0 until rows())
                if (this[r, c] is TapDifferentlyWallObject)
                    n++
            nums.add(n)
        }
        if (nums.size != cols()) isSolved = false
    }
}