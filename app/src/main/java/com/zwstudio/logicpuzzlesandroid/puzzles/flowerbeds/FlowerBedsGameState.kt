package com.zwstudio.logicpuzzlesandroid.puzzles.flowerbeds

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FlowerBedsGameState(game: FlowerBedsGame) : CellsGameState<FlowerBedsGame, FlowerBedsGameMove, FlowerBedsGameState>(game) {
    var objArray = Cloner().deepClone(game.dots.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int, dir: Int) = objArray[row * cols + col][dir]
    operator fun get(p: Position, dir: Int) = this[p.row, p.col, dir]
    operator fun set(row: Int, col: Int, dir: Int, obj: GridLineObject) {objArray[row * cols + col][dir] = obj}
    operator fun set(p: Position, dir: Int, obj: GridLineObject) {this[p.row, p.col, dir] = obj}

    override fun setObject(move: FlowerBedsGameMove): GameOperationType {
        val p1 = move.p
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        if (game.dots[p1, dir] != GridLineObject.Empty) return GameOperationType.Invalid
        val o = this[p1, dir]
        if (o == move.obj) return GameOperationType.Invalid
        val p2 = p1 + FlowerBedsGame.offset[dir]
        this[p1, dir] = move.obj
        this[p2, dir2] = this[p1, dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FlowerBedsGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 2/Puzzle Set 2/Flower Beds

        Summary
        Reverse Gardener

        Description
        1. The board represents a garden where flowers are scattered around.
        2. Your task as a gardener is to divide the garden in rectangular (or square)
           flower beds.
        3. Each flower bed should contain exactly one flower.
        4. Contiguous flower beds can't have the same area extension.
        5. Green squares are hedges that can't be included in flower beds.
    */
    private fun updateIsSolved() {
        isSolved = true
        val rects = mutableListOf<FlowerBedsRect>()
        val pos2rect = mutableMapOf<Position, Int>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                // 5. Green squares are blocks that can't be included in flower beds.
                if (game[p] != FlowerBedsObject.Hedge) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (i in 0 until 4) {
                if (this[p + FlowerBedsGame.offset2[i], FlowerBedsGame.dirs[i]] == GridLineObject.Line) continue
                val node2 = pos2node[p + FlowerBedsGame.offset[i]]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.flowers.contains(it) }
            // 2. Your task as a gardener is to divide the garden in rectangular (or square)
            //    flower beds.
            // 3. Each flower bed should contain exactly one flower.
            if (rng.size != 1) {
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val p2 = rng[0]
            val n1 = area.size
            var r2 = 0
            var r1 = rows
            var c2 = 0
            var c1 = cols
            for (p in area) {
                if (r2 < p.row) r2 = p.row
                if (r1 > p.row) r1 = p.row
                if (c2 < p.col) c2 = p.col
                if (c1 > p.col) c1 = p.col
            }
            val rs = r2 - r1 + 1
            val cs = c2 - c1 + 1
            val s = if (rs * cs == n1) HintState.Complete else HintState.Error
            pos2state[p2] = s
            if (s == HintState.Complete) {
                val n = rects.size
                rects.add(FlowerBedsRect(area, rs, cs))
                for (p in area) { pos2rect[p] = n }
            } else
                isSolved = false
        }
        if (!isSolved) return
        // 4. Contiguous flower beds can't have the same area extension.
        if (!((0 until rects.size).all { n ->
            val rect = rects[n]
            rect.area.all { p ->
                FlowerBedsGame.offset.all {
                    val n2 = pos2rect[p + it]
                    if (n2 == null || n2 == n)
                        true
                    else {
                        val rect2 = rects[n2]
                        !(rect.rows == rect2.rows && rect.cols == rect2.cols)
                    }
                }
            }
        })) isSolved = false
    }
}
