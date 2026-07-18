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
            // 2. Each Box must contain one number.
            if (rng.size != 1) {
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val p2 = rng[0]
            val n1 = area.size
            val n2 = game.pos2hint[p2]
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
            // 1. A simple puzzle where you have to divide the Board in Boxes (Rectangles).
            // 2. The number represents the sum of the width and the height of that Box.
            val s = if (rs * cs == n1 && rs + cs == n2 && !hasLine()) HintState.Complete else HintState.Error
            pos2state[p2] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}