package com.zwstudio.logicpuzzlesandroid.puzzles.chocolate

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.CloudsGame

class ChocolateGameState(game: ChocolateGame) : CellsGameState<ChocolateGame, ChocolateGameMove, ChocolateGameState>(game) {
    var objArray = Array(rows * cols) { ChocolateObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ChocolateObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: ChocolateObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: ChocolateGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ChocolateGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            ChocolateObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) ChocolateObject.Marker else ChocolateObject.Chocolate
            ChocolateObject.Chocolate -> if (markerOption == MarkerOptions.MarkerLast) ChocolateObject.Marker else ChocolateObject.Empty
            ChocolateObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) ChocolateObject.Chocolate else ChocolateObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 6/Chocolate

        Summary
        Yummy!

        Description
        1. Find some chocolate bars following these rules:
        2. A Chocolate bar is a rectangular or a square.
        3. Chocolate tiles form bars independently of the area borders.
        4. Chocolate bars must not be orthogonally adjacent.
        5. A tile with a number indicates how many tiles in the area must
           be chocolate.
        6. An area without number can have any number of tiles of chocolate.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] == ChocolateObject.Forbidden)
                    this[r, c] = ChocolateObject.Empty
        // 2. A Chocolate bar is a rectangular or a square.
        // 3. Chocolate tiles form bars independently of the area borders.
        // 4. Chocolate bars must not be orthogonally adjacent.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] != ChocolateObject.Chocolate) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (p in pos2node.keys)
            for (os in CloudsGame.offset) {
                val p2 = p + os
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            var r2 = 0
            var r1 = rows
            var c2 = 0
            var c1 = cols
            val bar = mutableListOf<Position>()
            for (node in nodeList) {
                val p = pos2node.filterValues { it == node }.keys.first()
                pos2node.remove(p)
                bar.add(p)
                if (r2 < p.row) r2 = p.row
                if (r1 > p.row) r1 = p.row
                if (c2 < p.col) c2 = p.col
                if (c1 > p.col) c1 = p.col
            }
            val rs = r2 - r1 + 1
            val cs = c2 - c1 + 1
            val s = if (rs * cs == nodeList.size) AllowedObjectState.Normal else AllowedObjectState.Error
            if (s != AllowedObjectState.Normal) isSolved = false
            for (p in bar)
                pos2stateAllowed[p] = s
        }
        // 5. A tile with a number indicates how many tiles in the area must
        //    be chocolate.
        // 6. An area without number can have any number of tiles of chocolate.
        for (area in game.areas) {
            val pHint = area.firstOrNull { game.pos2hint.contains(it) } ?: continue
            val n2 = game.pos2hint[pHint]!!
            val n1 = area.filter { this[it] == ChocolateObject.Chocolate }.size
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            pos2stateHint[pHint] = s
            if (!(allowedObjectsOnly && s != HintState.Normal)) continue
            val empties = area.filter { this[it] == ChocolateObject.Empty }
            for (p in empties)
                this[p] = ChocolateObject.Forbidden
        }
    }
}