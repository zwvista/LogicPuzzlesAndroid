package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenclouds

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.CloudsGame

class HiddenCloudsGameState(game: HiddenCloudsGame) : CellsGameState<HiddenCloudsGame, HiddenCloudsGameMove, HiddenCloudsGameState>(game) {
    val objArray = Array(rows * cols) { HiddenCloudsObject.Empty }
    val pos2stateHint = mutableMapOf<Position, HintState>()
    val pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: HiddenCloudsObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: HiddenCloudsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: HiddenCloudsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: HiddenCloudsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            HiddenCloudsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) HiddenCloudsObject.Marker else HiddenCloudsObject.Cloud
            HiddenCloudsObject.Cloud -> if (markerOption == MarkerOptions.MarkerLast) HiddenCloudsObject.Marker else HiddenCloudsObject.Empty
            HiddenCloudsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) HiddenCloudsObject.Cloud else HiddenCloudsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 7/Hidden Clouds

        Summary
        Hide and Seek in the sky

        Description
        1. Try to find the clouds.
        2. Clouds have a square form (even of one single tile) and can't touch
           each other horizontally or vertically.
        3. Clouds of the same size cannot see each other horizontally or vertically,
           that is, there must be other Clouds between them
           (horizontally or vertically).
        4. Numbers indicate the total number of clouds tiles in the tile itself
           and in the four tiles around it (up down left right)
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                if (this[r, c] == HiddenCloudsObject.Forbidden)
                    this[r, c] = HiddenCloudsObject.Empty
        // 2. Clouds have a square form (even of one single tile) and can't touch
        //    each other horizontally or vertically.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] != HiddenCloudsObject.Cloud) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node)
            for (os in CloudsGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            var (r1, r2) = rows to 0
            var (c1, c2) = cols to 0
            val cloud = mutableListOf<Position>()
            for (node in nodeList) {
                val p = pos2node.filterValues { it == node }.keys.first()
                pos2node.remove(p)
                cloud.add(p)
                if (r2 < p.row) r2 = p.row
                if (r1 > p.row) r1 = p.row
                if (c2 < p.col) c2 = p.col
                if (c1 > p.col) c1 = p.col
            }
            val rs = r2 - r1 + 1
            val cs = c2 - c1 + 1
            val s = if (rs * cs == nodeList.size) AllowedObjectState.Normal else AllowedObjectState.Error
            if (s != AllowedObjectState.Normal) isSolved = false
            for (p in cloud)
                pos2stateAllowed[p] = s
        }
        // 4. Numbers indicate the total number of clouds tiles in the tile itself
        //    and in the four tiles around it (up down left right)
        for ((p, n2) in game.pos2hint) {
            val rng = HiddenCloudsGame.offset2.map { p + it }.filter { isValid(it) }
            val n1 = rng.filter { this[it] == HiddenCloudsObject.Cloud }.size
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            pos2stateHint[p] = s
            if (!(allowedObjectsOnly && s != HintState.Normal)) continue
            val empties = rng.filter { this[it] == HiddenCloudsObject.Empty }
            for (p in empties)
                this[p] = HiddenCloudsObject.Forbidden
        }
    }
}