package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class CloudsGameState(game: CloudsGame) : CellsGameState<CloudsGame, CloudsGameMove, CloudsGameState>(game) {
    var objArray = Array(rows() * cols()) { CloudsObject.Empty }
    var row2state = Array(rows()) { HintState.Normal }
    var col2state = Array(cols()) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: CloudsObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: CloudsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: CloudsGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: CloudsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return false
        val o = this[p]
        move.obj = when (o) {
            CloudsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) CloudsObject.Marker else CloudsObject.Cloud
            CloudsObject.Cloud -> if (markerOption == MarkerOptions.MarkerLast) CloudsObject.Marker else CloudsObject.Empty
            CloudsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) CloudsObject.Cloud else CloudsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 5/Clouds

        Summary
        Weather Radar Report

        Description
        1. You must find Clouds in the sky.
        2. The hints on the borders tell you how many tiles are covered by Clouds
           in that row or column.
        3. Clouds only appear in rectangular or square areas. Furthermore, their
           width and height is always at least two tiles wide.
        4. Clouds can't touch between themselves, not even diagonally.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows())
            for (c in 0 until cols())
                if (this[r, c] == CloudsObject.Forbidden)
                    this[r, c] = CloudsObject.Empty
        // 2. The hints on the borders tell you how many tiles are covered by Clouds
        // in that row.
        for (r in 0 until rows()) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0 until cols())
                if (this[r, c] == CloudsObject.Cloud)
                    n1++
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        // 2. The hints on the borders tell you how many tiles are covered by Clouds
        // in that column.
        for (c in 0 until cols()) {
            var n1 = 0
            val n2 = game!!.col2hint[c]
            for (r in 0 until rows())
                if (this[r, c] == CloudsObject.Cloud)
                    n1++
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val o = this[r, c]
                if ((o == CloudsObject.Empty || o == CloudsObject.Marker) && allowedObjectsOnly && (row2state[r] != HintState.Normal || col2state[c] != HintState.Normal))
                    this[r, c] = CloudsObject.Forbidden
            }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                if (this[p] != CloudsObject.Cloud) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (p in pos2node.keys)
            for (os in CloudsGame.offset) {
                val p2 = p.add(os)
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p], pos2node[p2])
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            var r2 = 0
            var r1 = rows()
            var c2 = 0
            var c1 = cols()
            for (node in nodeList) {
                val p = pos2node.filterValues { it == node }.keys.first()
                pos2node.remove(p)
                if (r2 < p.row) r2 = p.row
                if (r1 > p.row) r1 = p.row
                if (c2 < p.col) c2 = p.col
                if (c1 > p.col) c1 = p.col
            }
            val rs = r2 - r1 + 1
            val cs = c2 - c1 + 1
            // 3. Clouds only appear in rectangular or square areas. Furthermore, their
            // width and height is always at least two tiles wide.
            if (!(rs >= 2 && cs >= 2 && rs * cs == nodeList.size)) {
                isSolved = false
                return
            }
        }
    }
}