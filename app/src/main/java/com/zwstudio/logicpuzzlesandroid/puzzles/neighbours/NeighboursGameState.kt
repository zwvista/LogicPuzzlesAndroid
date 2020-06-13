package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class NeighboursGameState(game: NeighboursGame) : CellsGameState<NeighboursGame, NeighboursGameMove, NeighboursGameState>(game) {
    var objArray = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<GridLineObject>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: NeighboursGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + NeighboursGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty) return false
        val o = this[p1][dir]
        if (o == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: NeighboursGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p][move.dir]
        move.obj = when (o) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 8/Neighbours

        Summary
        Neighbours, yes, but not equally sociable

        Description
        1. The board represents a piece of land bought by a bunch of people. They
           decided to split the land in equal parts.
        2. However some people are more social and some are less, so each owner
           wants an exact number of neighbours around him.
        3. Each number on the board represents an owner house and the number of
           neighbours he desires.
        4. Divide the land so that each one has an equal number of squares and
           the requested number of neighbours.
        5. Later on, there will be Question Marks, which represents an owner for
           which you don't know the neighbours preference.
    */
    private fun updateIsSolved() {
        isSolved = true
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
                    if (this[p + NeighboursGame.offset2[i]][NeighboursGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + NeighboursGame.offset[i]]!!)
            }
        val areas = mutableListOf<List<Position>>()
        val pos2area = mutableMapOf<Position, Int>()
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            areas.add(area)
            for (p in area) {
                pos2area[p] = areas.size
                pos2node.remove(p)
            }
        }
        val n2 = game.areaSize
        for (area in areas) {
            val rng = area.filter { game.pos2hint.containsKey(it) }
            if (rng.size != 1) {
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val p3 = rng[0]
            val n1 = area.size
            val n3 = game.pos2hint[p3]!!
            fun neighbours(): Int {
                val indexes = mutableSetOf<Int>()
                val idx = pos2area[area[0]]
                for (p in area) {
                    var i = 0
                    while (i < 4) {
                        if (this[p + NeighboursGame.offset2[i]][NeighboursGame.dirs[i]] != GridLineObject.Line) {
                            i++
                            continue
                        }
                        val p2 = p + NeighboursGame.offset[i]
                        val idx2 = pos2area[p2]
                        if (idx2 == null) {
                            i++
                            continue
                        }
                        if (idx == idx2) return -1
                        indexes.add(idx2)
                        i++
                    }
                }
                return indexes.size
            }
            // 3. Each number on the board represents an owner house and the number of
            // neighbours he desires.
            // 4. Divide the land so that each one has an equal number of squares and
            // the requested number of neighbours.
            val s = if (n1 == n2 && n3 == neighbours()) HintState.Complete else HintState.Error
            pos2state[p3] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}