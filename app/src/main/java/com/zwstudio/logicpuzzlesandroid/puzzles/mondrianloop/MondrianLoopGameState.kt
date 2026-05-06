package com.zwstudio.logicpuzzlesandroid.puzzles.mondrianloop

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MondrianLoopGameState(game: MondrianLoopGame) : CellsGameState<MondrianLoopGame, MondrianLoopGameMove, MondrianLoopGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var rectangles = mutableListOf<List<Position>>()
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: MondrianLoopGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + MondrianLoopGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: MondrianLoopGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 4/Puzzle Set 2/Mondrian Loop

        Summary
        Lots of artists around here

        Description
        1. Enough with impressionists, time for a nice geometric painting
           called Squarism!
        2. Divide the board in many rectangles or squares. Each
           rectangle/square can contain only one number, which represents
           its area, but it can also contain none.
        3. The rectangles/squares can't touch each other with their sides
           (they can't share a side), but they have to form a loop by
           connecting with their corners.
        4. In the end there must be a single loop that connects all
           rectangles/squares by corners.
    */
    private fun updateIsSolved() {
        isSolved = true
        pos2stateHint.clear()
        pos2stateAllowed.clear()
        rectangles.clear()
        var emptyRects = mutableListOf<List<Position>>()
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
                    if (this[p + MondrianLoopGame.offset2[i]][MondrianLoopGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + MondrianLoopGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
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
            fun hasLine(): Boolean {
                for (r in r1..r2)
                    for (c in c1..c2) {
                        val dotObj = this[r + 1, c + 1]
                        if (r < r2 && dotObj[3] == GridLineObject.Line || c < c2 && dotObj[0] == GridLineObject.Line)
                            return true
                    }
                return false
            }
            val isRect = rs * cs == area.size && !hasLine()
            if (!isRect) { isSolved = false; continue }
            // 2. Each rectangle/square can contain only one number, which represents
            //    its area, but it can also contain none.
            val rng = area.filter { game.pos2hint.containsKey(it) }
            if (rng.size > 1) { isSolved = false; continue }
            if (rng.isEmpty())
                emptyRects.add(area)
            else {
                rectangles.add(area)
                val pHint = rng[0]
                val n1 = area.size
                val n2 = game.pos2hint[pHint]!!
                val s = if (n2 == MondrianLoopGame.PUZ_UNKNOWN || n1 == n2) HintState.Complete else HintState.Error
                pos2stateHint[pHint] = s
                if (s != HintState.Complete) isSolved = false
            }
        }
        if (!isSolved) return
        // 3. The rectangles/squares can't touch each other with their sides
        //    (they can't share a side)
        emptyRects = emptyRects.filter { rect ->
            rect.all { p ->
                MondrianLoopGame.offset.all {
                    val p2 = p + it
                    rect.contains(p2) || !rectangles.any { it.contains(p2) }
                }
            }
        }.toMutableList()
        rectangles.addAll(emptyRects)
        if (!rectangles.all { rect ->
            rect.all { p ->
                MondrianLoopGame.offset.all {
                    val p2 = p + it
                    rect.contains(p2) || !rectangles.any { it.contains(p2) }
                }
            }
        }) { isSolved = false; return }
        // 4. In the end there must be a single loop that connects all
        //    rectangles/squares by corners.
        val id2ids = mutableMapOf<Int, MutableList<Int>>()
        for ((i, rect) in rectangles.withIndex()) {
            id2ids[i] = rect.flatMap { p ->
                MondrianLoopGame.offset3
                    .map { p + it }
                    .filter { isValid(it) }
                    .mapNotNull { p2 ->
                        rectangles.indices.firstOrNull { j -> j != i && rectangles[j].contains(p2) }
                    }
            }.toSet().toMutableList()
        }
        if (!id2ids.all { (id, ids) ->
            ids.size == 2
        }) { isSolved = false; return }
        // Check the loop
        val id = id2ids.keys.first()
        var id2 = id
        var n = -1
        while (true) {
            val ids = id2ids[id2]
            if (ids == null) { isSolved = false; return }
            id2ids.remove(id2)
            for (id3 in ids)
                if (id3 != n) {
                    n = id2
                    id2 = id3
                    break
                }
            if (id2 == id) break
        }
    }
}