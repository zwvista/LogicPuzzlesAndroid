package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class ProductSentinelsGameState(game: ProductSentinelsGame) : CellsGameState<ProductSentinelsGame, ProductSentinelsGameMove, ProductSentinelsGameState>(game) {
    var objArray = Array<ProductSentinelsObject>(rows() * cols()) { ProductSentinelsEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ProductSentinelsObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: ProductSentinelsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = ProductSentinelsHintObject()
        updateIsSolved()
    }

    fun setObject(move: ProductSentinelsGameMove): Boolean {
        if (this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: ProductSentinelsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is ProductSentinelsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) ProductSentinelsMarkerObject() else ProductSentinelsTowerObject()
            is ProductSentinelsTowerObject -> if (markerOption == MarkerOptions.MarkerLast) ProductSentinelsMarkerObject() else ProductSentinelsEmptyObject()
            is ProductSentinelsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) ProductSentinelsTowerObject() else ProductSentinelsEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
       iOS Game: Logic Games/Puzzle Set 11/Product Sentinels

       Summary
       Multiplicative Towers

       Description
       1. On the Board there are a few sentinels. These sentinels are marked with
          a number.
       2. The number tells you the product of the tiles that Sentinel can control
          (see) from there vertically and horizontally. This includes the tile
          where he is located.
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
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val o = this[r, c]
                if (o is ProductSentinelsTowerObject)
                    o.state = AllowedObjectState.Normal
                else {
                    if (o is ProductSentinelsForbiddenObject)
                        this[r, c] = ProductSentinelsEmptyObject()
                    val p = Position(r, c)
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for (p in pos2node.keys)
            for (os in ProductSentinelsGame.offset) {
                val p2 = p.add(os)
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p], pos2node[p2])
            }
        // 4. two Towers can't touch horizontally or vertically.
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                fun hasNeighbor(): Boolean {
                    for (os in ProductSentinelsGame.offset) {
                        val p2 = p.add(os)
                        if (isValid(p2) && this[p2] is ProductSentinelsTowerObject) return true
                    }
                    return false
                }
                val o = this[r, c]
                if (o is ProductSentinelsTowerObject)
                    o.state = if (o.state == AllowedObjectState.Normal && !hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is ProductSentinelsEmptyObject || o is ProductSentinelsMarkerObject) && allowedObjectsOnly && hasNeighbor())
                    this[r, c] = ProductSentinelsForbiddenObject()
            }
        // 2. The number tells you the product of the tiles that Sentinel can control
        // (see) from there vertically and horizontally. This includes the tile
        // where he is located.
        for ((p, n2) in game.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0..3) {
                val os = ProductSentinelsGame.offset[i]
                val p2 = p.add(os)
                while (isValid(p2)) {
                    val o2 = this[p2]
                    if (o2 is ProductSentinelsTowerObject) continue@next
                    if (o2 is ProductSentinelsEmptyObject)
                        rng.add(p2.plus())
                    nums[i]++
                    p2.addBy(os)
                }
            }
            val n1 = (nums[0] + nums[2] + 1) * (nums[1] + nums[3] + 1)
            pos2state[p] = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2)
                isSolved = false
            else
                for (p2 in rng)
                    this[p2] = ProductSentinelsForbiddenObject()
        }
        if (!isSolved) return
        // 4. There must be a single continuous Garden
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}