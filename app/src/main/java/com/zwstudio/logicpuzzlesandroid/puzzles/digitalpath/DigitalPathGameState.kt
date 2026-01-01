package com.zwstudio.logicpuzzlesandroid.puzzles.digitalpath

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DigitalPathGameState(game: DigitalPathGame) : CellsGameState<DigitalPathGame, DigitalPathGameMove, DigitalPathGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: DigitalPathGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != DigitalPathGame.PUZ_EMPTY || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: DigitalPathGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != DigitalPathGame.PUZ_EMPTY) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val nMax = rows
        move.obj = when (val o = this[p]) {
            DigitalPathGame.PUZ_EMPTY -> if (markerOption == MarkerOptions.MarkerFirst) DigitalPathGame.PUZ_MARKER else 1
            DigitalPathGame.PUZ_MARKER -> if (markerOption == MarkerOptions.MarkerFirst) 1 else DigitalPathGame.PUZ_EMPTY
            else -> if (o == nMax) if (markerOption == MarkerOptions.MarkerLast) DigitalPathGame.PUZ_MARKER else DigitalPathGame.PUZ_EMPTY else o + 1
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 2/Digital Path

        Summary
        Nurikabe for robots

        Description
        1. Fill some tiles with numbers. The numbers form a Nurikabe, that is
           a path interconnected horizontally or vertically and which can' t
           cover a 2x2 area.
        2. All numbers in an area must be the same and all of them must be
           equal to the number of those numbers in the area.
        3. All regions must have at least one number.
        4. Two orthogonally adjacent tiles across areas must be different.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                pos2state[p] = HintState.Normal
                if (this[p] == DigitalPathGame.PUZ_FORBIDDEN)
                    this[p] = DigitalPathGame.PUZ_EMPTY
            }
        for (area in game.areas) {
            val num2range = mutableMapOf<Int, MutableList<Position>>()
            for (p in area) {
                val n = this[p]
                if (n == 0) continue
                num2range.getOrPut(n) { mutableListOf() }.add(p)
                // 4. Two orthogonally adjacent tiles across areas must be different.
                for (os in DigitalPathGame.offset) {
                    val p2 = p + os
                    if (!isValid(p2)) continue
                    val n2 = this[p2]
                    if (game.pos2area[p] != game.pos2area[p2] && n == n2) {
                        isSolved = false
                        pos2state[p] = HintState.Error
                        pos2state[p2] = HintState.Error
                    }
                }
            }
            // 2. All numbers in an area must be the same and all of them must be
            //    equal to the number of those numbers in the area.
            // 3. All regions must have at least one number.
            if (num2range.size != 1 || num2range.keys.first() != num2range.values.first().size) {
                isSolved = false
                for ((_, range) in num2range)
                    for (p in range) 
                        pos2state[p] = HintState.Error
            }
        }
        // 1. The numbers can' t cover a 2x2 area.
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                if (DigitalPathGame.offset3.all { this[p + it] > 0 }) {
                    isSolved = false
                    for (os in DigitalPathGame.offset3)
                        pos2state[p + os] = HintState.Error
                }
            }
        if (!isSolved) return
        // 1. Fill some tiles with numbers. The numbers form a Nurikabe, that is
        //    a path interconnected horizontally or vertically.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = this[p]
                if (n == 0) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node)
            for (i in 0 until 4) {
                val p2 = p + DigitalPathGame.offset[i]
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}