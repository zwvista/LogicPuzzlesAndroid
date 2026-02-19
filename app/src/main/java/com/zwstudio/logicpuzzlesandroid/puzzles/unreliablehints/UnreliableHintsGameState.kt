package com.zwstudio.logicpuzzlesandroid.puzzles.unreliablehints

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class UnreliableHintsGameState(game: UnreliableHintsGame) : CellsGameState<UnreliableHintsGame, UnreliableHintsGameMove, UnreliableHintsGameState>(game) {
    private var objArray = Array(rows * cols) { UnreliableHintsObject.Normal }
    var row2hint = Array(rows) { "" }
    var col2hint = Array(cols) { "" }

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: UnreliableHintsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: UnreliableHintsObject) {this[p.row, p.col] = obj}

    override fun setObject(move: UnreliableHintsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: UnreliableHintsGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        fun f(obj: UnreliableHintsObject) =
            when (obj) {
                UnreliableHintsObject.Normal ->
                    if (markerOption == MarkerOptions.MarkerFirst) UnreliableHintsObject.Marker
                    else UnreliableHintsObject.Darken
                UnreliableHintsObject.Darken ->
                    if (markerOption == MarkerOptions.MarkerLast) UnreliableHintsObject.Marker
                    else UnreliableHintsObject.Normal
                UnreliableHintsObject.Marker ->
                    if (markerOption == MarkerOptions.MarkerFirst) UnreliableHintsObject.Darken
                    else UnreliableHintsObject.Normal
            }
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        move.obj = f(this[p])
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 5/Unreliable hints

        Summary
        Can't trust them all

        Description
        1. Shade some tiles according to the following rules:
        2. Shaded tiles must not be orthogonally connected.
        3. You can shade tiles with arrows and numbers.
        4. All tiles which are not shaded must form an orthogonally continuous area.
        5. A cell containing a number and an arrow tells you how many tiles are shaded
           in that direction.
        6. However not all tiles that are shaded tell you lies.
    */
    private fun updateIsSolved() {
        isSolved = true
        var chars: String
        // 1. The goal is to shade squares so that a number appears only once in a
        // row.
        for (r in 0 until rows) {
            row2hint[r] = ""
            chars = row2hint[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == UnreliableHintsObject.Darken) continue
                val ch = game[r, c]
                if (chars.contains(ch)) {
                    isSolved = false
                    row2hint[r] += ch.toString()
                } else
                    chars += ch
            }
        }
        // 1. The goal is to shade squares so that a number appears only once in a
        // column.
        for (c in 0 until cols) {
            col2hint[c] = ""
            chars = col2hint[c]
            for (r in 0 until rows) {
                val p = Position(r, c)
                if (this[p] == UnreliableHintsObject.Darken) continue
                val ch = game[r, c]
                if (chars.contains(ch)) {
                    isSolved = false
                    col2hint[c] += ch.toString()
                } else
                    chars += ch
            }
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngDarken = mutableListOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == UnreliableHintsObject.Darken)
                    rngDarken.add(p)
                else {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        // 2. While doing that, you must take care that shaded squares don't touch
        // horizontally or vertically between them.
        for (p in rngDarken)
            for (os in UnreliableHintsGame.offset) {
                val p2 = p + os
                if (rngDarken.contains(p2)) {
                    isSolved = false
                    return
                }
            }
        for (p in pos2node.keys)
            for (os in UnreliableHintsGame.offset) {
                val p2 = p + os
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        // 3. In the end all the un-shaded squares must form a single continuous area.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}
