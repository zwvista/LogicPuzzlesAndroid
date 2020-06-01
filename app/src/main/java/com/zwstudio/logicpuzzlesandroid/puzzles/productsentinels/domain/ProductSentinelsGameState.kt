package com.zwstudio.logicpuzzlesandroid.puzzles.productsentinels.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.data.List
import java.util.*

class ProductSentinelsGameState(game: ProductSentinelsGame) : CellsGameState<ProductSentinelsGame, ProductSentinelsGameMove, ProductSentinelsGameState>(game) {
    var objArray: Array<ProductSentinelsObject?>
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: ProductSentinelsObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: ProductSentinelsObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: ProductSentinelsGameMove): Boolean {
        if (get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: ProductSentinelsGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<ProductSentinelsObject, ProductSentinelsObject> = label@ F<ProductSentinelsObject, ProductSentinelsObject> { obj: ProductSentinelsObject? ->
            if (obj is ProductSentinelsEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) ProductSentinelsMarkerObject() else ProductSentinelsTowerObject()
            if (obj is ProductSentinelsTowerObject) return@label if (markerOption == MarkerOptions.MarkerLast) ProductSentinelsMarkerObject() else ProductSentinelsEmptyObject()
            if (obj is ProductSentinelsMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) ProductSentinelsTowerObject() else ProductSentinelsEmptyObject()
            obj
        }
        val o: ProductSentinelsObject? = get(move.p)
        move.obj = f.f(o)
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
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o: ProductSentinelsObject? = get(r, c)
            if (o is ProductSentinelsTowerObject) o.state = AllowedObjectState.Normal else {
                if (o is ProductSentinelsForbiddenObject) set(r, c, ProductSentinelsEmptyObject())
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for (p in pos2node.keys) for (os in ProductSentinelsGame.offset) {
            val p2 = p.add(os)
            if (pos2node.containsKey(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        // 4. two Towers can't touch horizontally or vertically.
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val hasNeighbor: F0<Boolean> = label@ F0<Boolean> {
                for (os in ProductSentinelsGame.offset) {
                    val p2 = p.add(os)
                    if (isValid(p2) && get(p2) is ProductSentinelsTowerObject) return@label true
                }
                false
            }
            val o: ProductSentinelsObject? = get(r, c)
            if (o is ProductSentinelsTowerObject) {
                val o2: ProductSentinelsTowerObject = o
                o2.state = if (o2.state == AllowedObjectState.Normal && !hasNeighbor.f()) AllowedObjectState.Normal else AllowedObjectState.Error
            } else if ((o is ProductSentinelsEmptyObject || o is ProductSentinelsMarkerObject) &&
                allowedObjectsOnly && hasNeighbor.f()) set(r, c, ProductSentinelsForbiddenObject())
        }
        // 2. The number tells you the product of the tiles that Sentinel can control
        // (see) from there vertically and horizontally. This includes the tile
        // where he is located.
        for ((p, n2) in game.pos2hint.entries) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0..3) {
                val os: Position = ProductSentinelsGame.offset.get(i)
                val p2 = p.add(os)
                while (isValid(p2)) {
                    val o2: ProductSentinelsObject? = get(p2)
                    if (o2 is ProductSentinelsTowerObject) continue@next
                    if (o2 is ProductSentinelsEmptyObject) rng.add(p2.plus())
                    nums[i]++
                    p2.addBy(os)
                }
            }
            val n1 = (nums[0] + nums[2] + 1) * (nums[1] + nums[3] + 1)
            pos2state[p] = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false else for (p2 in rng) set(p2, ProductSentinelsForbiddenObject())
        }
        if (!isSolved) return
        // 4. There must be a single continuous Garden
        g.setRootNode(List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }

    init {
        objArray = arrayOfNulls<ProductSentinelsObject>(rows() * cols())
        Arrays.fill(objArray, ProductSentinelsEmptyObject())
        for (p in game.pos2hint.keys) set(p, ProductSentinelsHintObject())
        updateIsSolved()
    }
}