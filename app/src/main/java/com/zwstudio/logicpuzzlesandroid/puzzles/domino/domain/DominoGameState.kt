package com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import java.util.*

class DominoGameState(game: DominoGame) : CellsGameState<DominoGame, DominoGameMove, DominoGameState>(game) {
    var objArray = Cloner().deepClone(game.objArray)
    var pos2state: Map<Position?, HintState?> = HashMap()
    
    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    fun setObject(move: DominoGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + DominoGame.offset[dir]
        val o = this[p1][dir]
        if (o == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: DominoGameMove): Boolean {
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
        iOS Game: Logic Games/Puzzle Set 5/Domino

        Summary
        Find all the Domino tiles

        Description
        1. On the board there is a complete Domino set. The Domino tiles borders
           however aren't marked, it's up to you to identify them.
        2. In early levels the board contains a smaller Domino set, of numbers
           ranging from 0 to 3.
        3. This means you will be looking for a Domino set composed of these
           combinations.
           0-0, 0-1, 0-2, 0-3
           1-1, 1-2, 1-3
           2-2, 2-3
           3-3
        4. In harder levels, the Domino set will also include fours, fives,
           sixes, etc.
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
                    if (this[p + DominoGame.offset2[i]][DominoGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + DominoGame.offset[i]]!!)
            }
        val dominoes = mutableListOf<List<Int?>>()
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { (_, node) -> nodeList.contains(node) }.keys.toList()
            if (area.size != 2) {
                isSolved = false
                return
            }
            val domino = area.map { game.pos2hint[it]!! }.sorted()
            // 2. In early levels the board contains a smaller Domino set, of numbers ranging from 0 to 3.
            // 3. This means you will be looking for a Domino set composed of these combinations.
            if (dominoes.contains(domino)) {
                isSolved = false
                return
            }
            dominoes.add(domino)
            for (p in area)
                pos2node.remove(p)
        }
    }
}