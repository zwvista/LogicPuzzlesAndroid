package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class BattleShipsGameState(game: BattleShipsGame) : CellsGameState<BattleShipsGame, BattleShipsGameMove, BattleShipsGameState>(game) {
    var objArray = Array(rows * cols) { BattleShipsObject.Empty }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    init {
        for ((p, o) in game.pos2obj)
            this[p] = o
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BattleShipsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: BattleShipsObject) {this[p.row, p.col] = obj}

    fun setObject(move: BattleShipsGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game.pos2obj.containsKey(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: BattleShipsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return false
        val o = this[p]
        move.obj = when (o) {
            BattleShipsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) BattleShipsObject.Marker else BattleShipsObject.BattleShipUnit
            BattleShipsObject.BattleShipUnit -> BattleShipsObject.BattleShipMiddle
            BattleShipsObject.BattleShipMiddle -> BattleShipsObject.BattleShipLeft
            BattleShipsObject.BattleShipLeft -> BattleShipsObject.BattleShipTop
            BattleShipsObject.BattleShipTop -> BattleShipsObject.BattleShipRight
            BattleShipsObject.BattleShipRight -> BattleShipsObject.BattleShipBottom
            BattleShipsObject.BattleShipBottom -> if (markerOption == MarkerOptions.MarkerLast) BattleShipsObject.Marker else BattleShipsObject.Empty
            BattleShipsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) BattleShipsObject.BattleShipUnit else BattleShipsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Battle Ships

        Summary
        Play solo Battleships, with the help of the numbers on the border.

        Description
        1. Standard rules of Battleships apply, but you are guessing the other
           player ships disposition, by using the numbers on the borders.
        2. Each number tells you how many ship or ship pieces you're seeing in
           that row or column.
        3. Standard rules apply: a ship or piece of ship can't touch another,
           not even diagonally.
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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] == BattleShipsObject.Forbidden)
                    this[r, c] = BattleShipsObject.Empty
        // 2. Each number tells you how many ship or ship pieces you're seeing in that row.
        for (r in 0 until rows) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0 until cols)
                if (this[r, c].isShipPiece())
                    n1++
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        // 2. Each number tells you how many ship or ship pieces you're seeing in that column.
        for (c in 0 until cols) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0 until rows)
                if (this[r, c].isShipPiece())
                    n1++
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val o = this[r, c]
                if ((o == BattleShipsObject.Empty || o == BattleShipsObject.Marker) &&
                    allowedObjectsOnly && (row2state[r] != HintState.Normal || col2state[c] != HintState.Normal))
                    this[r, c] = BattleShipsObject.Forbidden
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p].isShipPiece()) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (i in 0 until 4) {
                val p2 = p.add(BattleShipsGame.offset[i * 2])
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
        val shipNumbers = mutableListOf(0, 0, 0, 0, 0)
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.keys.toList().sorted()
            for (p in area)
                pos2node.remove(p)
            if (!(area.size == 1 && this[area[0]] == BattleShipsObject.BattleShipUnit || area.size in 2..4 && (
                    area.all { it.row == area[0].row } &&
                        this[area[0]] == BattleShipsObject.BattleShipLeft && this[area[area.size - 1]] == BattleShipsObject.BattleShipRight ||
                    area.all { it.col == area[0].col } &&
                        this[area[0]] == BattleShipsObject.BattleShipTop && this[area[area.size - 1]] == BattleShipsObject.BattleShipBottom) &&
                    (1 until area.size - 1).all { this[area[it]] == BattleShipsObject.BattleShipMiddle })) {
                isSolved = false
                continue
            }
            for (p in area)
                for (os in BattleShipsGame.offset) {
                    // 3. A ship or piece of ship can't touch another, not even diagonally.
                    val p2 = p.add(os)
                    if (!isValid(p2) || area.contains(p2)) continue
                    if (this[p2].isShipPiece())
                        isSolved = false
                    else if (allowedObjectsOnly)
                        this[p2] = BattleShipsObject.Forbidden
                }
            shipNumbers[area.size]++
        }
        // 4. In each puzzle there are
        //    1 Aircraft Carrier (4 squares)
        //    2 Destroyers (3 squares)
        //    3 Submarines (2 squares)
        //    4 Patrol boats (1 square)
        if (shipNumbers != listOf(0, 4, 3, 2, 1)) isSolved = false
    }
}
