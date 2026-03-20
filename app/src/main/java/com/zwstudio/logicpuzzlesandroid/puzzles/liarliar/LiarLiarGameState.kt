package com.zwstudio.logicpuzzlesandroid.puzzles.liarliar

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LiarLiarGameState(game: LiarLiarGame) : CellsGameState<LiarLiarGame, LiarLiarGameMove, LiarLiarGameState>(game) {
    var objArray = Array(rows * cols) { LiarLiarObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: LiarLiarObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: LiarLiarObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = LiarLiarObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: LiarLiarGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2hint[p] != null || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: LiarLiarGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2hint[p] != null) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            LiarLiarObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) LiarLiarObject.Marker else LiarLiarObject.Marked
            LiarLiarObject.Marked -> if (markerOption == MarkerOptions.MarkerLast) LiarLiarObject.Marker else LiarLiarObject.Empty
            LiarLiarObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) LiarLiarObject.Marked else LiarLiarObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 2/Liar Liar

        Summary
        Tiles on fire

        Description
        1. Mark some tiles according to these rules:
        2. Cells with numbers are never marked.
        3. A number in a cell indicates how many marked cells must be placed.
           adjacent to its four sides.
        4. However, in each region there is one (and only one) wrong number
           (it shows a wrong amount of marked cells).
        5. Two marked cells must not be orthogonally adjacent.
        6. All of the non-marked cells must be connected.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] == LiarLiarObject.Forbidden)
                    this[r, c] = LiarLiarObject.Empty
        // 3. A number in a cell indicates how many marked cells must be placed.
        //    adjacent to its four sides.
        for ((p, n1) in game.pos2hint) {
            val n2 = LiarLiarGame.offset.count {
                val p2 = p + it
                isValid(p2) && this[p2] == LiarLiarObject.Marked
            }
            val s = if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[p] = s
        }
        for (area in game.areas) {
            var nComplete = 0
            var nError = 0
            for (p in area)
                if (this[p] == LiarLiarObject.Hint)
                    if (pos2stateHint[p] == HintState.Complete) nComplete++ else nError++
            if (nError != 1) isSolved = false
        }
        // 5. Two marked cells must not be orthogonally adjacent.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] != LiarLiarObject.Marked) continue
                val rng = LiarLiarGame.offset.map { p + it }.filter { p ->
                    isValid(p) && this[p] == LiarLiarObject.Marked
                }
                if (rng.isEmpty())
                    pos2stateAllowed[p] = AllowedObjectState.Normal
                else {
                    isSolved = false
                    pos2stateAllowed[p] = AllowedObjectState.Error
                    for (p in rng)
                        pos2stateAllowed[p] = AllowedObjectState.Error
                }
                if (!allowedObjectsOnly) continue
                for (os in LiarLiarGame.offset) {
                    val p2 = p + os
                    if (isValid(p2) && this[p2] == LiarLiarObject.Empty)
                        this[p2] = LiarLiarObject.Forbidden
                }
            }
        if (!isSolved) return
        // 6. All of the non-marked cells must be connected.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == LiarLiarObject.Marked) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node)
            for (i in 0 until 4) {
                val p2 = p + LiarLiarGame.offset[i]
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}