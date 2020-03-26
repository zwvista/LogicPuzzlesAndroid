package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.P2
import fj.data.List
import fj.data.Stream
import fj.function.Integers
import java.util.*

class LightBattleShipsGameState(game: LightBattleShipsGame) : CellsGameState<LightBattleShipsGame?, LightBattleShipsGameMove?, LightBattleShipsGameState?>(game) {
    var objArray: Array<LightBattleShipsObject?>
    var pos2state: MutableMap<Position?, HintState?> = HashMap()
    operator fun get(row: Int, col: Int): LightBattleShipsObject? {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position?): LightBattleShipsObject? {
        return get(p!!.row, p.col)
    }

    operator fun set(row: Int, col: Int, obj: LightBattleShipsObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position?, obj: LightBattleShipsObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: LightBattleShipsGameMove?): Boolean {
        val p = move!!.p
        if (!isValid(p) || game!!.pos2obj.containsKey(p) || get(p) === move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: LightBattleShipsGameMove?): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: LightBattleShipsObject? ->
            if (obj is LightBattleShipsEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) LightBattleShipsMarkerObject() else LightBattleShipsBattleShipUnitObject() else if (obj is LightBattleShipsBattleShipUnitObject) return@label LightBattleShipsBattleShipMiddleObject() else if (obj is LightBattleShipsBattleShipMiddleObject) return@label LightBattleShipsBattleShipLeftObject() else if (obj is LightBattleShipsBattleShipLeftObject) return@label LightBattleShipsBattleShipTopObject() else if (obj is LightBattleShipsBattleShipTopObject) return@label LightBattleShipsBattleShipRightObject() else if (obj is LightBattleShipsBattleShipRightObject) return@label LightBattleShipsBattleShipBottomObject() else if (obj is LightBattleShipsBattleShipBottomObject) return@label if (markerOption == MarkerOptions.MarkerLast) LightBattleShipsMarkerObject() else LightBattleShipsEmptyObject() else if (obj is LightBattleShipsMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) LightBattleShipsBattleShipUnitObject() else LightBattleShipsEmptyObject()
            obj
        }
        val p = move!!.p
        if (!isValid(p)) return false
        move.obj = f.f(get(p))
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Light Battle Ships

        Summary
        Please divert your course 15 degrees to avoid collision

        Description
        1. A mix of Battle Ships and Lighthouses, you have to guess the usual
           piece of ships with the help of Lighthouses.
        2. Each number is a Lighthouse, telling you how many pieces of ship
           there are in that row and column, summed together.
        3. Ships cannot touch each other OR touch Lighthouses. Not even diagonally.
        4. In each puzzle there are
           1 Aircraft Carrier (4 squares)
           2 Destroyers (3 squares)
           3 Submarines (2 squares)
           4 Patrol boats (1 square)

        Variant
        5. Some puzzle can also have a:
           1 Supertanker (5 squares)
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game!!.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) if (get(r, c) is LightBattleShipsForbiddenObject) set(r, c, LightBattleShipsEmptyObject())
        // 3. Ships cannot touch Lighthouses. Not even diagonally.
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val hasNeighbor = label@ F { isHint: Boolean ->
                for (os in LightBattleShipsGame.Companion.offset) {
                    val p2 = p.add(os)
                    if (!isValid(p2)) continue
                    val o = get(p2)
                    if (o is LightBattleShipsHintObject) {
                        if (!isHint) return@label true
                    } else if (o is LightBattleShipsBattleShipTopObject || o is LightBattleShipsBattleShipBottomObject ||
                        o is LightBattleShipsBattleShipLeftObject || o is LightBattleShipsBattleShipRightObject ||
                        o is LightBattleShipsBattleShipMiddleObject || o is LightBattleShipsBattleShipUnitObject) {
                        if (isHint) return@label true
                    }
                }
                false
            }
            val o = get(r, c)
            if (o is LightBattleShipsHintObject) {
                val s = if (!hasNeighbor.f(true)) HintState.Normal else HintState.Error
                o.state = s
                if (s == HintState.Error) isSolved = false
            } else if ((o is LightBattleShipsEmptyObject || o is LightBattleShipsMarkerObject) && allowedObjectsOnly && hasNeighbor.f(false)) set(r, c, LightBattleShipsForbiddenObject())
        }
        // 2. Each number is a Lighthouse, telling you how many pieces of ship
        // there are in that row and column, summed together.
        for ((p, n2) in game!!.pos2hint) {
            val nums = arrayOf(0, 0, 0, 0)
            val rng: MutableList<Position> = ArrayList()
            for (i in 0..3) {
                val os: Position = LightBattleShipsGame.Companion.offset.get(i * 2)
                val p2 = p!!.add(os)
                while (game!!.isValid(p2)) {
                    val o = get(p2)
                    if (o is LightBattleShipsEmptyObject) rng.add(p2.plus()) else if (o is LightBattleShipsBattleShipTopObject || o is LightBattleShipsBattleShipBottomObject ||
                        o is LightBattleShipsBattleShipLeftObject || o is LightBattleShipsBattleShipRightObject ||
                        o is LightBattleShipsBattleShipMiddleObject || o is LightBattleShipsBattleShipUnitObject) nums[i]++
                    p2.addBy(os)
                }
            }
            val n1 = fj.data.Array.array(*nums).foldLeft(Integers.add, 0)
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false else if (allowedObjectsOnly) for (p2 in rng) set(p2, LightBattleShipsForbiddenObject())
        }
        val g = Graph()
        val pos2node: MutableMap<Position, Node> = HashMap()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o = get(p)
            if (o is LightBattleShipsBattleShipTopObject || o is LightBattleShipsBattleShipBottomObject ||
                o is LightBattleShipsBattleShipLeftObject || o is LightBattleShipsBattleShipRightObject ||
                o is LightBattleShipsBattleShipMiddleObject || o is LightBattleShipsBattleShipUnitObject) {
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for ((p, node) in pos2node) {
            for (os in LightBattleShipsGame.Companion.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        }
        val shipNumbers = arrayOf(0, 0, 0, 0, 0)
        val shipNumbers2 = arrayOf(0, 4, 3, 2, 1)
        while (!pos2node.isEmpty()) {
            g.setRootNode(List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter { e: P2<Position, Node> -> nodeList.contains(e._2()) }.map { e: P2<Position, Node> -> e._1() }.toJavaList()
            for (p in area) pos2node.remove(p)
            Collections.sort(area) { obj: Position, other: Position? -> obj.compareTo(other) }
            if (!(area.size == 1 && get(area[0]) is LightBattleShipsBattleShipUnitObject ||
                    area.size > 1 && area.size < 5 && (List.iterableList(area).forall { p: Position -> p.row == area[0].row } &&
                    get(area[0]) is LightBattleShipsBattleShipLeftObject &&
                    get(area[area.size - 1]) is LightBattleShipsBattleShipRightObject ||
                    List.iterableList(area).forall { p: Position -> p.col == area[0].col } &&
                    get(area[0]) is LightBattleShipsBattleShipTopObject &&
                    get(area[area.size - 1]) is LightBattleShipsBattleShipBottomObject) &&
                    Stream.range(1, area.size - 2.toLong()).forall { i: Int? -> get(area[i!!]) is LightBattleShipsBattleShipMiddleObject })) {
                isSolved = false
                continue
            }
            for (p in area) for (os in LightBattleShipsGame.Companion.offset) {
                // 3. Ships cannot touch each other. Not even diagonally.
                val p2 = p.add(os)
                if (!isValid(p2) || area.contains(p2)) continue
                val o = get(p2)
                if (!(o is LightBattleShipsEmptyObject || o is LightBattleShipsForbiddenObject || o is LightBattleShipsMarkerObject)) isSolved = false else if (allowedObjectsOnly) set(p, LightBattleShipsForbiddenObject())
            }
            shipNumbers[area.size]++
        }
        // 4. In each puzzle there are
        //    1 Aircraft Carrier (4 squares)
        //    2 Destroyers (3 squares)
        //    3 Submarines (2 squares)
        //    4 Patrol boats (1 square)
        if (!Arrays.equals(shipNumbers, shipNumbers2)) isSolved = false
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        Arrays.fill(objArray, LightBattleShipsEmptyObject())
        for ((p) in game.pos2hint) {
            val o = LightBattleShipsHintObject()
            set(p, o)
        }
        for ((p, o) in game.pos2obj) {
            set(p, o)
        }
        updateIsSolved()
    }
}