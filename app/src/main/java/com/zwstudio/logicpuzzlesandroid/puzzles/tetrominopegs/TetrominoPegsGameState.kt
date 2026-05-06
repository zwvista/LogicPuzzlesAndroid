package com.zwstudio.logicpuzzlesandroid.puzzles.tetrominopegs

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TetrominoPegsGameState(game: TetrominoPegsGame) : CellsGameState<TetrominoPegsGame, TetrominoPegsGameMove, TetrominoPegsGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var tetros = mutableListOf<TetrominoPegsObject>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: TetrominoPegsGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + TetrominoPegsGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TetrominoPegsGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 2/Puzzle Set 5/Tetromino Pegs

        Summary
        Stuck in Tetris

        Description
        1. Divide the board into Tetrominoes, area of exactly four tiles, of a shape
           like the pieces of Tetris, that is: L, I, T, S or O.
        2. Wood cells are fixed pegs and aren't part of Tetrominoes.
        3. Tetrominoes may be rotated or mirrored.
        4. Two Tetrominoes sharing an edge must be different.
    */
    private fun updateIsSolved() {
        isSolved = true
        tetros.clear()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                for (i in 0..<4)
                    if (this[p + TetrominoPegsGame.offset2[i]][TetrominoPegsGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + TetrominoPegsGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            // 2. Wood cells are fixed pegs and aren't part of Tetrominoes.
            if (area.size == 1 && game.pegs.contains(area[0])) continue
            if (area.size != 4) { isSolved = false; continue }
            // 1. Divide the board into Tetrominoes, area of exactly four tiles, of a shape
            //    like the pieces of Tetris, that is: L, I, T, S or O.
            // 3. Tetrominoes may be rotated or mirrored.
            var (r1, c1) = rows to cols
            for (p in area) {
                if (r1 > p.row) r1 = p.row
                if (c1 > p.col) c1 = p.col
            }
            val p1 = Position(r1, c1)
            val area2 = area.map { it - p1 }.sorted()
            val n = TetrominoPegsGame.tetrominoes.indices.first {
                TetrominoPegsGame.tetrominoes[it].contains(area2)
            }
            tetros.add(TetrominoPegsObject(area, n))
        }
        // 4. Two Tetrominoes sharing an edge must be different.
        if (tetros.indices.any { index ->
            val t = tetros[index]
            t.rng.any { p ->
                TetrominoPegsGame.offset.any {
                    val p2 = p + it
                    val index2 = tetros.indices.firstOrNull {
                        tetros[it].rng.contains(p2)
                    }
                    index2 != null && index2 != index && tetros[index2].kind == t.kind
                }
            }
        }) isSolved = false
    }
}