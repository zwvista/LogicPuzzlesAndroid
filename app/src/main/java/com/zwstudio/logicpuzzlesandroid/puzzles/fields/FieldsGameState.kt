package com.zwstudio.logicpuzzlesandroid.puzzles.fields

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FieldsGameState(game: FieldsGame) : CellsGameState<FieldsGame, FieldsGameMove, FieldsGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: FieldsObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: FieldsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FieldsGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != FieldsObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FieldsGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != FieldsObject.Empty) return GameOperationType.Invalid
        val o = this[move.p]
        move.obj = if (o == FieldsObject.Empty) FieldsObject.Meadow else if (o == FieldsObject.Meadow) FieldsObject.Soil else FieldsObject.Empty
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 1/Fields

        Summary
        Twice of the blessings of a Nurikabe

        Description
        1. Fill the board with either meadows or soil, creating a path of soil
           and a path of meadows, with the same rules for each of them.
        2. The path is continuous and connected horizontally or vertically, but
           cannot touch diagonally.
        3. The path can't form 2x2 squares.
        4. These type of paths are called Nurikabe.
    */
    private fun updateIsSolved() {
        isSolved = true
        val meadows = mutableListOf<Position>()
        val soils = mutableListOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                pos2state[p] = AllowedObjectState.Normal
                when (this[p]) {
                    FieldsObject.Empty -> isSolved = false
                    FieldsObject.Meadow -> meadows.add(p)
                    FieldsObject.Soil -> soils.add(p)
                }
            }
        // 3. The path can't form 2x2 squares.
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val square = FieldsGame.offset2.map { p + it }
                val objSet = square.map { this[it] }.toSet()
                if (objSet.size == 1 && objSet.first() != FieldsObject.Empty) {
                    isSolved = false
                    for (p in square)
                        pos2state[p] = AllowedObjectState.Error
                }
            }
        // 1. Fill the board with either meadows or soil, creating a path of soil
        //    and a path of meadows, with the same rules for each of them.
        // 2. The path is continuous and connected horizontally or vertically, but
        //    cannot touch diagonally.
        for (fields in listOf(meadows, soils)) {
            val g = Graph()
            val pos2node = mutableMapOf<Position, Node>()
            for (p in fields) {
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
            for (p in pos2node.keys)
                for (os in FieldsGame.offset) {
                    val p2 = p + os
                    if (pos2node.containsKey(p2))
                        g.connectNode(pos2node[p]!!, pos2node[p2]!!)
                }
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            if (nodeList.size != pos2node.size) isSolved = false
        }
    }
}
