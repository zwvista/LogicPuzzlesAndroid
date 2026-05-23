package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TierraDelFuegoGameState(game: TierraDelFuegoGame) : CellsGameState<TierraDelFuegoGame, TierraDelFuegoGameMove, TierraDelFuegoGameState>(game) {
    val objArray = Array(rows * cols) { TierraDelFuegoObject.Empty }
    val pos2stateHint = mutableMapOf<Position, HintState>()
    val pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: TierraDelFuegoObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: TierraDelFuegoObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = TierraDelFuegoObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: TierraDelFuegoGameMove): GameOperationType {
        if (!isValid(move.p) || game.pos2hint[move.p] != null || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TierraDelFuegoGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2hint[p] != null) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            TierraDelFuegoObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) TierraDelFuegoObject.Marker else TierraDelFuegoObject.Water
            TierraDelFuegoObject.Water -> if (markerOption == MarkerOptions.MarkerLast) TierraDelFuegoObject.Marker else TierraDelFuegoObject.Empty
            TierraDelFuegoObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) TierraDelFuegoObject.Water else TierraDelFuegoObject.Empty
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
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                when (this[p]) {
                    TierraDelFuegoObject.Forbidden -> this[p] = TierraDelFuegoObject.Empty
                    TierraDelFuegoObject.Water -> pos2stateAllowed[p] = AllowedObjectState.Normal
                    TierraDelFuegoObject.Hint -> pos2stateHint[p] = HintState.Normal
                    else -> {}
                }
            }
        for ((p, node) in pos2node) {
            val b1 = this[p] == TierraDelFuegoObject.Water
            for (os in TierraDelFuegoGame.offset) {
                val p2 = p + os
                val node2 = pos2node[p2] ?: continue
                val b2 = this[p2] == TierraDelFuegoObject.Water
                if (b1 == b2)
                    g.connectNode(node, node2)
            }
        }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            if (this[pos2node.keys.first()] == TierraDelFuegoObject.Water) {
                // 3. The archipelago is peculiar because all bodies of water separating the
                // islands are identical in shape and occupied a 2*1 or 1*2 space.
                // 4. These bodies of water can only touch diagonally.
                if (area.size != 2)
                    isSolved = false
                else if (allowedObjectsOnly)
                    for (p in area)
                        for (os in TierraDelFuegoGame.offset) {
                            val p2 = p + os
                            if (!isValid(p2)) continue
                            val o = this[p2]
                            if (o == TierraDelFuegoObject.Empty || o == TierraDelFuegoObject.Marker)
                                this[p2] = TierraDelFuegoObject.Forbidden
                        }
                if (area.size > 2)
                    for (p in area)
                        pos2stateAllowed[p] = AllowedObjectState.Error
            } else {
                // 2. Being organized in tribes, each tribe, marked with a different letter,
                // has occupied an island in the archipelago.
                val ids = mutableSetOf<Char>()
                for (p in area) {
                    val id = game.pos2hint[p] ?: continue
                    ids.add(id)
                }
                if (ids.size == 1)
                    for (p in area) {
                        val o = this[p]
                        if (o == TierraDelFuegoObject.Hint)
                            pos2stateHint[p] = HintState.Complete
                    }
                else
                    isSolved = false
            }
            for (p in area)
                pos2node.remove(p)
        }
    }
}