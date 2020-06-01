package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.*

class TierraDelFuegoGameState(game: TierraDelFuegoGame) : CellsGameState<TierraDelFuegoGame, TierraDelFuegoGameMove, TierraDelFuegoGameState>(game) {
    var objArray: Array<TierraDelFuegoObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: TierraDelFuegoObject?) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: TierraDelFuegoObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: TierraDelFuegoGameMove): Boolean {
        if (!isValid(move.p) || game.pos2hint.get(move.p) != null || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: TierraDelFuegoGameMove): Boolean {
        if (!isValid(move.p) || game.pos2hint.get(move.p) != null) return false
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<TierraDelFuegoObject, TierraDelFuegoObject> = label@ F<TierraDelFuegoObject, TierraDelFuegoObject> { obj: TierraDelFuegoObject? ->
            if (obj is TierraDelFuegoEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TierraDelFuegoMarkerObject() else TierraDelFuegoTreeObject()
            if (obj is TierraDelFuegoTreeObject) return@label if (markerOption == MarkerOptions.MarkerLast) TierraDelFuegoMarkerObject() else TierraDelFuegoEmptyObject()
            if (obj is TierraDelFuegoMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TierraDelFuegoTreeObject() else TierraDelFuegoEmptyObject()
            obj
        }
        val o: TierraDelFuegoObject? = get(move.p)
        move.obj = f.f(o)
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
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o: TierraDelFuegoObject? = get(p)
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
            if (o is TierraDelFuegoForbiddenObject) set(p, TierraDelFuegoEmptyObject()) else if (o is TierraDelFuegoTreeObject) o.state = AllowedObjectState.Normal else if (o is TierraDelFuegoHintObject) o.state = HintState.Normal
        }
        for ((p, node) in pos2node) {
            val b1 = get(p) is TierraDelFuegoTreeObject
            for (os in TierraDelFuegoGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2] ?: continue
                val b2 = get(p2) is TierraDelFuegoTreeObject
                if (b1 == b2) g.connectNode(node, node2)
            }
        }
        while (!pos2node.isEmpty()) {
            g.setRootNode(fj.data.HashMap.fromMap(pos2node).values().head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter(F<P2<Position, Node>, Boolean> { e: P2<Position?, Node?> -> nodeList.contains(e._2()) }).map<Position>(F<P2<Position, Node>, Position> { e: P2<Position?, Node?> -> e._1() }).toJavaList()
            if (get(fj.data.HashMap.fromMap(pos2node).keys().head()) is TierraDelFuegoTreeObject) {
                // 3. The archipelago is peculiar because all bodies of water separating the
                // islands are identical in shape and occupied a 2*1 or 1*2 space.
                // 4. These bodies of water can only touch diagonally.
                if (area.size != 2) isSolved = false else if (allowedObjectsOnly) for (p in area) for (os in TierraDelFuegoGame.offset) {
                    val p2 = p.add(os)
                    if (!isValid(p2)) continue
                    val o: TierraDelFuegoObject? = get(p2)
                    if (o is TierraDelFuegoEmptyObject || o is TierraDelFuegoMarkerObject) set(p, TierraDelFuegoForbiddenObject())
                }
                if (area.size > 2) for (p in area) (get(p) as TierraDelFuegoTreeObject?)!!.state = AllowedObjectState.Error
            } else {
                // 2. Being organized in tribes, each tribe, marked with a different letter,
                // has occupied an island in the archipelago.
                val ids = mutableSetOf<Char>()
                for (p in area) {
                    val o: TierraDelFuegoObject? = get(p)
                    if (o is TierraDelFuegoHintObject) ids.add(o.id)
                }
                if (ids.size == 1) for (p in area) {
                    val o: TierraDelFuegoObject? = get(p)
                    if (o is TierraDelFuegoHintObject) o.state = HintState.Complete
                } else isSolved = false
            }
            for (p in area) pos2node.remove(p)
        }
    }

    init {
        objArray = arrayOfNulls<TierraDelFuegoObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = TierraDelFuegoEmptyObject()
        for ((p, ch) in game.pos2hint.entries) {
            set(p, object : TierraDelFuegoHintObject() {
                init {
                    id = ch
                }
            })
        }
        updateIsSolved()
    }
}