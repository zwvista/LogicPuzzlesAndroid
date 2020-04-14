package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import java.util.*

class CastleBaileyGameState(game: CastleBaileyGame) : CellsGameState<CastleBaileyGame, CastleBaileyGameMove, CastleBaileyGameState>(game) {
    // https://stackoverflow.com/questions/43172947/kotlin-creating-a-mutable-list-with-repeating-elements
    var objArray = MutableList(rows() * cols()) { CastleBaileyObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: CastleBaileyObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: CastleBaileyObject) {this[p.row, p.col] = obj}

    fun setObject(move: CastleBaileyGameMove): Boolean {
        if (this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: CastleBaileyGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        fun f(obj: CastleBaileyObject) =
            when (obj) {
                CastleBaileyObject.Empty ->
                    if (markerOption == MarkerOptions.MarkerFirst) CastleBaileyObject.Marker
                    else CastleBaileyObject.Wall
                CastleBaileyObject.Wall ->
                    if (markerOption == MarkerOptions.MarkerLast) CastleBaileyObject.Marker
                    else CastleBaileyObject.Empty
                CastleBaileyObject.Marker ->
                    if (markerOption == MarkerOptions.MarkerFirst) CastleBaileyObject.Wall
                    else CastleBaileyObject.Empty
                else -> obj
            }
        val o = this[move.p]
        move.obj = f(o)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Castle Bailey

        Summary
        Towers, keeps and curtain walls

        Description
        1. A very convoluted Medieval Architect devised a very weird Bailey
           (or Ward, the inner area contained in the Outer Walls of a Castle).
        2. He deployed quite a few towers (the circles) and put a number on
           top of each one.
        3. The number tells you how many pieces (squares) of wall it touches.
        4. So the number can go from 0 (no walls around the tower) to 4 (tower
           entirely surrounded by walls).
        5. Board borders don't count as walls, so there you'll have two walls
           at most (or one in corners).
        6. To facilitate movement in the castle, the Bailey must have a single
           continuous area (Garden).
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                if (this[p] == CastleBaileyObject.Forbidden)
                    this[p] = CastleBaileyObject.Empty
            }
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            val rng = mutableListOf<Position>()
            for (os in CastleBaileyGame.offset2) {
                val p2 = p.add(os)
                if (!isValid(p2)) continue
                when (this[p2]) {
                    CastleBaileyObject.Empty, CastleBaileyObject.Marker -> rng.add(p2)
                    CastleBaileyObject.Wall -> n1++
                    else -> {}
                }
            }
            // 3. The number tells you how many pieces (squares) of wall it touches.
            // 4. So the number can go from 0 (no walls around the tower) to 4 (tower
            // entirely surrounded by walls).
            // 5. Board borders don't count as walls, so there you'll have two walls
            // at most (or one in corners).
            val s = when {
                n1 < n2 -> HintState.Normal
                n1 == n2 -> HintState.Complete
                else -> HintState.Error
            }
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = CastleBaileyObject.Forbidden
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = HashMap<Position, Node>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                if (this[p] != CastleBaileyObject.Wall) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in CastleBaileyGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        // 6. To facilitate movement in the castle, the Bailey must have a single
        // continuous area (Garden).
        g.setRootNode(pos2node.values.elementAt(0))
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}
