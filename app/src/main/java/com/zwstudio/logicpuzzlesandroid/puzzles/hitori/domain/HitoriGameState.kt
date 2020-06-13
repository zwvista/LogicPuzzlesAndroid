package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import java.util.*

class HitoriGameState(game: HitoriGame) : CellsGameState<HitoriGame, HitoriGameMove, HitoriGameState>(game) {
    private var objArray = Array(rows * cols) { HitoriObject.Normal }
    var row2hint = Array(rows) { "" }
    var col2hint = Array(cols) { "" }

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: HitoriObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: HitoriObject) {this[p.row, p.col] = obj}

    fun setObject(move: HitoriGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: HitoriGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        fun f(obj: HitoriObject) =
            when (obj) {
                HitoriObject.Normal ->
                    if (markerOption == MarkerOptions.MarkerFirst) HitoriObject.Marker
                    else HitoriObject.Darken
                HitoriObject.Darken ->
                    if (markerOption == MarkerOptions.MarkerLast) HitoriObject.Marker
                    else HitoriObject.Normal
                HitoriObject.Marker ->
                    if (markerOption == MarkerOptions.MarkerFirst) HitoriObject.Darken
                    else HitoriObject.Normal
            }
        val p = move.p
        if (!isValid(p)) return false
        move.obj = f(this[p])
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Hitori

        Summary
        Darken some tiles so no number appears in a row or column more than once

        Description
        1. The goal is to shade squares so that a number appears only once in a
           row or column.
        2. While doing that, you must take care that shaded squares don't touch
           horizontally or vertically between them.
        3. In the end all the un-shaded squares must form a single continuous area.
    */
    private fun updateIsSolved() {
        isSolved = true
        var chars: String
        // 1. The goal is to shade squares so that a number appears only once in a
        // row.
        for (r in 0 until rows) {
            row2hint[r] = ""
            chars = row2hint[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == HitoriObject.Darken) continue
                val ch = game[r, c]
                if (chars.contains(ch)) {
                    isSolved = false
                    row2hint[r] += ch.toString()
                } else
                    chars += ch
            }
        }
        // 1. The goal is to shade squares so that a number appears only once in a
        // column.
        for (c in 0 until cols) {
            col2hint[c] = ""
            chars = col2hint[c]
            for (r in 0 until rows) {
                val p = Position(r, c)
                if (this[p] == HitoriObject.Darken) continue
                val ch = game[r, c]
                if (chars.contains(ch)) {
                    isSolved = false
                    col2hint[c] += ch.toString()
                } else
                    chars += ch
            }
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = HashMap<Position, Node>()
        val rngDarken = mutableListOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == HitoriObject.Darken)
                    rngDarken.add(p)
                else {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        // 2. While doing that, you must take care that shaded squares don't touch
        // horizontally or vertically between them.
        for (p in rngDarken)
            for (os in HitoriGame.offset) {
                val p2 = p + os
                if (rngDarken.contains(p2)) {
                    isSolved = false
                    return
                }
            }
        for (p in pos2node.keys) {
            for (os in HitoriGame.offset) {
                val p2 = p + os
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        }
        // 3. In the end all the un-shaded squares must form a single continuous area.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}
