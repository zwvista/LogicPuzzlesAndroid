package com.zwstudio.logicpuzzlesandroid.puzzles.gardener

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GardenerGameState(game: GardenerGame) : CellsGameState<GardenerGame, GardenerGameMove, GardenerGameState>(game) {
    var objArray = Array(rows * cols) { GardenerObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()
    var invalidSpacesHorz = mutableSetOf<Position>()
    var invalidSpacesVert = mutableSetOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: GardenerObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: GardenerObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: GardenerGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: GardenerGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            GardenerObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GardenerObject.Marker else GardenerObject.Flower
            GardenerObject.Flower -> if (markerOption == MarkerOptions.MarkerLast) GardenerObject.Marker else GardenerObject.Empty
            GardenerObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GardenerObject.Flower else GardenerObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 7/Gardener

        Summary
        Hitori Flower Planting

        Description
        1. The Board represents a Garden, divided in many rectangular Flowerbeds.
        2. The owner of the Garden wants you to plant Flowers according to these
           rules.
        3. A number tells you how many Flowers you must plant in that Flowerbed.
           A Flowerbed without number can have any quantity of Flowers.
        4. Flowers can't be horizontally or vertically touching.
        5. All the remaining Garden space where there are no Flowers must be
           interconnected (horizontally or vertically), as he wants to be able
           to reach every part of the Garden without treading over Flowers.
        6. Lastly, there must be enough balance in the Garden, so a straight
           line (horizontally or vertically) of non-planted tiles can't span
           for more than two Flowerbeds.
        7. In other words, a straight path of empty space can't pass through
           three or more Flowerbeds.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                if (this[r, c] == GardenerObject.Forbidden)
                    this[r, c] = GardenerObject.Empty
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                fun hasNeighbor(): Boolean {
                    return GardenerGame.offset.any {
                        val p2 = p + it
                        isValid(p2) && this[p2] == GardenerObject.Flower
                    }
                }
                if (o == GardenerObject.Flower) {
                    // 4. Flowers can't be horizontally or vertically touching.
                    val s = if (!hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                    pos2stateAllowed[p] = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else {
                    // 4. Flowers can't be horizontally or vertically touching.
                    if (o != GardenerObject.Forbidden && allowedObjectsOnly && hasNeighbor())
                        this[p] = GardenerObject.Forbidden
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in GardenerGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        }
        // 5. All the remaining Garden space where there are no Flowers must be
        // interconnected (horizontally or vertically), as he wants to be able
        // to reach every part of the Garden without treading over Flowers.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false

        // 3. A number tells you how many Flowers you must plant in that Flowerbed.
        // A Flowerbed without number can have any quantity of Flowers.
        for ((p, value) in game.pos2hint) {
            val n2 = value.first
            val i = value.second
            val area = game.areas[i]
            var n1 = 0
            for (p2 in area)
                if (this[p2] == GardenerObject.Flower) n1++
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[p] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                for (p2 in area) {
                    val o = this[p2]
                    if (o == GardenerObject.Empty || o == GardenerObject.Marker)
                        this[p2] = GardenerObject.Forbidden
                }
        }
        val spaces = mutableListOf<Position>()
        invalidSpacesHorz.clear()
        invalidSpacesVert.clear()
        // 6. Lastly, there must be enough balance in the Garden, so a straight
        // line (horizontally or vertically) of non-planted tiles can't span
        // for more than two Flowerbeds.
        // 7. In other words, a straight path of empty space can't pass through
        // three or more Flowerbeds.
        fun checkSpaces(isHorz: Boolean) {
            if (spaces.map { game.pos2area[it] }.toSet().size > 2) {
                isSolved = false
                (if (isHorz) invalidSpacesHorz else invalidSpacesVert).addAll(spaces)
            }
            spaces.clear()
        }
        for (r in 0..<rows) {
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == GardenerObject.Flower)
                    checkSpaces(true)
                else
                    spaces.add(p)
            }
            checkSpaces(true)
        }
        for (c in 0..<cols) {
            for (r in 0..<rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o == GardenerObject.Flower)
                    checkSpaces(false)
                else
                    spaces.add(p)
            }
            checkSpaces(false)
        }
    }
}