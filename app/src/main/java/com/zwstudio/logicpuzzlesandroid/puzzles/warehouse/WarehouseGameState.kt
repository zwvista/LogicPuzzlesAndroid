package com.zwstudio.logicpuzzlesandroid.puzzles.warehouse

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WarehouseGameState(game: WarehouseGame) : CellsGameState<WarehouseGame, WarehouseGameMove, WarehouseGameState>(game) {
    val objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    val pos2state = mutableMapOf<Position, AllowedObjectState>()
    val dot2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: WarehouseGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + WarehouseGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: WarehouseGameMove): GameOperationType {
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
                    if (this[p + WarehouseGame.offset2[i]][WarehouseGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + WarehouseGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.pos2symbol.containsKey(it) }
            // 2. Each Box must contain one number.
            if (rng.size != 1) {
                for (p in rng)
                    pos2state[p] = AllowedObjectState.Error
                isSolved = false
                continue
            }
            val p2 = rng[0]
            val n1 = area.size
            val n2 = game.pos2symbol[p2]
            var (r1, r2) = rows to 0
            var (c1, c2) = cols to 0
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
            // 3. a cross means it's a square box.
            // 4. a horizontal bar means the box is wider than taller.
            // 5. a vertical bar means the box is taller than wider.
            val ch = game.pos2symbol[p2]!!
            val s = if ((if (ch == WarehouseGame.PUZ_HORZ) rs < cs
                else if (ch == WarehouseGame.PUZ_VERT) rs > cs
                else rs == cs) && !hasLine()) AllowedObjectState.Normal else AllowedObjectState.Error
            pos2state[p2] = s
            if (s != AllowedObjectState.Normal) isSolved = false
        }
        // 6. A grid dot must not be shared by the corners of four boxes.
        dot2state.clear()
        for (r in 1..<rows - 1)
            for (c in 1..<cols - 1) {
                val p = Position(r, c)
                val has4 = (0..<4).all { this[p][it] == GridLineObject.Line }
                dot2state[p] = if(has4) AllowedObjectState.Error else AllowedObjectState.Normal
                if (has4) isSolved = false
            }
    }
}