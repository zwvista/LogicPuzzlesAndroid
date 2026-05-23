package com.zwstudio.logicpuzzlesandroid.puzzles.nooks

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.GardenerGame

class NooksGameState(game: NooksGame) : CellsGameState<NooksGame, NooksGameMove, NooksGameState>(game) {
    val objArray = Array(rows * cols) { NooksObject.Empty }
    val pos2state = mutableMapOf<Position, HintState>()
    val invalid2x2Squares = mutableListOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: NooksObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: NooksObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = NooksObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: NooksGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2hint.contains(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: NooksGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2hint.contains(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            NooksObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) NooksObject.Marker else NooksObject.Hedge
            NooksObject.Hedge -> if (markerOption == MarkerOptions.MarkerLast) NooksObject.Marker else NooksObject.Empty
            NooksObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) NooksObject.Hedge else NooksObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 17/Nooks

        Summary
        ...in a forest

        Description
        1. Fill some tiles with hedges, so that each number (where someone is playing hide and seek)
           finds itself in the nook.
        2. a Nook is a dead end, one tile wide, with a number in it.
        3. a Nook contains a number that shows you how many tiles can be seen in a straight line from
           there, including the tile itself.
        4. The resulting maze should be a single one-tile path connected horizontally or vertically
           where there are no 2x2 areas of the same type (hedge or path).
        5. No area in the maze can have the characteristics of a Nook without a number in it.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (p in game.pos2hint.keys)
            pos2state[p] = HintState.Normal
        // 1. Fill some tiles with hedges, so that each number (where someone is playing hide and seek)
        //    finds itself in the nook.
        // 2. a Nook is a dead end, one tile wide, with a number in it.
        // 5. No area in the maze can have the characteristics of a Nook without a number in it.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p].isHedge) continue
                val rng = NooksGame.offset.map { p + it }.filter { isValid(it) && this[it].isEmpty }
                if (rng.size != 1) continue
                val n2 = game.pos2hint[p]
                if (n2 == null) { isSolved = false; continue }
                // 3. a Nook contains a number that shows you how many tiles can be seen in a straight line from
                //    there, including the tile itself.
                val os = rng[0] - p
                var n1 = 1
                var p2 = p + os
                while (isValid(p2) && this[p2].isEmpty) {
                    n1 += 1
                    p2 += os
                }
                val s = if (n2 == NooksGame.PUZ_UNKWOWN || n1 == n2) HintState.Complete else if (n1 < n2) HintState.Normal else HintState.Error
                pos2state[p] = s
                if (s != HintState.Complete) isSolved = false
            }
        // 4. there are no 2x2 areas of the same type (hedge or path).
        invalid2x2Squares.clear()
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                val rng = NooksGame.offset2.map { p + it }
                val isOfSameType = rng.all { this[it].isHedge } || rng.all { this[it].isEmpty }
                if (isOfSameType) { invalid2x2Squares.add(p + Position.SouthEast); isSolved = false }
            }
        // 4. The resulting maze should be a single one-tile path connected horizontally or vertically
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p].isHedge) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node)
            for (os in GardenerGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}