package com.zwstudio.logicpuzzlesandroid.puzzles.guesstris

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GuesstrisGameState(game: GuesstrisGame) : CellsGameState<GuesstrisGame, GuesstrisGameMove, GuesstrisGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: GuesstrisGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + GuesstrisGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: GuesstrisGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[move.p][move.dir]) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 3/Guesstris

        Summary
        Encoded Tetris

        Description
        1. Divide the board in Tetrominoes (Tetris-like shapes of four cells).
        2. Each Tetromino contains two different symbols.
        3. Tetrominoes of the same shape have the same couple of symbols inside
           them, although not necessarily in the same positions.
        4. Tetrominoes with the same symbols can be rotated or mirrored.
    */
    private fun updateIsSolved() {
        isSolved = true
        val areas = mutableListOf<List<Position>>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                for (i in 0 until 4)
                    if (this[p + GuesstrisGame.offset2[i]][GuesstrisGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + GuesstrisGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.pos2char[it] != ' ' }
            // 1. Divide the board in Tetrominoes (Tetris-like shapes of four cells).
            // 2. Each Tetromino contains two different symbols.
            if (rng.size == 2 && area.size == 4)
                areas.add(area)
            else {
                isSolved = false; return
            }
        }
        // 3. Tetrominoes of the same shape have the same couple of symbols inside
        //    them, although not necessarily in the same positions.
        // 4. Tetrominoes with the same symbols can be rotated or mirrored.
        val area2D = areas.groupBy { area ->
            var r1 = rows
            var c1 = cols
            for (p in area) {
                if (r1 > p.row) r1 = p.row
                if (c1 > p.col) c1 = p.col
            }
            val p1 = Position (r1, c1)
            val area2 = area.map { it - p1 }.sorted().toTypedArray()
            GuesstrisGame.tetrominoes.indices.first {
                GuesstrisGame.tetrominoes[it].any { it.contentEquals(area2) }
            }
        }.values
        if (!area2D.all {
            val lst = it.map { String(it.map { game.pos2char[it]!! }.filter { it != ' ' }.toSortedSet().toCharArray()) }
            lst.toSet().size == 1
        }) isSolved = false
    }
}