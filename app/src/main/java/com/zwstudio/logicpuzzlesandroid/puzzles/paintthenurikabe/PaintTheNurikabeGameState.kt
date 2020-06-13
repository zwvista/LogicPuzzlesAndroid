package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.NurikabeGame

class PaintTheNurikabeGameState(game: PaintTheNurikabeGame) : CellsGameState<PaintTheNurikabeGame, PaintTheNurikabeGameMove, PaintTheNurikabeGameState>(game) {
    var objArray = Array(rows * cols) { PaintTheNurikabeObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: PaintTheNurikabeObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: PaintTheNurikabeObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            pos2state[p] = HintState.Normal
        updateIsSolved()
    }

    fun setObject(move: PaintTheNurikabeGameMove): Boolean {
        val p = move.p
        val o = move.obj
        if (!isValid(p) || this[p] == o) return false
        this[p] = o
        for (p2 in game.areas[game.pos2area[p]!!])
            this[p2] = o
        updateIsSolved()
        return true
    }

    fun switchObject(move: PaintTheNurikabeGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return false
        val o = this[p]
        move.obj = when (o) {
            PaintTheNurikabeObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) PaintTheNurikabeObject.Marker else PaintTheNurikabeObject.Painted
            PaintTheNurikabeObject.Painted -> if (markerOption == MarkerOptions.MarkerLast) PaintTheNurikabeObject.Marker else PaintTheNurikabeObject.Empty
            PaintTheNurikabeObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) PaintTheNurikabeObject.Painted else PaintTheNurikabeObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Paint The Nurikabe

        Summary
        Paint areas, find Nurikabes

        Description
        1. By painting (filling) the areas you have to complete a Nurikabe.
           Specifically:
        2. A number indicates how many painted tiles are adjacent to it.
        3. The painted tiles form an orthogonally continuous area, like a
           Nurikabe.
        4. There can't be any 2*2 area of the same color(painted or empty).
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] == PaintTheNurikabeObject.Forbidden)
                    this[r, c] = PaintTheNurikabeObject.Empty
        // 2. A number indicates how many painted tiles are adjacent to it.
        for ((p, n2) in game.pos2hint) {
            val rng = mutableListOf<Position>()
            var n1 = 0
            for (os in PaintTheNurikabeGame.offset) {
                val p2 = p + os
                if (!isValid(p2)) continue
                val o = this[p2]
                if (o == PaintTheNurikabeObject.Painted)
                    n1++
                else if (o == PaintTheNurikabeObject.Empty)
                    rng.add(p2)
            }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete)
                isSolved = false
            else if (allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = PaintTheNurikabeObject.Forbidden
        }
        // 4. There can't be any 2*2 area of the same color(painted or empty).
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                if (NurikabeGame.offset2.all { this[p + it] == PaintTheNurikabeObject.Painted } ||
                    NurikabeGame.offset2.all { this[p + it] == PaintTheNurikabeObject.Empty }) {
                    isSolved = false
                    return
                }
            }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == PaintTheNurikabeObject.Painted) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for (p in pos2node.keys)
            for (os in NurikabeGame.offset) {
                val p2 = p + os
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        // 3. The painted tiles form an orthogonally continuous area, like a
        // Nurikabe.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}