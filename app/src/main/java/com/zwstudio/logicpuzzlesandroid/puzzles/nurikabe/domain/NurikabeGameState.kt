package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import fj.data.List
import java.util.*

class NurikabeGameState(game: NurikabeGame) : CellsGameState<NurikabeGame?, NurikabeGameMove?, NurikabeGameState?>(game) {
    var objArray: Array<NurikabeObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position?) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: NurikabeObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position?, obj: NurikabeObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: NurikabeGameMove): Boolean {
        val p: Position = move.p
        val objOld: NurikabeObject? = get(p)
        val objNew: NurikabeObject = move.obj
        if (objOld is NurikabeHintObject || objOld.toString() == objNew.toString()) return false
        set(p, objNew)
        updateIsSolved()
        return true
    }

    fun switchObject(move: NurikabeGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<NurikabeObject, NurikabeObject> = label@ F<NurikabeObject, NurikabeObject> { obj: NurikabeObject? ->
            if (obj is NurikabeEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) NurikabeMarkerObject() else NurikabeWallObject()
            if (obj is NurikabeWallObject) return@label if (markerOption == MarkerOptions.MarkerLast) NurikabeMarkerObject() else NurikabeEmptyObject()
            if (obj is NurikabeMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) NurikabeWallObject() else NurikabeEmptyObject()
            obj
        }
        move.obj = f.f(get(move.p))
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
        for (r in 0 until rows() - 1) rule2x2@ for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            for (os in NurikabeGame.Companion.offset2) if (get(p.add(os)) !is NurikabeWallObject) continue@rule2x2
            isSolved = false
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        var rngEmpty = mutableListOf<Position>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
            if (get(p) is NurikabeWallObject) rngWalls.add(p) else rngEmpty.add(p)
        }
        for (p in rngWalls) for (os in NurikabeGame.Companion.offset) {
            val p2 = p.add(os)
            if (rngWalls.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        for (p in rngEmpty) for (os in NurikabeGame.Companion.offset) {
            val p2 = p.add(os)
            if (rngEmpty.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        if (rngWalls.isEmpty()) isSolved = false else {
            // 3. The garden is separated by a single continuous wall. This means all
            // wall tiles on the board must be connected horizontally or vertically.
            // There can't be isolated walls.
            g.setRootNode(pos2node[rngWalls[0]])
            val nodeList = g.bfs()
            if (rngWalls.size != nodeList.size) isSolved = false
        }
        while (!rngEmpty.isEmpty()) {
            val node = pos2node[rngEmpty[0]]
            g.setRootNode(node)
            val nodeList = g.bfs()
            rngEmpty = List.iterableList(rngEmpty).removeAll(F<Position, Boolean> { p: Position -> nodeList.contains(pos2node[p]) }).toJavaList()
            val n2 = nodeList.size
            val rng = mutableListOf<Position>()
            for (p in game.pos2hint.keys) if (nodeList.contains(pos2node[p])) rng.add(p.plus())
            when (rng.size) {
                0 ->                 // 5. All the gardens in the puzzle are numbered at the start, there are no
                    // hidden gardens.
                    isSolved = false
                1 -> {

                    // 1. Each number on the grid indicates a garden, occupying as many tiles
                    // as the number itself.
                    val p = rng[0]
                    val n1: Int = game.pos2hint.get(p)
                    val s: HintState = if (n1 == n2) HintState.Complete else HintState.Error
                    (get(p) as NurikabeHintObject?)!!.state = s
                    if (s != HintState.Complete) isSolved = false
                }
                else -> {
                    for (p in rng) (get(p) as NurikabeHintObject?)!!.state = HintState.Normal
                    isSolved = false
                }
            }
        }
    }

    init {
        objArray = arrayOfNulls<NurikabeObject>(rows() * cols())
        Arrays.fill(objArray, NurikabeEmptyObject())
        for (p in game.pos2hint.keys) set(p, NurikabeHintObject())
    }
}