package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.data.List
import java.util.*

class PaintTheNurikabeGameState(game: PaintTheNurikabeGame) : CellsGameState<PaintTheNurikabeGame, PaintTheNurikabeGameMove, PaintTheNurikabeGameState>(game) {
    var objArray: Array<PaintTheNurikabeObject?>
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: PaintTheNurikabeObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: PaintTheNurikabeObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: PaintTheNurikabeGameMove): Boolean {
        val p: Position = move.p
        val o: PaintTheNurikabeObject = move.obj
        if (!isValid(p) || get(p) == o) return false
        set(p, o)
        for (p2 in game.areas.get(game.pos2area.get(p))) set(p2, o)
        updateIsSolved()
        return true
    }

    fun switchObject(move: PaintTheNurikabeGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<PaintTheNurikabeObject, PaintTheNurikabeObject> = label@ F<PaintTheNurikabeObject, PaintTheNurikabeObject> { obj: PaintTheNurikabeObject? ->
            when (obj) {
                PaintTheNurikabeObject.Empty -> return@label if (markerOption == MarkerOptions.MarkerFirst) PaintTheNurikabeObject.Marker else PaintTheNurikabeObject.Painted
                PaintTheNurikabeObject.Painted -> return@label if (markerOption == MarkerOptions.MarkerLast) PaintTheNurikabeObject.Marker else PaintTheNurikabeObject.Empty
                PaintTheNurikabeObject.Marker -> return@label if (markerOption == MarkerOptions.MarkerFirst) PaintTheNurikabeObject.Painted else PaintTheNurikabeObject.Empty
            }
            obj
        }
        val p: Position = move.p
        if (!isValid(p)) return false
        val o: PaintTheNurikabeObject? = get(p)
        move.obj = f.f(o)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Paint The Nurikabe

        Summary
        Paint areas, find Nurikabes

        Description
        1. By painting (filling) the areas you have to complete a Nurikabe.
           Specifically:
        2. A number indicates how many painted tiles are adjacent to it.
        3. The painted tiles form an orthogonally continuous area, like a
           Nurikabe.
        4. There can't be any 2*2 area of the same color(painted or empty).
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o: PaintTheNurikabeObject? = get(r, c)
            if (o == PaintTheNurikabeObject.Forbidden) set(r, c, PaintTheNurikabeObject.Empty)
        }
        // 2. A number indicates how many painted tiles are adjacent to it.
        for ((p, n2) in game.pos2hint.entries) {
            val rng = mutableListOf<Position>()
            var n1 = 0
            for (os in PaintTheNurikabeGame.offset) {
                val p2 = p.add(os)
                if (!isValid(p2)) continue
                val o: PaintTheNurikabeObject? = get(p2)
                if (o == PaintTheNurikabeObject.Painted) n1++ else if (o == PaintTheNurikabeObject.Empty) rng.add(p2)
            }
            val s: HintState = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false else if (allowedObjectsOnly) for (p2 in rng) set(p2, PaintTheNurikabeObject.Forbidden)
        }
        // 4. There can't be any 2*2 area of the same color(painted or empty).
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            if (fj.data.Array.array<Position>(*NurikabeGame.offset2).forall(F<Position, Boolean> { os: Position? -> get(p.add(os)) == PaintTheNurikabeObject.Painted }) ||
                fj.data.Array.array<Position>(*NurikabeGame.offset2).forall(F<Position, Boolean> { os: Position? -> get(p.add(os)) == PaintTheNurikabeObject.Empty })) {
                isSolved = false
                return
            }
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            if (get(p) == PaintTheNurikabeObject.Painted) {
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for (p in pos2node.keys) for (os in NurikabeGame.offset) {
            val p2 = p.add(os)
            if (pos2node.containsKey(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        // 3. The painted tiles form an orthogonally continuous area, like a
        // Nurikabe.
        g.setRootNode(List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }

    init {
        objArray = arrayOfNulls<PaintTheNurikabeObject>(rows() * cols())
        Arrays.fill(objArray, PaintTheNurikabeObject.Empty)
        for (p in game.pos2hint.keys) pos2state[p] = HintState.Normal
        updateIsSolved()
    }
}