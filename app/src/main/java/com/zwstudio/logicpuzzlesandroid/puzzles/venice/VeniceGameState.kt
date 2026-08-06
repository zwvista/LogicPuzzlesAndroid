package com.zwstudio.logicpuzzlesandroid.puzzles.venice

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class VeniceGameState(game: VeniceGame) : CellsGameState<VeniceGame, VeniceGameMove, VeniceGameState>(game) {
    val objArray = Array<VeniceObject>(rows * cols) { VeniceObject.Empty }
    val pos2state = mutableMapOf<Position, HintState>()
    val invalid2x2Squares = mutableListOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: VeniceObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: VeniceObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = VeniceObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: VeniceGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == VeniceObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: VeniceGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == VeniceObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            VeniceObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) VeniceObject.Marker else VeniceObject.Water
            VeniceObject.Water -> if (markerOption == MarkerOptions.MarkerLast) VeniceObject.Marker else VeniceObject.Empty
            VeniceObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) VeniceObject.Water else VeniceObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 2/Venice

        Summary
        Gondolas and Canals

        Description
        1. Each number identifies a house in Venice.
        2. The number on it tells you how many tiles of Canal that house sees,
           horizontally and vertically in the four directions, up to the next empty cell.
        3. The Canal forms a single connected area which cannot contain a 2x2 area
           (like a Nurikabe).
    */
    private fun updateIsSolved() {
        isSolved = true
        // 3. The Canal cannot contain a 2x2 area (like a Nurikabe).
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                if (VeniceGame.offset2.map { p + it }.all { this[it] == VeniceObject.Water }) {
                    invalid2x2Squares.add(p + Position.SouthEast); isSolved = false
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] == VeniceObject.Water) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        // 1. Each number identifies a house in Venice.
        // 2. The number on it tells you how many tiles of Canal that house sees,
        //    horizontally and vertically in the four directions, up to the next empty cell.
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            for (i in 0..<4) {
                val os = VeniceGame.offset[i]
                var p2 = p + os
                while (isValid(p2)) {
                    if (this[p2] != VeniceObject.Water) break
                    n1++
                    p2 += os
                }
            }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            pos2state[p] = s
        }
        if (!isSolved) return
        for ((p, node) in pos2node)
            for (os in VeniceGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        // 3. The Canal forms a single connected area
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}