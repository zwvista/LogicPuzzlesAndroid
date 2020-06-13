package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class TierraDelFuegoGameState(game: TierraDelFuegoGame) : CellsGameState<TierraDelFuegoGame, TierraDelFuegoGameMove, TierraDelFuegoGameState>(game) {
    var objArray = Array<TierraDelFuegoObject>(rows * cols) { TierraDelFuegoEmptyObject() }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: TierraDelFuegoObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: TierraDelFuegoObject) {this[p.row, p.col] = obj}

    init {
        for ((p, ch) in game.pos2hint)
            this[p] = TierraDelFuegoHintObject(id = ch)
        updateIsSolved()
    }

    fun setObject(move: TierraDelFuegoGameMove): Boolean {
        if (!isValid(move.p) || game.pos2hint[move.p] != null || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: TierraDelFuegoGameMove): Boolean {
        if (!isValid(move.p) || game.pos2hint[move.p] != null) return false
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is TierraDelFuegoEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) TierraDelFuegoMarkerObject() else TierraDelFuegoTreeObject()
            is TierraDelFuegoTreeObject -> if (markerOption == MarkerOptions.MarkerLast) TierraDelFuegoMarkerObject() else TierraDelFuegoEmptyObject()
            is TierraDelFuegoMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) TierraDelFuegoTreeObject() else TierraDelFuegoEmptyObject()
            else -> o

        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 11/Tierra Del Fuego

        Summary
        Fuegians!

        Description
        1. The board represents the 'Tierra del Fuego' archipelago, where native
           tribes, the Fuegians, live.
        2. Being organized in tribes, each tribe, marked with a different letter,
           has occupied an island in the archipelago.
        3. The archipelago is peculiar because all bodies of water separating the
           islands are identical in shape and occupied a 2*1 or 1*2 space.
        4. These bodies of water can only touch diagonally.
        5. Your task is to find these bodies of water.
        6. Please note there are no hidden tribes or islands without a tribe on it.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (o is TierraDelFuegoForbiddenObject)
                    this[p] = TierraDelFuegoEmptyObject()
                else if (o is TierraDelFuegoTreeObject)
                    o.state = AllowedObjectState.Normal
                else if (o is TierraDelFuegoHintObject)
                    o.state = HintState.Normal
            }
        for ((p, node) in pos2node) {
            val b1 = this[p] is TierraDelFuegoTreeObject
            for (os in TierraDelFuegoGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2] ?: continue
                val b2 = this[p2] is TierraDelFuegoTreeObject
                if (b1 == b2)
                    g.connectNode(node, node2)
            }
        }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            if (this[pos2node.keys.first()] is TierraDelFuegoTreeObject) {
                // 3. The archipelago is peculiar because all bodies of water separating the
                // islands are identical in shape and occupied a 2*1 or 1*2 space.
                // 4. These bodies of water can only touch diagonally.
                if (area.size != 2)
                    isSolved = false
                else if (allowedObjectsOnly)
                    for (p in area)
                        for (os in TierraDelFuegoGame.offset) {
                            val p2 = p.add(os)
                            if (!isValid(p2)) continue
                            val o = this[p2]
                            if (o is TierraDelFuegoEmptyObject || o is TierraDelFuegoMarkerObject)
                                this[p] = TierraDelFuegoForbiddenObject()
                        }
                if (area.size > 2)
                    for (p in area)
                        (this[p] as TierraDelFuegoTreeObject).state = AllowedObjectState.Error
            } else {
                // 2. Being organized in tribes, each tribe, marked with a different letter,
                // has occupied an island in the archipelago.
                val ids = mutableSetOf<Char>()
                for (p in area) {
                    val o = this[p]
                    if (o is TierraDelFuegoHintObject)
                        ids.add(o.id)
                }
                if (ids.size == 1)
                    for (p in area) {
                        val o = this[p]
                        if (o is TierraDelFuegoHintObject)
                            o.state = HintState.Complete
                    }
                else
                    isSolved = false
            }
            for (p in area)
                pos2node.remove(p)
        }
    }
}