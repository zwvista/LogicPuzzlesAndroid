package com.zwstudio.logicpuzzlesandroid.puzzles.planets

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PlanetsGameState(game: PlanetsGame) : CellsGameState<PlanetsGame, PlanetsGameMove, PlanetsGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: PlanetsObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: PlanetsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: PlanetsGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != PlanetsObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PlanetsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != PlanetsObject.Empty) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            PlanetsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) PlanetsObject.Marker else PlanetsObject.Flower
            PlanetsObject.Flower -> if (markerOption == MarkerOptions.MarkerLast) PlanetsObject.Marker else PlanetsObject.Empty
            PlanetsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) PlanetsObject.Flower else PlanetsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 1/Planets

        Summary
        Planets, Stars and Nebulas

        Description
        1. In Planets you are given an interesting Galaxy, where Suns only
           shine their light in horizontal and vertical lines.
        2. On the board you can see the Planets of this Galaxy. Each Planet
           is lit on some side (or not lit at all).
        3. You should place one Sun on each row and column, according to how
           the Planets are lit.
        4. You should also place one Nebula on each row and column.
        5. Nebulas block sunlight, so if there is a Nebula between a Sun and
           a Planet, the Planet won't be lit.
        6. Finally, Planets block sunlight too. So if there is a Planet
           between a Sun and another Planet, the further Planet won't be lit
           by that Sun.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (this[p]) {
                    PlanetsObject.Forbidden ->
                        this[p] = PlanetsObject.Empty
                    PlanetsObject.Flower -> {
                        pos2state[p] = AllowedObjectState.Normal
                        val node = Node(p.toString())
                        g.addNode(node)
                        pos2node[p] = node
                    }
                    else -> {}
                }
            }
        for ((p, node) in pos2node)
            for (os in PlanetsGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        // 2. More exactly, you have to join the existing flowers by adding more of
        // them, creating a single path of flowers touching horizontally or
        // vertically.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
        val flowers = mutableListOf<Position>()
        // 3. At the same time, you can't line up horizontally or vertically more
        // than 3 flowers (thus Forbidden Four).
        fun checkFlowers() {
            if (flowers.size > 3) {
                isSolved = false
                for (p in flowers)
                    pos2state[p] = AllowedObjectState.Error
            }
            flowers.clear()
        }
        fun checkForbidden(p: Position, indexes: List<Int>) {
            if (!allowedObjectsOnly) return
            for (i in indexes) {
                val os = PlanetsGame.offset[i]
                var p2 = p + os
                while (isValid(p2) && this[p2] == PlanetsObject.Flower) {
                    flowers.add(p2)
                    p2 += os
                }
            }
            if (flowers.size > 2)
                this[p] = PlanetsObject.Forbidden
            flowers.clear()
        }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == PlanetsObject.Flower)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o == PlanetsObject.Empty || o == PlanetsObject.Marker)
                        checkForbidden(p, listOf(1, 3))
                }
            }
            checkFlowers()
        }
        for (c in 0 until cols) {
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o == PlanetsObject.Flower)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o == PlanetsObject.Empty || o == PlanetsObject.Marker)
                        checkForbidden(p, listOf(0, 2))
                }
            }
            checkFlowers()
        }
    }
}