package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class ProductSentinelsGameState(game: ProductSentinelsGame) : CellsGameState<ProductSentinelsGame, ProductSentinelsGameMove, ProductSentinelsGameState>(game) {
    var objArray = Array(rows * cols) { ProductSentinelsObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ProductSentinelsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: ProductSentinelsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = ProductSentinelsObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: ProductSentinelsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ProductSentinelsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            ProductSentinelsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) ProductSentinelsObject.Marker else ProductSentinelsObject.Tower
            ProductSentinelsObject.Tower -> if (markerOption == MarkerOptions.MarkerLast) ProductSentinelsObject.Marker else ProductSentinelsObject.Empty
            ProductSentinelsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) ProductSentinelsObject.Tower else ProductSentinelsObject.Empty
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
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == ProductSentinelsObject.Tower)
                    pos2stateAllowed[p] = AllowedObjectState.Normal
                else {
                    if (o == ProductSentinelsObject.Forbidden)
                        this[p] = ProductSentinelsObject.Empty
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in ProductSentinelsGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        // 4. two Towers can't touch horizontally or vertically.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                fun hasNeighbor(): Boolean =
                    ProductSentinelsGame.offset.any {
                        val p2 = p + it
                        isValid(p2) && this[p2] == ProductSentinelsObject.Tower
                    }
                val o = this[p]
                if (o == ProductSentinelsObject.Tower)
                    pos2stateAllowed[p] = if (pos2stateAllowed[p] == AllowedObjectState.Normal && !hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o == ProductSentinelsObject.Empty || o == ProductSentinelsObject.Marker) && allowedObjectsOnly && hasNeighbor())
                    this[p] = ProductSentinelsObject.Forbidden
            }
        // 2. The number tells you the product of the tiles that Sentinel can control
        // (see) from there vertically and horizontally. This includes the tile
        // where he is located.
        for ((p, n2) in game.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0..<4) {
                val os = ProductSentinelsGame.offset[i]
                var p2 = p + os
                while (isValid(p2)) {
                    val o2 = this[p2]
                    if (o2 == ProductSentinelsObject.Tower) continue@next
                    if (o2 == ProductSentinelsObject.Empty)
                        rng.add(+p2)
                    nums[i]++
                    p2 += os
                }
            }
            val n1 = (nums[0] + nums[2] + 1) * (nums[1] + nums[3] + 1)
            pos2stateHint[p] = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2)
                isSolved = false
            else
                for (p2 in rng)
                    this[p2] = ProductSentinelsObject.Forbidden
        }
        if (!isSolved) return
        // 4. There must be a single continuous Garden
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}