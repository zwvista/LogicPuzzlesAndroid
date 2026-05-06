package com.zwstudio.logicpuzzlesandroid.puzzles.floweromino

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FlowerOMinoGameState(game: FlowerOMinoGame) : CellsGameState<FlowerOMinoGame, FlowerOMinoGameMove, FlowerOMinoGameState>(game) {
    var objArray = Cloner().deepClone(game.dots.objArray)
    var pos2state = mutableMapOf<Position, AllowedObjectState>()
    var gardens = mutableListOf<List<Position>>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int, dir: Int) = objArray[row * cols + col][dir]
    operator fun get(p: Position, dir: Int) = this[p.row, p.col, dir]
    operator fun set(row: Int, col: Int, dir: Int, obj: GridLineObject) {objArray[row * cols + col][dir] = obj}
    operator fun set(p: Position, dir: Int, obj: GridLineObject) {this[p.row, p.col, dir] = obj}

    override fun setObject(move: FlowerOMinoGameMove): GameOperationType {
        val p1 = move.p
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        if (game.dots[p1, dir] != GridLineObject.Empty) return GameOperationType.Invalid
        val o = this[p1, dir]
        if (o == move.obj) return GameOperationType.Invalid
        val p2 = p1 + FlowerOMinoGame.offset[dir]
        this[p1, dir] = move.obj
        this[p2, dir2] = this[p1, dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FlowerOMinoGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[move.p, move.dir]) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 1/Flower-O-Mino

        Summary
        Don't tread On flowers, Often.

        Description
        1. You are a gardener. you've been employed by a sour weird lady.
        2. This lady, after years of having her garden grow wild with flowers
           and without enclosures, decided that those are way too many flowers
           and too few enclosures.
        3. So now being an avid fan of Tetris, she asked you to divide the garden
           in many Tetris shaped mini-gardens.
        4. And while doing that they HAVE to tread, destroy and plow as many
           flowers as you can, provide you leave just the one in each Tetris
           mini-garden.
        5. Divide the board in tetrominos (4-tile pieces). Each tetromino should
           have only one flower inside it.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                // 5. Green squares are blocks that can't be included in flower beds.
                if (game[p] != FlowerOMinoObject.Hedge) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (i in 0..<4) {
                if (this[p + FlowerOMinoGame.offset2[i], FlowerOMinoGame.dirs[i]] == GridLineObject.Line) continue
                val p2 = p + FlowerOMinoGame.offset[i]
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            if (area.size != 4) { isSolved = false; continue }
            gardens.add(area)
            val n2 = 1
            val n1 = area.fold(0) { acc, p ->
                var m = 0
                val o = game[p]
                if (o.hasCenter) m++
                if (o.hasRight && area.contains(p + FlowerOMinoGame.offset[1])) m++
                if (o.hasBottom && area.contains(p + FlowerOMinoGame.offset[2])) m++
                acc + m
            }
            val s = if (n1 == n2) AllowedObjectState.Normal else AllowedObjectState.Error
            for (p in area) pos2state[p] = s
            if (s == AllowedObjectState.Error) isSolved = false
        }
    }
}
