package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.data.List
import java.util.*

class NurikabeGameState(game: NurikabeGame) : CellsGameState<NurikabeGame, NurikabeGameMove, NurikabeGameState>(game) {
    var objArray = Array<NurikabeObject>(rows() * cols()) { NurikabeEmptyObject() }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: NurikabeObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: NurikabeObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = NurikabeHintObject()
    }

    fun setObject(move: NurikabeGameMove): Boolean {
        val p = move.p
        val objOld = this[p]
        val objNew = move.obj
        if (objOld is NurikabeHintObject || objOld.toString() == objNew.toString()) return false
        this[p] = objNew
        updateIsSolved()
        return true
    }

    fun switchObject(move: NurikabeGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is NurikabeEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) NurikabeMarkerObject() else NurikabeWallObject()
            is NurikabeWallObject -> if (markerOption == MarkerOptions.MarkerLast) NurikabeMarkerObject() else NurikabeEmptyObject()
            is NurikabeMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) NurikabeWallObject() else NurikabeEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Nurikabe

        Summary
        Draw a continuous wall that divides gardens as big as the numbers

        Description
        1. Each number on the grid indicates a garden, occupying as many tiles
           as the number itself.
        2. Gardens can have any form, extending horizontally and vertically, but
           can't extend diagonally.
        3. The garden is separated by a single continuous wall. This means all
           wall tiles on the board must be connected horizontally or vertically.
           There can't be isolated walls.
        4. You must find and mark the wall following these rules:
        5. All the gardens in the puzzle are numbered at the start, there are no
           hidden gardens.
        6. A wall can't go over numbered squares.
        7. The wall can't form 2*2 squares.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 7. The wall can't form 2*2 squares.
        for (r in 0 until rows() - 1)
            rule2x2@ for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                for (os in NurikabeGame.offset2)
                    if (this[p.add(os)] !is NurikabeWallObject)
                        continue@rule2x2
                isSolved = false
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        var rngEmpty = mutableListOf<Position>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (this[p] is NurikabeWallObject)
                    rngWalls.add(p)
                else
                    rngEmpty.add(p)
            }
        for (p in rngWalls)
            for (os in NurikabeGame.offset) {
                val p2 = p.add(os)
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p], pos2node[p2])
            }
        for (p in rngEmpty)
            for (os in NurikabeGame.offset) {
                val p2 = p.add(os)
                if (rngEmpty.contains(p2))
                    g.connectNode(pos2node[p], pos2node[p2])
            }
        if (rngWalls.isEmpty())
            isSolved = false
        else {
            // 3. The garden is separated by a single continuous wall. This means all
            // wall tiles on the board must be connected horizontally or vertically.
            // There can't be isolated walls.
            g.setRootNode(pos2node[rngWalls[0]])
            val nodeList = g.bfs()
            if (rngWalls.size != nodeList.size) isSolved = false
        }
        while (rngEmpty.isNotEmpty()) {
            val node = pos2node[rngEmpty[0]]
            g.setRootNode(node)
            val nodeList = g.bfs()
            rngEmpty.removeAll { nodeList.contains(pos2node[it]) }
            val n2 = nodeList.size
            val rng = mutableListOf<Position>()
            for (p in game.pos2hint.keys)
                if (nodeList.contains(pos2node[p]))
                    rng.add(p.plus())
            when (rng.size) {
                0 ->                 // 5. All the gardens in the puzzle are numbered at the start, there are no
                    // hidden gardens.
                    isSolved = false
                1 -> {

                    // 1. Each number on the grid indicates a garden, occupying as many tiles
                    // as the number itself.
                    val p = rng[0]
                    val n1 = game.pos2hint[p]!!
                    val s = if (n1 == n2) HintState.Complete else HintState.Error
                    (this.get(p) as NurikabeHintObject).state = s
                    if (s != HintState.Complete) isSolved = false
                }
                else -> {
                    for (p in rng)
                        (this.get(p) as NurikabeHintObject).state = HintState.Normal
                    isSolved = false
                }
            }
        }
    }
}