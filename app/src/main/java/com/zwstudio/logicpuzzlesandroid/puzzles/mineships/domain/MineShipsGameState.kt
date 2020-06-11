package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class MineShipsGameState(game: MineShipsGame) : CellsGameState<MineShipsGame, MineShipsGameMove, MineShipsGameState>(game) {
    var objArray = Array<MineShipsObject>(rows() * cols()) { MineShipsEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: MineShipsObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: MineShipsObject) {this[p.row, p.col] = obj}

    init {
        for ((p, _) in game.pos2hint)
            this[p] = MineShipsHintObject()
        updateIsSolved()
    }

    fun setObject(move: MineShipsGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game.pos2hint.containsKey(p) || this[p] === move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: MineShipsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()]
        val p = move.p
        if (!isValid(p)) return false
        val o = this[p]
        move.obj = when(o) {
            is MineShipsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) MineShipsMarkerObject() else MineShipsBattleShipUnitObject()
            is MineShipsBattleShipUnitObject -> MineShipsBattleShipMiddleObject()
            is MineShipsBattleShipMiddleObject -> MineShipsBattleShipLeftObject()
            is MineShipsBattleShipLeftObject -> MineShipsBattleShipTopObject()
            is MineShipsBattleShipTopObject -> MineShipsBattleShipRightObject()
            is MineShipsBattleShipRightObject -> MineShipsBattleShipBottomObject()
            is MineShipsBattleShipBottomObject -> if (markerOption == MarkerOptions.MarkerLast) MineShipsMarkerObject() else MineShipsEmptyObject()
            is MineShipsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) MineShipsBattleShipUnitObject() else MineShipsEmptyObject()
            else -> o
        }
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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows())
            for (c in 0 until cols())
                if (this[r, c] is MineShipsForbiddenObject)
                    this[r, c] = MineShipsEmptyObject()
        // 3. A number tells you how many pieces of ship are around it.
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            val rng = mutableListOf<Position>()
            for (os in MineShipsGame.offset) {
                val p2 = p.add(os)
                if (!isValid(p2)) continue
                val o = this[p2]
                if (o.isShipPiece())
                    n1++
                else if (o is MineShipsEmptyObject)
                    rng.add(p2.plus())
            }
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2)
                isSolved = false
            else if (allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = MineShipsForbiddenObject()
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val o = this[p]
                if (o.isShipPiece()) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (i in 0..3) {
                val p2 = p.add(MineShipsGame.offset[i * 2])
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
        }
        val shipNumbers = mutableListOf(0, 0, 0, 0, 0)
        while (pos2node.isNotEmpty()) {
            g.setRootNode(pos2node.values.first())
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.keys.toList().sorted()
            for (p in area)
                pos2node.remove(p)
            if (!(area.size == 1 && this[area[0]] is MineShipsBattleShipUnitObject || area.size in 2..4 && (
                    area.all { it.row == area[0].row } &&
                        this[area[0]] is MineShipsBattleShipLeftObject && this[area[area.size - 1]] is MineShipsBattleShipRightObject ||
                    area.all { it.col == area[0].col } &&
                        this[area[0]] is MineShipsBattleShipTopObject && this[area[area.size - 1]] is MineShipsBattleShipBottomObject) &&
                    (1 until area.size - 1).all { this[area[it]] is MineShipsBattleShipMiddleObject })) {
                isSolved = false
                continue
            }
            for (p in area)
                for (os in MineShipsGame.offset) {
                    // A ship or piece of ship can't touch another, not even diagonally.
                    val p2 = p.add(os)
                    if (!isValid(p2) || area.contains(p2)) continue
                    val o = this[p2]
                    if (o is MineShipsEmptyObject || o is MineShipsMarkerObject) {
                        if (allowedObjectsOnly)
                            this[p2] = MineShipsForbiddenObject()
                    } else if (!(o is MineShipsForbiddenObject || o is MineShipsHintObject))
                        isSolved = false
                }
                shipNumbers[area.size]++
            }
        // In each puzzle there are
        //    1 Aircraft Carrier (4 squares)
        //    2 Destroyers (3 squares)
        //    3 Submarines (2 squares)
        //    4 Patrol boats (1 square)
        if (shipNumbers != listOf(0, 4, 3, 2, 1)) isSolved = false
    }
}