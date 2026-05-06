package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.TapaGame

class TapAlikeGameState(game: TapAlikeGame) : CellsGameState<TapAlikeGame, TapAlikeGameMove, TapAlikeGameState>(game) {
    var objArray = Array(rows * cols) { TapAlikeObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: TapAlikeObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: TapAlikeObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = TapAlikeObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: TapAlikeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == TapAlikeObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TapAlikeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == TapAlikeObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            TapAlikeObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) TapAlikeObject.Marker else TapAlikeObject.Wall
            TapAlikeObject.Wall -> if (markerOption == MarkerOptions.MarkerLast) TapAlikeObject.Marker else TapAlikeObject.Empty
            TapAlikeObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) TapAlikeObject.Wall else TapAlikeObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Tap-Alike

        Summary
        Dr. Jekyll and Mr. Tapa

        Description
        1. Plays with the same rules as Tapa with these variations:
        2. At the end of the solution, the filled tiles will form an identical
           pattern to the one formed by the empty tiles.
        3. It's basically like having the same figure rotated or reversed in the
           opposite colour. The two figures will have the same exact shape.
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
            val filled = (0..<8).filter {
                val p2 = p + TapAlikeGame.offset[it]
                isValid(p2) && this[p2] == TapAlikeObject.Wall
            }
            val arr = computeHint(filled)
            val s = if (arr.size == 1 && arr[0] == 0) HintState.Normal else if (isCompatible(arr, arr2)) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
        // Tiles with numbers can be considered 'empty'.
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                if (TapAlikeGame.offset2.all {
                    val o = this[p + it]
                    o == TapAlikeObject.Wall
                }) {
                    isSolved = false
                    return
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] == TapAlikeObject.Wall) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in TapaGame.offset3) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        // The goal == to fill some tiles forming a single orthogonally continuous
        // path. Just like Nurikabe.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
        // 2. At the end of the solution, the filled tiles will form an identical
        // pattern to the one formed by the empty tiles.
        // 3. It's basically like having the same figure rotated or reversed in the
        // opposite colour. The two figures will have the same exact shape.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val o1 = this[r, c]
                val o2 = this[rows - 1 - r, cols - 1 - c]
                if ((o1 == TapAlikeObject.Wall) == (o2 == TapAlikeObject.Wall)) {
                    isSolved = false
                    return
                }
            }
    }
}