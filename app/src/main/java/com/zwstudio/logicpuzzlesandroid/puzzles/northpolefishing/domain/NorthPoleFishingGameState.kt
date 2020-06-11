package com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import java.util.*

class NorthPoleFishingGameState(game: NorthPoleFishingGame) : CellsGameState<NorthPoleFishingGame, NorthPoleFishingGameMove, NorthPoleFishingGameState>(game) {
    var objArray = Cloner().deepClone(game.dots.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int, dir: Int) = objArray[row * cols() + col][dir]
    operator fun get(p: Position, dir: Int) = this[p.row, p.col, dir]
    operator fun set(row: Int, col: Int, dir: Int, obj: GridLineObject) {objArray[row * cols() + col][dir] = obj}
    operator fun set(p: Position, dir: Int, obj: GridLineObject) {this[p.row, p.col, dir] = obj}

    fun setObject(move: NorthPoleFishingGameMove): Boolean {
        val p1 = move.p
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        if (game.dots[p1, dir] != GridLineObject.Empty) return false
        val o = this[p1, dir]
        if (o == move.obj) return false
        val p2 = p1.add(NorthPoleFishingGame.offset[dir])
        this[p1, dir] = move.obj
        this[p2, dir2] = this[p1, dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: NorthPoleFishingGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p, move.dir]
        move.obj = when (o) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 14/North Pole Fishing

        Summary
        Fishing Neighbours

        Description
        1. The board represents a piece of antarctic, which some fisherman want
           to split in equal parts.
        2. They decide each one should have a piece of land of exactly 4 squares,
           including one fishing hole.
        3. Divide the land accordingly.
        4. Ice blocks are just obstacles and don't count as piece of land.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = HashMap<Position, Node>()
        for (r in 0 until rows() - 1)
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                if (game[p] != NorthPoleFishingObject.Block) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (i in 0 until 4) {
                if (this[p.add(NorthPoleFishingGame.offset2[i]), NorthPoleFishingGame.dirs[i]] == GridLineObject.Line) continue
                val node2 = pos2node[p.add(NorthPoleFishingGame.offset[i])]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.holes.contains(it) }
            // 2. They decide each one should have a piece of land of exactly 4 squares,
            // including one fishing hole.
            if (rng.size != 1) {
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val p2 = rng[0]
            val n1 = area.size
            val n2 = 4
            val s = if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p2] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}
