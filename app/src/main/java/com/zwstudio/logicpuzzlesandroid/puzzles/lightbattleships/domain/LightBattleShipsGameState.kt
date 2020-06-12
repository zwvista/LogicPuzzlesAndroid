package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class LightBattleShipsGameState(game: LightBattleShipsGame) : CellsGameState<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState>(game) {
    var objArray = Array<LightBattleShipsObject>(rows() * cols()) { LightBattleShipsEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: LightBattleShipsObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: LightBattleShipsObject) {this[p.row, p.col] = obj}

    init {
        for ((p, _) in game.pos2hint)
            this[p] = LightBattleShipsHintObject()
        for ((p, o) in game.pos2obj)
            this[p] = o
        updateIsSolved()
    }

    fun setObject(move: LightBattleShipsGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game.pos2obj.containsKey(p) || this[p] === move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: LightBattleShipsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val p = move.p
        val o = this[p]
        move.obj = when (o) {
            is LightBattleShipsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) LightBattleShipsMarkerObject() else LightBattleShipsBattleShipUnitObject()
            is LightBattleShipsBattleShipUnitObject -> LightBattleShipsBattleShipMiddleObject()
            is LightBattleShipsBattleShipMiddleObject -> LightBattleShipsBattleShipLeftObject()
            is LightBattleShipsBattleShipLeftObject -> LightBattleShipsBattleShipTopObject()
            is LightBattleShipsBattleShipTopObject -> LightBattleShipsBattleShipRightObject()
            is LightBattleShipsBattleShipRightObject -> LightBattleShipsBattleShipBottomObject()
            is LightBattleShipsBattleShipBottomObject -> if (markerOption == MarkerOptions.MarkerLast) LightBattleShipsMarkerObject() else LightBattleShipsEmptyObject()
            is LightBattleShipsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) LightBattleShipsBattleShipUnitObject() else LightBattleShipsEmptyObject()
            else -> o
        }
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
        for (r in 0 until rows())
            for (c in 0 until cols())
                if (this[r, c] is LightBattleShipsForbiddenObject)
                    this[r, c] = LightBattleShipsEmptyObject()
        // 3. Ships cannot touch Lighthouses. Not even diagonally.
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                fun hasNeighbor(isHint: Boolean): Boolean {
                    for (os in LightBattleShipsGame.offset) {
                        val p2 = p.add(os)
                        if (!isValid(p2)) continue
                        val o = this[p2]
                        if (o is LightBattleShipsHintObject) {
                            if (!isHint) return true
                        } else if (o.isShipPiece()) {
                            if (isHint) return true
                        }
                    }
                    return false
                }
                val o = get(r, c)
                if (o is LightBattleShipsHintObject) {
                    val s = if (!hasNeighbor(true)) HintState.Normal else HintState.Error
                    o.state = s
                    if (s == HintState.Error) isSolved = false
                } else if ((o is LightBattleShipsEmptyObject || o is LightBattleShipsMarkerObject) && allowedObjectsOnly && hasNeighbor(false))
                    this[r, c] = LightBattleShipsForbiddenObject()
            }
        // 2. Each number is a Lighthouse, telling you how many pieces of ship
        // there are in that row and column, summed together.
        for ((p, n2) in game!!.pos2hint) {
            val nums = arrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            for (i in 0..3) {
                val os: Position = LightBattleShipsGame.offset[i * 2]
                val p2 = p.add(os)
                while (game!!.isValid(p2)) {
                    val o = get(p2)
                    if (o is LightBattleShipsEmptyObject)
                        rng.add(p2.plus())
                    else if (o.isShipPiece())
                        nums[i]++
                    p2.addBy(os)
                }
            }
            val n1 = nums.sum()
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2)
                isSolved = false
            else if (allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = LightBattleShipsForbiddenObject()
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val o = get(p)
                if (o.isShipPiece()) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in LightBattleShipsGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
        }
        val shipNumbers = arrayOf(0, 0, 0, 0, 0)
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { (_, node) -> nodeList.contains(node) }.keys.toList().sorted()
            for (p in area)
                pos2node.remove(p)
            if (!(area.size == 1 && this[area[0]] is LightBattleShipsBattleShipUnitObject || area.size in 2..4 && (
                    area.all { it.row == area[0].row } &&
                        this[area[0]] is LightBattleShipsBattleShipLeftObject && this[area[area.size - 1]] is LightBattleShipsBattleShipRightObject ||
                    area.all { it.col == area[0].col } &&
                        this[area[0]] is LightBattleShipsBattleShipTopObject && this[area[area.size - 1]] is LightBattleShipsBattleShipBottomObject) &&
                    (1 until area.size - 1).all { this[area[it]] is LightBattleShipsBattleShipMiddleObject })) {
                isSolved = false
                continue
            }
            for (p in area)
                for (os in LightBattleShipsGame.offset) {
                    // 3. Ships cannot touch each other. Not even diagonally.
                    val p2 = p.add(os)
                    if (!isValid(p2) || area.contains(p2)) continue
                    if (this[p2].isShipPiece())
                        isSolved = false
                    else if (allowedObjectsOnly)
                        this[p2] = LightBattleShipsForbiddenObject()
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