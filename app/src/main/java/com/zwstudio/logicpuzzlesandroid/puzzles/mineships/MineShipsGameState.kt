package com.zwstudio.logicpuzzlesandroid.puzzles.mineships

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class MineShipsGameState(game: MineShipsGame) : CellsGameState<MineShipsGame, MineShipsGameMove, MineShipsGameState>(game) {
    val objArray = Array<MineShipsObject>(rows * cols) { MineShipsObject.Empty }
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: MineShipsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: MineShipsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = MineShipsObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: MineShipsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2hint.containsKey(p) || this[p] === move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: MineShipsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            MineShipsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) MineShipsObject.Marker else MineShipsObject.BattleShipUnit
            MineShipsObject.BattleShipUnit -> MineShipsObject.BattleShipMiddle
            MineShipsObject.BattleShipMiddle -> MineShipsObject.BattleShipLeft
            MineShipsObject.BattleShipLeft -> MineShipsObject.BattleShipTop
            MineShipsObject.BattleShipTop -> MineShipsObject.BattleShipRight
            MineShipsObject.BattleShipRight -> MineShipsObject.BattleShipBottom
            MineShipsObject.BattleShipBottom -> if (markerOption == MarkerOptions.MarkerLast) MineShipsObject.Marker else MineShipsObject.Empty
            MineShipsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) MineShipsObject.BattleShipUnit else MineShipsObject.Empty
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

        Variant
        5. Some puzzle can also have a:
           1 Supertanker (5 squares)
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                if (this[r, c] == MineShipsObject.Forbidden)
                    this[r, c] = MineShipsObject.Empty
        // 3. A number tells you how many pieces of ship are around it.
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            val rng = mutableListOf<Position>()
            for (os in MineShipsGame.offset2) {
                val p2 = p + os
                if (!isValid(p2)) continue
                val o = this[p2]
                if (o.isShipPiece)
                    n1++
                else if (o == MineShipsObject.Empty)
                    rng.add(+p2)
            }
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2)
                isSolved = false
            else if (allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = MineShipsObject.Forbidden
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
            for (os in MineShipsGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        val shipNumbers = mutableListOf(0, 0, 0, 0, 0)
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.keys.toList().sorted()
            for (p in area)
                pos2node.remove(p)
            if (!(area.size == 1 && this[area[0]] == MineShipsObject.BattleShipUnit || area.size in 2..4 && (
                    area.all { it.row == area[0].row } &&
                        this[area[0]] == MineShipsObject.BattleShipLeft && this[area.last()] == MineShipsObject.BattleShipRight ||
                    area.all { it.col == area[0].col } &&
                        this[area[0]] == MineShipsObject.BattleShipTop && this[area.last()] == MineShipsObject.BattleShipBottom) &&
                    (1..<area.size - 1).all { this[area[it]] == MineShipsObject.BattleShipMiddle })) {
                isSolved = false
                continue
            }
            for (p in area)
                for (os in MineShipsGame.offset2) {
                    // A ship or piece of ship can't touch another, not even diagonally.
                    val p2 = p + os
                    if (!isValid(p2) || area.contains(p2)) continue
                    val o = this[p2]
                    if (o == MineShipsObject.Empty || o == MineShipsObject.Marker) {
                        if (allowedObjectsOnly)
                            this[p2] = MineShipsObject.Forbidden
                    } else if (!(o == MineShipsObject.Forbidden || o == MineShipsObject.Hint))
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