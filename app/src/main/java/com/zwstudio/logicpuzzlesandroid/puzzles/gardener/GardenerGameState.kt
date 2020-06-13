package com.zwstudio.logicpuzzlesandroid.puzzles.gardener

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class GardenerGameState(game: GardenerGame) : CellsGameState<GardenerGame, GardenerGameMove, GardenerGameState>(game) {
    var objArray = Array<GardenerObject>(rows * cols) { GardenerEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()
    var invalidSpacesHorz = mutableSetOf<Position>()
    var invalidSpacesVert = mutableSetOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: GardenerObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: GardenerObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: GardenerGameMove): Boolean {
        if (!isValid(move.p) || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: GardenerGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when(o) {
            is GardenerEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) GardenerMarkerObject() else GardenerTreeObject()
            is GardenerTreeObject -> if (markerOption == MarkerOptions.MarkerLast) GardenerMarkerObject() else GardenerEmptyObject()
            is GardenerMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) GardenerTreeObject() else GardenerEmptyObject()
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
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is GardenerForbiddenObject)
                    this[r, c] = GardenerEmptyObject()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                fun hasNeighbor(): Boolean {
                    return GardenerGame.offset.any {
                        val p2 = p + it
                        isValid(p2) && this[p2] is GardenerTreeObject
                    }
                }
                if (o is GardenerTreeObject) {
                    // 4. Flowers can't be horizontally or vertically touching.
                    val s = if (!hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else {
                    // 4. Flowers can't be horizontally or vertically touching.
                    if (o !is GardenerForbiddenObject && allowedObjectsOnly && hasNeighbor())
                        this[p] = GardenerForbiddenObject()
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in GardenerGame.offset) {
                val p2 = p + os
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
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
                if (this[p2] is GardenerTreeObject) n1++
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                for (p2 in area) {
                    val o = this[p2]
                    if (o is GardenerEmptyObject || o is GardenerMarkerObject)
                        this[p2] = GardenerForbiddenObject()
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
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is GardenerTreeObject)
                    checkSpaces(true)
                else
                    spaces.add(p)
            }
            checkSpaces(true)
        }
        for (c in 0 until cols) {
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o is GardenerTreeObject)
                    checkSpaces(false)
                else
                    spaces.add(p)
            }
            checkSpaces(false)
        }
    }
}