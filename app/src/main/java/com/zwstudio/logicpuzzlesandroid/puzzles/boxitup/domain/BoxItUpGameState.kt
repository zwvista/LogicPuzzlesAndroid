package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class BoxItUpGameState(game: BoxItUpGame) : CellsGameState<BoxItUpGame, BoxItUpGameMove, BoxItUpGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    fun setObject(move: BoxItUpGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1.add(BoxItUpGame.offset[dir])
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return false
        val o = this[p1][dir]
        if (o == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: BoxItUpGameMove): Boolean {
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
        iOS Game: Logic Games/Puzzle Set 5/Box It Up

        Summary
        Numbered Areas Interval

        Description
        1. A simple puzzle where you have to divide the Board in Boxes (Rectangles).
        2. Each Box must contain one number and the number represents the area of
           that Box.
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
                    if (this[p.add(BoxItUpGame.offset2[i])][BoxItUpGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p.add(BoxItUpGame.offset[i])]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.pos2hint.containsKey(it) }
            // 2. Each Box must contain one number.
            if (rng.size != 1) {
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val p2 = rng[0]
            val n1 = area.size
            val n2 = game.pos2hint[p2]
            var r2 = 0
            var r1 = rows()
            var c2 = 0
            var c1 = cols()
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
            // 1. A simple puzzle where you have to divide the Board in Boxes (Rectangles).
            // 2. The number represents the area of that Box.
            val s = if (rs * cs == n1 && rs * cs == n2 && !hasLine()) HintState.Complete else HintState.Error
            pos2state[p2] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}