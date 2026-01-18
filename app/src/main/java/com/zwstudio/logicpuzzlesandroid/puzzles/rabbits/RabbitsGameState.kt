package com.zwstudio.logicpuzzlesandroid.puzzles.rabbits

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class RabbitsGameState(game: RabbitsGame) : CellsGameState<RabbitsGame, RabbitsGameMove, RabbitsGameState>(game) {
    var objArray = Array<RabbitsObject>(rows * cols) { RabbitsEmptyObject }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: RabbitsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: RabbitsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = RabbitsHintObject()
        updateIsSolved()
    }

    override fun setObject(move: RabbitsGameMove): GameOperationType {
        if (this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: RabbitsGameMove): GameOperationType {
        val p = move.p
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is RabbitsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) RabbitsMarkerObject else RabbitsTowerObject()
            is RabbitsTowerObject -> if (markerOption == MarkerOptions.MarkerLast) RabbitsMarkerObject else RabbitsEmptyObject
            is RabbitsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) RabbitsTowerObject() else RabbitsEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 3/Rabbits

        Summary
        Rabbit 'n' Seek

        Description
        1. The board represents a lawn where Rabbits are playing Hide 'n' Seek,
           behind Trees.
        2. Each number tells you how many Rabbits can be seen from that tile,
           in an horizontal and vertical line.
        3. Tree hide Rabbits, numbers don't.
        4. Each row and column has exactly one Tree and one Rabbit.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val o = this[r, c]
                if (o is RabbitsTowerObject)
                    o.state = AllowedObjectState.Normal
                else {
                    if (o is RabbitsForbiddenObject)
                        this[r, c] = RabbitsEmptyObject
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
                    for (os in RabbitsGame.offset) {
                        val p2 = p + os
                        if (isValid(p2) && this[p2] is RabbitsTowerObject)
                            return true
                    }
                    return false
                }
                val o = this[r, c]
                if (o is RabbitsTowerObject)
                    o.state = if (o.state == AllowedObjectState.Normal && !hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is RabbitsEmptyObject || o is RabbitsMarkerObject) && allowedObjectsOnly && hasNeighbor())
                    this[r, c] = RabbitsForbiddenObject
            }
        // 2. The number tells you how many tiles that Sentinel can control (see) from
        // there vertically and horizontally. This includes the tile where he is
        // located.
        for ((p, n2) in game.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0 until 4) {
                val os = RabbitsGame.offset[i]
                var p2 = p + os
                while (isValid(p2)) {
                    val o2 = this[p2]
                    if (o2 is RabbitsTowerObject) continue@next
                    if (o2 is RabbitsEmptyObject)
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
                    this[p2] = RabbitsForbiddenObject
        }
        if (!isSolved) return
        for ((p, node) in pos2node) {
            for (os in RabbitsGame.offset) {
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