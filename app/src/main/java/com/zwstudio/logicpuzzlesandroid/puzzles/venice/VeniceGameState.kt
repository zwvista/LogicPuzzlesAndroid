package com.zwstudio.logicpuzzlesandroid.puzzles.venice

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class VeniceGameState(game: VeniceGame) : CellsGameState<VeniceGame, VeniceGameMove, VeniceGameState>(game) {
    var objArray = Array<VeniceObject>(rows * cols) { VeniceEmptyObject }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: VeniceObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: VeniceObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = VeniceHintObject()
        updateIsSolved()
    }

    override fun setObject(move: VeniceGameMove): GameOperationType {
        if (this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: VeniceGameMove): GameOperationType {
        val p = move.p
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is VeniceEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) VeniceMarkerObject else VeniceTowerObject()
            is VeniceTowerObject -> if (markerOption == MarkerOptions.MarkerLast) VeniceMarkerObject else VeniceEmptyObject
            is VeniceMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) VeniceTowerObject() else VeniceEmptyObject
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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val o = this[r, c]
                if (o is VeniceTowerObject)
                    o.state = AllowedObjectState.Normal
                else {
                    if (o is VeniceForbiddenObject)
                        this[r, c] = VeniceEmptyObject
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
                    for (os in VeniceGame.offset) {
                        val p2 = p + os
                        if (isValid(p2) && this[p2] is VeniceTowerObject)
                            return true
                    }
                    return false
                }
                val o = this[r, c]
                if (o is VeniceTowerObject)
                    o.state = if (o.state == AllowedObjectState.Normal && !hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is VeniceEmptyObject || o is VeniceMarkerObject) && allowedObjectsOnly && hasNeighbor())
                    this[r, c] = VeniceForbiddenObject
            }
        // 2. The number tells you how many tiles that Sentinel can control (see) from
        // there vertically and horizontally. This includes the tile where he is
        // located.
        for ((p, n2) in game.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0 until 4) {
                val os = VeniceGame.offset[i]
                var p2 = p + os
                while (isValid(p2)) {
                    val o2 = this[p2]
                    if (o2 is VeniceTowerObject) continue@next
                    if (o2 is VeniceEmptyObject)
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
                    this[p2] = VeniceForbiddenObject
        }
        if (!isSolved) return
        for ((p, node) in pos2node) {
            for (os in VeniceGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        }
        // 4. There must be a single continuous Garden
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}