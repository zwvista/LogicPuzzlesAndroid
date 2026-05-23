package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.BattleShipsGame

class LightBattleShipsGameState(game: LightBattleShipsGame) : CellsGameState<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState>(game) {
    val objArray = Array(rows * cols) { LightBattleShipsObject.Empty }
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: LightBattleShipsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: LightBattleShipsObject) {this[p.row, p.col] = obj}

    init {
        for ((p, o) in game.pos2obj)
            this[p] = o
        updateIsSolved()
    }

    override fun setObject(move: LightBattleShipsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2obj.containsKey(p) || this[p] === move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: LightBattleShipsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2obj.containsKey(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            LightBattleShipsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) LightBattleShipsObject.Marker else LightBattleShipsObject.BattleShipUnit
            LightBattleShipsObject.BattleShipUnit -> LightBattleShipsObject.BattleShipMiddle
            LightBattleShipsObject.BattleShipMiddle -> LightBattleShipsObject.BattleShipLeft
            LightBattleShipsObject.BattleShipLeft -> LightBattleShipsObject.BattleShipTop
            LightBattleShipsObject.BattleShipTop -> LightBattleShipsObject.BattleShipRight
            LightBattleShipsObject.BattleShipRight -> LightBattleShipsObject.BattleShipBottom
            LightBattleShipsObject.BattleShipBottom -> if (markerOption == MarkerOptions.MarkerLast) LightBattleShipsObject.Marker else LightBattleShipsObject.Empty
            LightBattleShipsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) LightBattleShipsObject.BattleShipUnit else LightBattleShipsObject.Empty
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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                if (this[r, c] == LightBattleShipsObject.Forbidden)
                    this[r, c] = LightBattleShipsObject.Empty
        // 3. Ships cannot touch Lighthouses. Not even diagonally.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                fun touchHint(isHint: Boolean) =
                    LightBattleShipsGame.offset2.any {
                        val p2 = p + it
                        if (!isValid(p2))
                            false
                        else {
                            val o = this[p2]
                            !isHint && o == LightBattleShipsObject.Hint || isHint && o.isShipPiece
                        }
                    }
                val o = this[p]
                if (o == LightBattleShipsObject.Hint) {
                    val s = if (!touchHint(true)) HintState.Normal else HintState.Error
                    pos2state[p] = s
                    if (s == HintState.Error) isSolved = false
                } else if ((o == LightBattleShipsObject.Empty || o == LightBattleShipsObject.Marker) &&
                    allowedObjectsOnly && touchHint(false))
                    this[p] = LightBattleShipsObject.Forbidden
            }
        // 2. Each number is a Lighthouse, telling you how many pieces of ship
        // there are in that row and column, summed together.
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            val rng = mutableListOf<Position>()
            for (os in BattleShipsGame.offset) {
                var p2 = p + os
                while (isValid(p2)) {
                    val o = this[p2]
                    if (o == LightBattleShipsObject.Empty)
                        rng.add(+p2)
                    else if (o.isShipPiece)
                        n1++
                    p2 += os
                }
            }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (pos2state[p] != HintState.Error)
                pos2state[p] = s
            if (s != HintState.Complete)
                isSolved = false
            else if (allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = LightBattleShipsObject.Forbidden
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o.isShipPiece) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in LightBattleShipsGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        val shipNumbers = mutableListOf(0, 0, 0, 0, 0)
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { (_, node) -> nodeList.contains(node) }.keys.toList().sorted()
            for (p in area)
                pos2node.remove(p)
            if (!(area.size == 1 && this[area[0]] == LightBattleShipsObject.BattleShipUnit || area.size in 2..4 && (
                    area.all { it.row == area[0].row } &&
                        this[area[0]] == LightBattleShipsObject.BattleShipLeft && this[area.last()] == LightBattleShipsObject.BattleShipRight ||
                    area.all { it.col == area[0].col } &&
                        this[area[0]] == LightBattleShipsObject.BattleShipTop && this[area.last()] == LightBattleShipsObject.BattleShipBottom) &&
                    (1..<area.size - 1).all { this[area[it]] == LightBattleShipsObject.BattleShipMiddle })) {
                isSolved = false
                continue
            }
            for (p in area)
                for (os in LightBattleShipsGame.offset2) {
                    // 3. Ships cannot touch each other. Not even diagonally.
                    val p2 = p + os
                    if (!isValid(p2) || area.contains(p2)) continue
                    if (this[p2].isShipPiece)
                        isSolved = false
                    else if (allowedObjectsOnly)
                        this[p2] = LightBattleShipsObject.Forbidden
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