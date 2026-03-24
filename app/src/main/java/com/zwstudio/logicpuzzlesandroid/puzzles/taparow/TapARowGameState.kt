package com.zwstudio.logicpuzzlesandroid.puzzles.taparow

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.TapaGame

class TapARowGameState(game: TapARowGame) : CellsGameState<TapARowGame, TapARowGameMove, TapARowGameState>(game) {
    var objArray = Array(rows * cols) { TapARowObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: TapARowObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: TapARowObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = TapARowObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: TapARowGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == TapARowObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TapARowGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == TapARowObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            TapARowObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) TapARowObject.Marker else TapARowObject.Wall
            TapARowObject.Wall -> if (markerOption == MarkerOptions.MarkerLast) TapARowObject.Marker else TapARowObject.Empty
            TapARowObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) TapARowObject.Wall else TapARowObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Tap-A-Row

        Summary
        Tap me a row, please

        Description
        1. Plays with the same rules as Tapa with these variations:
        2. The number also tells you the filled cell count for that row.
        3. In other words, the sum of the digits in that row equals the number
           of that row.
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
                for (j in filled.indices) if (j == 0 || filled[j] - filled[j - 1] != 1)
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
            val h1 = HashSet(computedHint)
            val h2 = HashSet(givenHint)
            h2.remove(-1)
            return h1.containsAll(h2)
        }
        for ((p, arr2) in game.pos2hint) {
            val filled = (0 until 8).filter {
                val p2 = p + TapARowGame.offset[it]
                isValid(p2) && this[p2] == TapARowObject.Wall
            }
            val arr = computeHint(filled)
            val s = if (arr.size == 1 && arr[0] == 0) HintState.Normal else if (isCompatible(arr, arr2)) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
        // Tiles with numbers can be considered 'empty'.
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                if (TapARowGame.offset2.all {
                    val o = this[p + it]
                    o == TapARowObject.Wall
                }) {
                    isSolved = false
                    return
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == TapARowObject.Wall) {
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
        // 2. The number also tells you the filled cell count for that row.
        // 3. In other words, the sum of the digits in that row equals the number
        // of that row.
        for (r in 0 until rows) {
            var n1 = 0
            var n2 = 0
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == TapARowObject.Wall)
                    n1++
                else if (o == TapARowObject.Hint) {
                    val arr = game.pos2hint[p]!!
                    n2 += arr.sum()
                }
            }
            if (n2 != 0 && n1 != n2) {
                isSolved = false
                return
            }
        }
    }
}