package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.P2
import fj.data.List
import fj.data.Stream
import java.util.*

class MineShipsGameState(game: MineShipsGame) : CellsGameState<MineShipsGame?, MineShipsGameMove?, MineShipsGameState?>(game) {
    var objArray: Array<MineShipsObject?>
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position?) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: MineShipsObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position?, obj: MineShipsObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: MineShipsGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.pos2hint.containsKey(p) || get(p) === move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: MineShipsGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<MineShipsObject, MineShipsObject> = label@ F<MineShipsObject, MineShipsObject> { obj: MineShipsObject? ->
            if (obj is MineShipsEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) MineShipsMarkerObject() else MineShipsBattleShipUnitObject() else if (obj is MineShipsBattleShipUnitObject) return@label MineShipsBattleShipMiddleObject() else if (obj is MineShipsBattleShipMiddleObject) return@label MineShipsBattleShipLeftObject() else if (obj is MineShipsBattleShipLeftObject) return@label MineShipsBattleShipTopObject() else if (obj is MineShipsBattleShipTopObject) return@label MineShipsBattleShipRightObject() else if (obj is MineShipsBattleShipRightObject) return@label MineShipsBattleShipBottomObject() else if (obj is MineShipsBattleShipBottomObject) return@label if (markerOption == MarkerOptions.MarkerLast) MineShipsMarkerObject() else MineShipsEmptyObject() else if (obj is MineShipsMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) MineShipsBattleShipUnitObject() else MineShipsEmptyObject()
            obj
        }
        val p: Position = move.p
        if (!isValid(p)) return false
        move.obj = f.f(get(p))
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 8/Mine Ships

        Summary
        Warning! Naval Mines in the water!

        Description
        1. There are actually no mines in the water, but this is a mix between
           Minesweeper and Battle Ships.
        2. You must find the same set of ships like 'Battle Ships'
           (1*4, 2*3, 3*2, 4*1).
        3. However this time the hints are given in the same form as 'Minesweeper',
           where a number tells you how many pieces of ship are around it.
        4. Usual Battle Ships rules apply!
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) if (get(r, c) is MineShipsForbiddenObject) set(r, c, MineShipsEmptyObject())
        // 3. A number tells you how many pieces of ship are around it.
        for ((p, n2) in game.pos2hint.entries) {
            var n1 = 0
            val rng = mutableListOf<Position>()
            for (os in MineShipsGame.Companion.offset) {
                val p2 = p.add(os)
                if (!isValid(p2)) continue
                val o: MineShipsObject? = get(p2)
                if (o is MineShipsBattleShipTopObject || o is MineShipsBattleShipBottomObject ||
                    o is MineShipsBattleShipLeftObject || o is MineShipsBattleShipRightObject ||
                    o is MineShipsBattleShipMiddleObject || o is MineShipsBattleShipUnitObject) n1++ else if (o is MineShipsEmptyObject) rng.add(p2.plus())
            }
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false else if (allowedObjectsOnly) for (p2 in rng) set(p2, MineShipsForbiddenObject())
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o: MineShipsObject? = get(p)
            if (o is MineShipsBattleShipTopObject || o is MineShipsBattleShipBottomObject ||
                o is MineShipsBattleShipLeftObject || o is MineShipsBattleShipRightObject ||
                o is MineShipsBattleShipMiddleObject || o is MineShipsBattleShipUnitObject) {
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for ((p, node) in pos2node) {
            for (i in 0..3) {
                val p2 = p.add(MineShipsGame.Companion.offset.get(i * 2))
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        }
        val shipNumbers = arrayOf(0, 0, 0, 0, 0)
        val shipNumbers2 = arrayOf(0, 4, 3, 2, 1)
        while (!pos2node.isEmpty()) {
            g.setRootNode(List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter(F<P2<Position, Node>, Boolean> { e: P2<Position?, Node?> -> nodeList.contains(e._2()) }).map<Position>(F<P2<Position, Node>, Position> { e: P2<Position?, Node?> -> e._1() }).toJavaList()
            for (p in area) pos2node.remove(p)
            Collections.sort(area) { obj: Position, other: Position? -> obj.compareTo(other) }
            if (!(area.size == 1 && get(area[0]) is MineShipsBattleShipUnitObject ||
                    area.size > 1 && area.size < 5 && (List.iterableList(area).forall(F<Position, Boolean> { p: Position -> p.row == area[0].row }) &&
                    get(area[0]) is MineShipsBattleShipLeftObject &&
                    get(area[area.size - 1]) is MineShipsBattleShipRightObject ||
                    List.iterableList(area).forall(F<Position, Boolean> { p: Position -> p.col == area[0].col }) &&
                    get(area[0]) is MineShipsBattleShipTopObject &&
                    get(area[area.size - 1]) is MineShipsBattleShipBottomObject) &&
                    Stream.range(1, area.size - 2.toLong()).forall(F<Int, Boolean> { i: Int? -> get(area[i!!]) is MineShipsBattleShipMiddleObject }))) {
                isSolved = false
                continue
            }
            for (p in area) for (os in MineShipsGame.Companion.offset) {
                // A ship or piece of ship can't touch another, not even diagonally.
                val p2 = p.add(os)
                if (!isValid(p2) || area.contains(p2)) continue
                val o: MineShipsObject? = get(p2)
                if (o is MineShipsEmptyObject || o is MineShipsMarkerObject) {
                    if (allowedObjectsOnly) set(p, MineShipsForbiddenObject())
                } else if (!(o is MineShipsForbiddenObject || o is MineShipsHintObject)) isSolved = false
            }
            shipNumbers[area.size]++
        }
        // In each puzzle there are
        //    1 Aircraft Carrier (4 squares)
        //    2 Destroyers (3 squares)
        //    3 Submarines (2 squares)
        //    4 Patrol boats (1 square)
        if (!Arrays.equals(shipNumbers, shipNumbers2)) isSolved = false
    }

    init {
        objArray = arrayOfNulls<MineShipsObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = MineShipsEmptyObject()
        for ((p) in game.pos2hint.entries) {
            val o = MineShipsHintObject()
            set(p, o)
        }
        updateIsSolved()
    }
}