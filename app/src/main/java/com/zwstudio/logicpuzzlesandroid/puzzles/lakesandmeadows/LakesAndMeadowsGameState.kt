package com.zwstudio.logicpuzzlesandroid.puzzles.lakesandmeadows

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LakesAndMeadowsGameState(game: LakesAndMeadowsGame) : CellsGameState<LakesAndMeadowsGame, LakesAndMeadowsGameMove, LakesAndMeadowsGameState>(game) {
    val objArray = Cloner().deepClone(game.dots.objArray)
    val pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int, dir: Int) = objArray[row * cols + col][dir]
    operator fun get(p: Position, dir: Int) = this[p.row, p.col, dir]
    operator fun set(row: Int, col: Int, dir: Int, obj: GridLineObject) {objArray[row * cols + col][dir] = obj}
    operator fun set(p: Position, dir: Int, obj: GridLineObject) {this[p.row, p.col, dir] = obj}

    override fun setObject(move: LakesAndMeadowsGameMove): GameOperationType {
        val p1 = move.p
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        if (game.dots[p1, dir] != GridLineObject.Empty) return GameOperationType.Invalid
        val o = this[p1, dir]
        if (o == move.obj) return GameOperationType.Invalid
        val p2 = p1 + LakesAndMeadowsGame.offset[dir]
        this[p1, dir] = move.obj
        this[p2, dir2] = this[p1, dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: LakesAndMeadowsGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 2/Puzzle Set 5/Lakes and Meadows

        Summary
        Lakes and Meadows

        Description
        1. Some of the cells have lakes in them.
        2. The aim is to divide the grid into square blocks such that each
           block contains exactly one lake.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node)
            for (i in 0..<4) {
                if (this[p + LakesAndMeadowsGame.offset2[i], LakesAndMeadowsGame.dirs[i]] == GridLineObject.Line) continue
                val p2 = p + LakesAndMeadowsGame.offset[i]
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.lakes.contains(it) }
            // 1. Some of the cells have lakes in them.
            // 2. The aim is to divide the grid into square blocks such that each
            //    block contains exactly one lake.
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
            fun hasLine(): Boolean {
                for (r in r1..r2)
                    for (c in c1..c2)
                        if (r < r2 && this[r + 1, c + 1, 3] == GridLineObject.Line || c < c2 && this[r + 1, c + 1, 0] == GridLineObject.Line)
                            return true
                return false
            }
            val s = if (rs * cs == n1 && !hasLine()) HintState.Complete else HintState.Error
            pos2state[p2] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}
