package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class SentinelsGameState(game: SentinelsGame) : CellsGameState<SentinelsGame, SentinelsGameMove, SentinelsGameState>(game) {
    var objArray = Array<SentinelsObject>(rows * cols) { SentinelsEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: SentinelsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: SentinelsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = SentinelsHintObject()
        updateIsSolved()
    }

    override fun setObject(move: SentinelsGameMove): Boolean {
        if (this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    override fun switchObject(move: SentinelsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is SentinelsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) SentinelsMarkerObject() else SentinelsTowerObject()
            is SentinelsTowerObject -> if (markerOption == MarkerOptions.MarkerLast) SentinelsMarkerObject() else SentinelsEmptyObject()
            is SentinelsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) SentinelsTowerObject() else SentinelsEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/Sentinels

        Summary
        This time it's one Garden and many Towers

        Description
        1. On the Board there are a few sentinels. These sentinels are marked with
           a number.
        2. The number tells you how many tiles that Sentinel can control (see) from
           there vertically and horizontally. This includes the tile where he is
           located.
        3. You must put Towers on the Boards in accordance with these hints, keeping
           in mind that a Tower blocks the Sentinel View.
        4. The restrictions are that there must be a single continuous Garden, and
           two Towers can't touch horizontally or vertically.
        5. Towers can't go over numbered squares. But numbered squares don't block
           Sentinel View.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val o = this[r, c]
                if (o is SentinelsTowerObject)
                    o.state = AllowedObjectState.Normal
                else {
                    if (o is SentinelsForbiddenObject)
                        this[r, c] = SentinelsEmptyObject()
                    val p = Position(r, c)
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        // 4. two Towers can't touch horizontally or vertically.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                fun hasNeighbor(): Boolean {
                    for (os in SentinelsGame.offset) {
                        val p2 = p + os
                        if (isValid(p2) && this[p2] is SentinelsTowerObject)
                            return true
                    }
                    return false
                }
                val o = this[r, c]
                if (o is SentinelsTowerObject)
                    o.state = if (o.state == AllowedObjectState.Normal && !hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is SentinelsEmptyObject || o is SentinelsMarkerObject) && allowedObjectsOnly && hasNeighbor())
                    this[r, c] = SentinelsForbiddenObject()
            }
        // 2. The number tells you how many tiles that Sentinel can control (see) from
        // there vertically and horizontally. This includes the tile where he is
        // located.
        for ((p, n2) in game.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0 until 4) {
                val os = SentinelsGame.offset[i]
                var p2 = p + os
                while (isValid(p2)) {
                    val o2 = this[p2]
                    if (o2 is SentinelsTowerObject) continue@next
                    if (o2 is SentinelsEmptyObject)
                        rng.add(+p2)
                    nums[i]++
                    p2 += os
                }
            }
            val n1 = nums[0] + nums[1] + nums[2] + nums[3] + 1
            pos2state[p] = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2)
                isSolved = false
            else
                for (p2 in rng)
                    this[p2] = SentinelsForbiddenObject()
        }
        if (!isSolved) return
        for ((p, node) in pos2node) {
            for (os in SentinelsGame.offset) {
                val p2 = p + os
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
        }
        // 4. There must be a single continuous Garden
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}