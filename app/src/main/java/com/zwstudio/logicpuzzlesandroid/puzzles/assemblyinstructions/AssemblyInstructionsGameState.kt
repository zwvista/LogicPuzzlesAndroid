package com.zwstudio.logicpuzzlesandroid.puzzles.assemblyinstructions

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class AssemblyInstructionsGameState(game: AssemblyInstructionsGame) : CellsGameState<AssemblyInstructionsGame, AssemblyInstructionsGameMove, AssemblyInstructionsGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: AssemblyInstructionsGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + AssemblyInstructionsGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: AssemblyInstructionsGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[move.p][move.dir]) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 4/Assembly Instructions

        Summary
        New screw legs 'A' to seat 'C' using bolts 'J'...

        Description
        1. Divide the board so that every letter corresponds to a 'part' which
           has the same shape and orientation everywhere it is found.
        2. So for example if letter 'A' is a 2x3 rectangle, every 'A' on the board
           will correspond to a 2x3 rectangle and 'A' will appear in the same position
           in the rectangle itself.
        3. If letter 'B' has an L shape with the letter on the top left, every 'B'
           will have an L shape with the letter on the top left, etc.
    */
    private fun updateIsSolved() {
        isSolved = true
        val ch2areas = mutableMapOf<Char, MutableList<List<Position>>>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                for (i in 0..<4)
                    if (this[p + AssemblyInstructionsGame.offset2[i]][AssemblyInstructionsGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + AssemblyInstructionsGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.pos2hint.containsKey(it) }
            // 1. Divide the board so that every letter corresponds to a 'part'
            if (rng.size != 1) {
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val ch = game.pos2hint[rng[0]]!!
            ch2areas.getOrPut(ch) { mutableListOf() }.add(area)
        }
        for ((ch, areas) in ch2areas) {
            if (areas.size != game.ch2rng[ch]!!.size) {
                isSolved = false
                for (area in areas) {
                    val pHint = area.first { game.pos2hint[it] != null }
                    pos2state[pHint] = HintState.Normal
                }
                continue
            }
            // 1. every letter corresponds to a 'part' which
            // has the same shape and orientation everywhere it is found.
            val cnt = (areas.map { area ->
                var r1 = rows
                var c1 = cols
                for (p in area) {
                    if (r1 > p.row) r1 = p.row
                    if (c1 > p.col) c1 = p.col
                }
                val p1 = Position(r1, c1)
                val pHint = area.first { game.pos2hint[it] != null }
                AssemblyInstructionsPart(area.map { it - p1 }.sorted(), pHint - p1)
            }).toSet().size
            val s = if (cnt == 1) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            for (area in areas) {
                val pHint = area.first { game.pos2hint[it] != null }
                pos2state[pHint] = s
            }
        }
    }
}