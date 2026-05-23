package com.zwstudio.logicpuzzlesandroid.puzzles.adifferentfarmer

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ADifferentFarmerGameState(game: ADifferentFarmerGame) : CellsGameState<ADifferentFarmerGame, ADifferentFarmerGameMove, ADifferentFarmerGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: ADifferentFarmerObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: ADifferentFarmerObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: ADifferentFarmerGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != ADifferentFarmerObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ADifferentFarmerGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ADifferentFarmerObject.Empty) return GameOperationType.Invalid
        move.obj = when (this[p]) {
            ADifferentFarmerObject.Empty -> ADifferentFarmerObject.Fv1
            ADifferentFarmerObject.Fv1 -> ADifferentFarmerObject.Fv2
            ADifferentFarmerObject.Fv2 -> ADifferentFarmerObject.Fv3
            ADifferentFarmerObject.Fv3 -> ADifferentFarmerObject.Empty
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 4/A different Farmer

        Summary
        Not all farmers are created equal

        Description
        1. A Different Farmer plants fruits and vegetables in a different way.
        2. He places exactly one of each of the three fruits or vegetables in each field
           (marked area).
        3. The same plant cannot be placed in adjacent tiles, not even diagonally.
        4. All the plants must be connected horizontally or vertically.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 2. He places exactly one of each of the three fruits or vegetables in each field
        //    (marked area).
        for (area in game.areas) {
            val obj2range = mutableMapOf<ADifferentFarmerObject, MutableList<Position>>()
            for (p in area) {
                val o = this[p]
                if (o == ADifferentFarmerObject.Empty) continue
                obj2range.getOrPut(o) { mutableListOf() }.add(p)
            }
            if (obj2range.size != 3)
                isSolved = false
            for ((_, range) in obj2range)
                if (range.size > 1) {
                    isSolved = false
                    for (p in range)
                        pos2state[p] = AllowedObjectState.Error
                }
        }
        // 3. The same plant cannot be placed in adjacent tiles, not even diagonally.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == ADifferentFarmerObject.Empty) continue
                for (os in ADifferentFarmerGame.offset3) {
                    val p2 = p + os
                    if (!isValid(p2)) continue
                    if (this[p2] == o) {
                        isSolved = false
                        pos2state[p] = AllowedObjectState.Error
                    }
                }
            }
        if (!isSolved) return
        // 4. All the plants must be connected horizontally or vertically.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == ADifferentFarmerObject.Empty) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node)
            for (os in ADifferentFarmerGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}
