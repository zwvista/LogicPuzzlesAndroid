package com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class OverUnderGameState(game: OverUnderGame) : CellsGameState<OverUnderGame, OverUnderGameMove, OverUnderGameState>(game) {
    var objArray = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject>) {objArray[row * cols() + col] = dotObj}
    operator fun set(p: Position, obj: Array<GridLineObject>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: OverUnderGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1.add(OverUnderGame.offset[dir])
        if (game[p1][dir] != GridLineObject.Empty) return false
        val o = this[p1][dir]
        if (o == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: OverUnderGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p][move.dir]
        move.obj =  when (o) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 15/Over Under

        Summary
        Over and Under regions

        Description
        1. Divide the board in regions following these rules:
        2. Each region must contain two numbers.
        3. The region size must be between the two numbers.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows() - 1)
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0 until rows() - 1)
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                for (i in 0..3)
                    if (this[p.add(OverUnderGame.offset2[i])][OverUnderGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p], pos2node[p.add(OverUnderGame.offset[i])])
            }
        val areas = mutableListOf<List<Position>>()
        while (pos2node.isNotEmpty()) {
            g.setRootNode(pos2node.values.first())
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            areas.add(area)
            for (p in area)
                pos2node.remove(p)
        }
        for (area in areas) {
            val rng = area.filter { game.pos2hint.containsKey(it) }
            // 2. Each region must contain two numbers.
            if (rng.size != 2) {
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val p2 = rng[0]
            val p3 = rng[1]
            val n1 = area.size
            var n2 = game.pos2hint[p2]!!
            var n3 = game.pos2hint[p3]!!
            if (n2 > n3) {
                val temp = n2
                n2 = n3
                n3 = temp
            }
            // 3. The region size must be between the two numbers.
            val s = if (n1 > n2 && n1 < n3) HintState.Complete else HintState.Error
            pos2state[p2] = s
            pos2state[p3] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}