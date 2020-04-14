package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.P2
import fj.data.List
import fj.data.Stream
import java.util.*

class DigitalBattleShipsGameState(game: DigitalBattleShipsGame?) : CellsGameState<DigitalBattleShipsGame?, DigitalBattleShipsGameMove?, DigitalBattleShipsGameState?>(game) {
    var objArray: Array<DigitalBattleShipsObject?>
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: DigitalBattleShipsObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: DigitalBattleShipsObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: DigitalBattleShipsGameMove?): Boolean {
        val p = move!!.p
        if (!isValid(p) || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: DigitalBattleShipsGameMove?): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: DigitalBattleShipsObject? ->
            when (obj) {
                DigitalBattleShipsObject.Empty -> return@label if (markerOption == MarkerOptions.MarkerFirst) DigitalBattleShipsObject.Marker else DigitalBattleShipsObject.BattleShipUnit
                DigitalBattleShipsObject.BattleShipUnit -> return@label DigitalBattleShipsObject.BattleShipMiddle
                DigitalBattleShipsObject.BattleShipMiddle -> return@label DigitalBattleShipsObject.BattleShipLeft
                DigitalBattleShipsObject.BattleShipLeft -> return@label DigitalBattleShipsObject.BattleShipTop
                DigitalBattleShipsObject.BattleShipTop -> return@label DigitalBattleShipsObject.BattleShipRight
                DigitalBattleShipsObject.BattleShipRight -> return@label DigitalBattleShipsObject.BattleShipBottom
                DigitalBattleShipsObject.BattleShipBottom -> return@label if (markerOption == MarkerOptions.MarkerLast) DigitalBattleShipsObject.Marker else DigitalBattleShipsObject.Empty
                DigitalBattleShipsObject.Marker -> return@label if (markerOption == MarkerOptions.MarkerFirst) DigitalBattleShipsObject.BattleShipUnit else DigitalBattleShipsObject.Empty
            }
            obj
        }
        val p = move!!.p
        if (!isValid(p)) return false
        move.obj = f.f(get(p))
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Digital Battle Ships

        Summary
        Please divert your course 12+1+2 to avoid collision

        Description
        1. Play like Solo Battle Ships, with a difference.
        2. Each number on the outer board tells you the SUM of the ship or
           ship pieces you're seeing in that row or column.
        3. A ship or ship piece is worth the number it occupies on the board.
        4. Standard rules apply: a ship or piece of ship can't touch another,
           not even diagonally.
        5. In each puzzle there are
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
        for (r in 0 until rows()) for (c in 0 until cols()) if (get(r, c) == DigitalBattleShipsObject.Forbidden) set(r, c, DigitalBattleShipsObject.Empty)
        // 2. Each number on the outer board tells you the SUM of the ship or
        // ship pieces you're seeing in that row.
        for (r in 0 until rows()) {
            var n1 = 0
            val n2 = game!!.row2hint[r]
            for (c in 0 until cols()) {
                val o = get(r, c)
                if (o == DigitalBattleShipsObject.BattleShipTop || o == DigitalBattleShipsObject.BattleShipBottom || o == DigitalBattleShipsObject.BattleShipLeft || o == DigitalBattleShipsObject.BattleShipRight || o == DigitalBattleShipsObject.BattleShipMiddle || o == DigitalBattleShipsObject.BattleShipUnit) // 3. A ship or ship piece is worth the number it occupies on the board.
                    n1 += game!![r, c]
            }
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        // 2. Each number on the outer board tells you the SUM of the ship or
        // ship pieces you're seeing in that column.
        for (c in 0 until cols()) {
            var n1 = 0
            val n2 = game!!.col2hint[c]
            for (r in 0 until rows()) {
                val o = get(r, c)
                if (o == DigitalBattleShipsObject.BattleShipTop || o == DigitalBattleShipsObject.BattleShipBottom || o == DigitalBattleShipsObject.BattleShipLeft || o == DigitalBattleShipsObject.BattleShipRight || o == DigitalBattleShipsObject.BattleShipMiddle || o == DigitalBattleShipsObject.BattleShipUnit) // 3. A ship or ship piece is worth the number it occupies on the board.
                    n1 += game!![r, c]
            }
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o = get(r, c)
            if ((o == DigitalBattleShipsObject.Empty || o == DigitalBattleShipsObject.Marker) && allowedObjectsOnly && (row2state[r] != HintState.Normal || col2state[c] != HintState.Normal)) set(r, c, DigitalBattleShipsObject.Forbidden)
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o = get(p)
            if (o == DigitalBattleShipsObject.BattleShipTop || o == DigitalBattleShipsObject.BattleShipBottom || o == DigitalBattleShipsObject.BattleShipLeft || o == DigitalBattleShipsObject.BattleShipRight || o == DigitalBattleShipsObject.BattleShipMiddle || o == DigitalBattleShipsObject.BattleShipUnit) {
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for ((p, node) in pos2node) {
            for (i in 0..3) {
                val p2 = p.add(DigitalBattleShipsGame.Companion.offset.get(i * 2))
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
            if (!(area.size == 1 && get(area[0]) == DigitalBattleShipsObject.BattleShipUnit ||
                    area.size > 1 && area.size < 5 && (List.iterableList(area).forall { p: Position -> p.row == area[0].row } && get(area[0]) == DigitalBattleShipsObject.BattleShipLeft && get(area[area.size - 1]) == DigitalBattleShipsObject.BattleShipRight ||
                    List.iterableList(area).forall { p: Position -> p.col == area[0].col } && get(area[0]) == DigitalBattleShipsObject.BattleShipTop && get(area[area.size - 1]) == DigitalBattleShipsObject.BattleShipBottom) &&
                    Stream.range(1, area.size - 2.toLong()).forall { i: Int? -> get(area[i!!]) == DigitalBattleShipsObject.BattleShipMiddle })) {
                isSolved = false
                continue
            }
            for (p in area) for (os in DigitalBattleShipsGame.Companion.offset) {
                // 4. A ship or piece of ship can't touch another, not even diagonally.
                val p2 = p.add(os)
                if (!isValid(p2) || area.contains(p2)) continue
                val o = get(p2)
                if (!(o == DigitalBattleShipsObject.Empty || o == DigitalBattleShipsObject.Forbidden || o == DigitalBattleShipsObject.Marker)) isSolved = false else if (allowedObjectsOnly) set(p, DigitalBattleShipsObject.Forbidden)
            }
            shipNumbers[area.size]++
        }
        // 5. In each puzzle there are
        //    1 Aircraft Carrier (4 squares)
        //    2 Destroyers (3 squares)
        //    3 Submarines (2 squares)
        //    4 Patrol boats (1 square)
        if (!Arrays.equals(shipNumbers, shipNumbers2)) isSolved = false
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        Arrays.fill(objArray, DigitalBattleShipsObject.Empty)
        row2state = arrayOfNulls(rows())
        col2state = arrayOfNulls(cols())
        updateIsSolved()
    }
}