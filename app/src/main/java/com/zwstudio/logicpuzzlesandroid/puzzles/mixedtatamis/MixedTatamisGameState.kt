package com.zwstudio.logicpuzzlesandroid.puzzles.mixedtatamis

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MixedTatamisGameState(game: MixedTatamisGame) : CellsGameState<MixedTatamisGame, MixedTatamisGameMove, MixedTatamisGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: MixedTatamisGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + MixedTatamisGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: MixedTatamisGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 2/Puzzle Set 4/Mixed Tatamis

        Summary
        Just that long

        Description
        1. Divide the board into areas (Tatami).
        2. Every Tatami must be exactly one cell wide and can be of length
           from 1 to 4 cells.
        3. A cell with a number indicates the length of the Tatami. Not all
           Tatamis have to be marked by a number.
        4. Two Tatamis of the same size must not be orthogonally adjacent.
        5. A grid dot must not be shared by the corners of four Tatamis
           (lines can't form 4-way crosses).
    */
    private fun updateIsSolved() {
        isSolved = true
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
                    if (this[p + MixedTatamisGame.offset2[i]][MixedTatamisGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + MixedTatamisGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.pos2hint.containsKey(it) }
            // 2. Each Box must contain one number.
            if (rng.size > 1) {
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val n1 = area.size
            var r2 = 0
            var r1 = rows
            var c2 = 0
            var c1 = cols
            for (p in area) {
                if (r2 < p.row) r2 = p.row
                if (r1 > p.row) r1 = p.row
                if (c2 < p.col) c2 = p.col
                if (c1 > p.col) c1 = p.col
            }
            val rs = r2 - r1 + 1
            val cs = c2 - c1 + 1
            val (w, h) = if (rs < cs) rs to cs else cs to rs
            var s = if (w == 1 && h <= 4 && h == n1) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            // 3. Not all Tatamis have to be marked by a number.
            if (rng.isEmpty()) continue
            val p2 = rng[0]
            val n2 = game.pos2hint[p2]
            // 1. Just like Box It Up, you have to divide the Board in Boxes (Rectangles).
            // 3. A cell with a number indicates the length of the Tatami.
            s = if (s == HintState.Complete && n1 == n2) HintState.Complete else HintState.Error
            pos2state[p2] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}