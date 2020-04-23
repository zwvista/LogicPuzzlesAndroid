package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.data.List
import java.util.*

class SentinelsGameState(game: SentinelsGame) : CellsGameState<SentinelsGame, SentinelsGameMove, SentinelsGameState>(game) {
    var objArray: Array<SentinelsObject?>
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: SentinelsObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: SentinelsObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: SentinelsGameMove): Boolean {
        if (get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: SentinelsGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<SentinelsObject, SentinelsObject> = label@ F<SentinelsObject, SentinelsObject> { obj: SentinelsObject? ->
            if (obj is SentinelsEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) SentinelsMarkerObject() else SentinelsTowerObject()
            if (obj is SentinelsTowerObject) return@label if (markerOption == MarkerOptions.MarkerLast) SentinelsMarkerObject() else SentinelsEmptyObject()
            if (obj is SentinelsMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) SentinelsTowerObject() else SentinelsEmptyObject()
            obj
        }
        val o: SentinelsObject? = get(move.p)
        move.obj = f.f(o)
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
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o: SentinelsObject? = get(r, c)
            if (o is SentinelsTowerObject) o.state = AllowedObjectState.Normal else {
                if (o is SentinelsForbiddenObject) set(r, c, SentinelsEmptyObject())
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        // 4. two Towers can't touch horizontally or vertically.
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val hasNeighbor: F0<Boolean> = label@ F0<Boolean> {
                for (os in SentinelsGame.Companion.offset) {
                    val p2 = p.add(os)
                    if (isValid(p2) && get(p2) is SentinelsTowerObject) return@label true
                }
                false
            }
            val o: SentinelsObject? = get(r, c)
            if (o is SentinelsTowerObject) {
                val o2: SentinelsTowerObject = o
                o2.state = if (o2.state == AllowedObjectState.Normal && !hasNeighbor.f()) AllowedObjectState.Normal else AllowedObjectState.Error
            } else if ((o is SentinelsEmptyObject || o is SentinelsMarkerObject) &&
                allowedObjectsOnly && hasNeighbor.f()) set(r, c, SentinelsForbiddenObject())
        }
        // 2. The number tells you how many tiles that Sentinel can control (see) from
        // there vertically and horizontally. This includes the tile where he is
        // located.
        for ((p, n2) in game.pos2hint.entries) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0..3) {
                val os: Position = SentinelsGame.Companion.offset.get(i)
                val p2 = p.add(os)
                while (isValid(p2)) {
                    val o2: SentinelsObject? = get(p2)
                    if (o2 is SentinelsTowerObject) continue@next
                    if (o2 is SentinelsEmptyObject) rng.add(p2.plus())
                    nums[i]++
                    p2.addBy(os)
                }
            }
            val n1 = nums[0] + nums[1] + nums[2] + nums[3] + 1
            pos2state[p] = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false else for (p2 in rng) set(p2, SentinelsForbiddenObject())
        }
        if (!isSolved) return
        for ((p, node) in pos2node) {
            for (os in SentinelsGame.Companion.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        }
        // 4. There must be a single continuous Garden
        g.setRootNode(List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }

    init {
        objArray = arrayOfNulls<SentinelsObject>(rows() * cols())
        Arrays.fill(objArray, SentinelsEmptyObject())
        for (p in game.pos2hint.keys) set(p, SentinelsHintObject())
        updateIsSolved()
    }
}