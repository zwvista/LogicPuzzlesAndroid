package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import fj.F
import fj.data.List
import java.util.*

class PairakabeGameState(game: PairakabeGame) : CellsGameState<PairakabeGame, PairakabeGameMove, PairakabeGameState>(game) {
    var objArray: Array<PairakabeObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: PairakabeObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: PairakabeObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: PairakabeGameMove): Boolean {
        val p: Position = move.p
        val objOld: PairakabeObject? = get(p)
        val objNew: PairakabeObject = move.obj
        if (objOld is PairakabeHintObject || objOld.toString() == objNew.toString()) return false
        set(p, objNew)
        updateIsSolved()
        return true
    }

    fun switchObject(move: PairakabeGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<PairakabeObject, PairakabeObject> = label@ F<PairakabeObject, PairakabeObject> { obj: PairakabeObject? ->
            if (obj is PairakabeEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) PairakabeMarkerObject() else PairakabeWallObject()
            if (obj is PairakabeWallObject) return@label if (markerOption == MarkerOptions.MarkerLast) PairakabeMarkerObject() else PairakabeEmptyObject()
            if (obj is PairakabeMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) PairakabeWallObject() else PairakabeEmptyObject()
            obj
        }
        move.obj = f.f(get(move.p))
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 12/Pairakabe

        Summary
        Just to confuse things a bit more

        Description
        1. Plays like Nurikabe, with an interesting twist.
        2. Instead of just one number, each 'garden' contains two numbers and
           the area of the garden is given by the sum of both.
    */
    private fun updateIsSolved() {
        isSolved = true
        // The wall can't form 2*2 squares.
        for (r in 0 until rows() - 1) rule2x2@ for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            for (os in PairakabeGame.Companion.offset2) if (get(p.add(os)) !is PairakabeWallObject) continue@rule2x2
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
            if (get(p) is PairakabeWallObject) rngWalls.add(p) else rngEmpty.add(p)
        }
        for (p in rngWalls) for (os in PairakabeGame.Companion.offset) {
            val p2 = p.add(os)
            if (rngWalls.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        for (p in rngEmpty) for (os in PairakabeGame.Companion.offset) {
            val p2 = p.add(os)
            if (rngEmpty.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        if (rngWalls.isEmpty()) isSolved = false else {
            // The garden is separated by a single continuous wall. This means all
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
                0 ->                 // All the gardens in the puzzle are numbered at the start, there are no
                    // hidden gardens.
                    isSolved = false
                1 -> (get(rng[0]) as PairakabeHintObject?)!!.state = HintState.Error
                2 -> {
                    // 2. Instead of just one number, each 'garden' contains two numbers and
                    // the area of the garden is given by the sum of both.
                    val p1 = rng[0]
                    val p2 = rng[1]
                    val n1: Int = game.pos2hint.get(p1) + game.pos2hint.get(p2)
                    val s: HintState = if (n1 == n2) HintState.Complete else HintState.Error
                    (get(p1) as PairakabeHintObject?)!!.state = s
                    (get(p2) as PairakabeHintObject?)!!.state = s
                    if (s != HintState.Complete) isSolved = false
                }
                else -> {
                    for (p in rng) (get(p) as PairakabeHintObject?)!!.state = HintState.Normal
                    isSolved = false
                }
            }
        }
    }

    init {
        objArray = arrayOfNulls<PairakabeObject>(rows() * cols())
        Arrays.fill(objArray, PairakabeEmptyObject())
        for (p in game.pos2hint.keys) set(p, PairakabeHintObject())
    }
}