package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*

class GalaxiesGameState(game: GalaxiesGame) : CellsGameState<GalaxiesGame, GalaxiesGameMove, GalaxiesGameState>(game) {
    var objArray: Array<Array<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<GridLineObject>) {this[p.row, p.col] = obj}

    init {
        for (p in game.galaxies)
            pos2state[p] = HintState.Normal
        updateIsSolved()
    }

    override fun setObject(move: GalaxiesGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + GalaxiesGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty) return false
        val o = this[p1][dir]
        if (o == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    override fun switchObject(move: GalaxiesGameMove): Boolean {
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
        iOS Game: Logic Games/Puzzle Set 4/Galaxies

        Summary
        Fill the Symmetric Spiral Galaxies

        Description
        1. In the board there are marked centers of a few 'Spiral' Galaxies.
        2. These Galaxies are symmetrical to a rotation of 180 degrees. This
           means that rotating the shape of the Galaxy by 180 degrees (half a
           full turn) around the center, will result in an identical shape.
        3. In the end, all the space must be included in Galaxies and Galaxies
           can't overlap.
        4. There can be single tile Galaxies (with the center inside it) and
           some Galaxy center will be cross two or four tiles.
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
                    if (this[p + GalaxiesGame.offset2[i]][GalaxiesGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + GalaxiesGame.offset[i]]!!)
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
        var n1 = 0
        for (area in areas) {
            val rng = game.galaxies.filter { area.contains(Position(it.row / 2, it.col / 2)) }
            if (rng.size != 1) {
                // 3. Galaxies can't overlap.
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
            } else {
                // 2. These Galaxies are symmetrical to a rotation of 180 degrees. This
                // means that rotating the shape of the Galaxy by 180 degrees (half a
                // full turn) around the center, will result in an identical shape.
                val galaxy = rng[0]
                val b = area.all { area.contains(Position(galaxy.row - it.row - 1, galaxy.col - it.col - 1)) }
                val s = if (b) HintState.Complete else HintState.Error
                pos2state[galaxy] = s
                if (!b) isSolved = false
            }
            n1 += area.size
        }
        // 3. In the end, all the space must be included in Galaxies
        if (n1 != rows * cols) isSolved = false
    }
}