package com.zwstudio.logicpuzzlesandroid.puzzles.blackandwhitechocolate

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BlackAndWhiteChocolateGameState(game: BlackAndWhiteChocolateGame) : CellsGameState<BlackAndWhiteChocolateGame, BlackAndWhiteChocolateGameMove, BlackAndWhiteChocolateGameState>(game) {
    val objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: BlackAndWhiteChocolateGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + BlackAndWhiteChocolateGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: BlackAndWhiteChocolateGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 4/Puzzle Set 2/Black and White Chocolate

        Summary
        Yummy !!

        Description
         1. Your chocolate factory made a mess. Instead of pouring dark and white chocolate
            in the neat usual rectangle shapes, everything got mixed up.
         2. Your brand policy is equality, so you have to sell chocolate bars with have
            equally dark and white chocolate in it.
         3. Divide the board in chocolate 'bars' that contain the same number of dark
            and white chocolate.
         4. Also the shape of the dark chocolate in an area must be the same as the white
            one, although it can be mirrored and/or rotated.
         5. The number on a square tells you how big is that dark or white shape.
            Obviosly in a single shape there must be the same number.
         6. A chocolate 'bar' can have any shape
         7. but it must contain equal number of dark and white squares
         8. which can be indicated on the squares themselves
         9. the shape of the dark squares must be the same of the white ones,
            possibly rotated or mirrored.
         10.Not every bar of dark/white chocolate is marked by numbers
         11.Big numbers indicate a big chocolate 'bar', so look for them first.
            For example a 6 indicates an area of 12 !
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
                    if (this[p + BlackAndWhiteChocolateGame.offset2[i]][BlackAndWhiteChocolateGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + BlackAndWhiteChocolateGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.pos2hint.containsKey(it) }
            for (p in rng)
                pos2state[p] = HintState.Normal
            val area1 = area.filter { game.pos2color[it] == BlackAndWhiteChocolateGame.PUZ_BLACK }
            val area2 = area.filter { game.pos2color[it] == BlackAndWhiteChocolateGame.PUZ_WHITE }
            fun hasLine(): Boolean =
                !area.all { p ->
                    (0..<4).all { i ->
                        (this[p + BlackAndWhiteChocolateGame.offset2[i]][BlackAndWhiteChocolateGame.dirs[i]] == GridLineObject.Line) != area.contains(p + BlackAndWhiteChocolateGame.offset[i])
                    }
                }
            if (area1.size != area2.size || hasLine()) { isSolved = false; continue }
            val s = if (rng.all { game.pos2hint[it] == area1.size }) HintState.Complete else HintState.Error
            for (p in rng) pos2state[p] = s
            if (s != HintState.Complete) { isSolved = false; continue }
            fun f(a: List<Position>): Pair<List<Position>, Position> {
                var (r1, r2) = rows to 0
                var (c1, c2) = cols to 0
                for (p in area) {
                    if (r2 < p.row) r2 = p.row
                    if (r1 > p.row) r1 = p.row
                    if (c2 < p.col) c2 = p.col
                    if (c1 > p.col) c1 = p.col
                }
                val p1 = Position(r1, c1)
                val rs = r2 - r1 + 1
                val cs = c2 - c1 + 1
                return a.map { it - p1 } to Position(rs, cs)
            }
            val (area3, size1) = f(area1)
            val (area4, size2) = f(area2)
            val (rs, cs) = size1
            val size3 = Position(cs, rs)
            if (!(0..<8).any { i ->
                size2 == (if (i % 2 == 0) size1 else size3) &&
                area3.all { p ->
                    val (r3, c3) = p
                    val p2 = when (i) {
                    1 -> Position(c3, rs - 1 - r3)
                    2 -> Position(rs - 1 - r3, cs - 1 - c3)
                    3 -> Position(cs - 1 - c3, r3)
                    4 -> Position(r3, cs - 1 - c3)
                    5 -> Position(cs - 1 - c3, rs - 1 - r3)
                    6 -> Position(rs - 1 - r3, c3)
                    7 -> Position(c3, r3)
                    else -> p
                    }
                    area4.contains(p2)
                }
            }) isSolved = false
        }
    }
}