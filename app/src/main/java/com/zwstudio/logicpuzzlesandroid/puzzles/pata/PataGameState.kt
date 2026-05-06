package com.zwstudio.logicpuzzlesandroid.puzzles.pata

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PataGameState(game: PataGame) : CellsGameState<PataGame, PataGameMove, PataGameState>(game) {
    var objArray = Array(rows * cols) { PataObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: PataObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: PataObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = PataObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: PataGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == PataObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PataGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == PataObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            PataObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) PataObject.Marker else PataObject.Wall
            PataObject.Wall -> if (markerOption == MarkerOptions.MarkerLast) PataObject.Marker else PataObject.Empty
            PataObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) PataObject.Wall else PataObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/Pata

        Summary
        Yes, it's the opposite of Tapa

        Description
        1. Plays the opposite of Tapa, regarding the hints:
        2. A number indicates the groups of connected empty tiles that are around
           it, instead of filled ones.
        3. Different groups of empty tiles are separated by at least one filled cell.
        4. Same as Tapa:
        5. The filled tiles are continuous.
        6. You can't have a 2*2 space of filled tiles.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. A number indicates the groups of connected empty tiles that are around
        // it, instead of filled ones.
        fun computeHint(emptied: List<Int>): List<Int> {
            val hint = mutableListOf<Int>()
            if (emptied.isEmpty())
                hint.add(0)
            else {
                for (j in emptied.indices)
                    if (j == 0 || emptied[j] - emptied[j - 1] != 1)
                        hint.add(1)
                    else
                        hint[hint.size - 1] = hint[hint.size - 1] + 1
                if (emptied.size > 1 && hint.size > 1 && emptied[emptied.size - 1] - emptied[0] == 7) {
                    hint[0] = hint[0] + hint[hint.size - 1]
                    hint.removeAt(hint.size - 1)
                }
                hint.sort()
            }
            return hint
        }
        fun isCompatible(computedHint: List<Int>, givenHint: List<Int>): Boolean {
            if (computedHint.size != givenHint.size) return false
            val h1 = computedHint.sorted()
            val h2 = givenHint.sorted().toMutableList()
            h2.removeAll { it == -1 }
            val (n1, n2) = h1.size to h2.size
            return (0..(n1 - n2)).any {
                h1.subList(it, it + n2) == h2
            }
        }
        for ((p, arr2) in game.pos2hint) {
            val emptied = (0..<8).filter {
                val p2 = p + PataGame.offset[it]
                if (!isValid(p2))
                    false
                else {
                    val o = this[p2]
                    o == PataObject.Empty || o == PataObject.Hint
                }
            }
            val arr: List<Int> = computeHint(emptied)
            val filled = (0..<8).filter {
                val p2 = p + PataGame.offset[it]
                isValid(p2) && this[p2] == PataObject.Wall
            }
            val arr3 = computeHint(filled)
            val s = if (arr3.size == 1 && arr3[0] == 0) HintState.Normal else if (isCompatible(arr, arr2)) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // 6. You can't have a 2*2 space of filled tiles.
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                if (PataGame.offset2.all {
                    val o = this[p + it]
                    o == PataObject.Wall
                }) {
                    isSolved = false
                    return
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (this[p] == PataObject.Wall)
                    rngWalls.add(p)
            }
        for (p in rngWalls)
            for (os in PataGame.offset3) {
                val p2 = p + os
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        // 5. The filled tiles are continuous.
        g.rootNode = pos2node[rngWalls[0]]!!
        val nodeList = g.bfs()
        if (rngWalls.size != nodeList.size) isSolved = false
    }
}