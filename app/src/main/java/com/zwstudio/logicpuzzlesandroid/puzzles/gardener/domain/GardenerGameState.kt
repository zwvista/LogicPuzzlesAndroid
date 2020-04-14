package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGame
import fj.F
import fj.F0
import fj.data.List
import fj.function.Effect1
import java.util.*

class GardenerGameState(game: GardenerGame?) : CellsGameState<GardenerGame?, GardenerGameMove?, GardenerGameState?>(game) {
    var objArray: Array<GardenerObject?>
    var pos2state: MutableMap<Position?, HintState?> = HashMap()
    var invalidSpacesHorz: MutableSet<Position> = HashSet()
    var invalidSpacesVert: MutableSet<Position> = HashSet()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position?) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: GardenerObject?) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position?, obj: GardenerObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: GardenerGameMove?): Boolean {
        if (!isValid(move!!.p) || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: GardenerGameMove?): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: GardenerObject? ->
            if (obj is GardenerEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) GardenerMarkerObject() else GardenerTreeObject()
            if (obj is GardenerTreeObject) return@label if (markerOption == MarkerOptions.MarkerLast) GardenerMarkerObject() else GardenerEmptyObject()
            if (obj is GardenerMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) GardenerTreeObject() else GardenerEmptyObject()
            obj
        }
        val o = get(move!!.p)
        move.obj = f.f(o)
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
        val allowedObjectsOnly = game!!.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) if (get(r, c) is GardenerForbiddenObject) set(r, c, GardenerEmptyObject())
        val g = Graph()
        val pos2node: MutableMap<Position, Node> = HashMap()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o = get(p)
            val hasNeighbor = F0 {
                fj.data.Array.array<Position>(*GardenerGame.Companion.offset).exists { os: Position? ->
                    val p2 = p.add(os)
                    isValid(p2) && get(p2) is GardenerTreeObject
                }
            }
            if (o is GardenerTreeObject) {
                // 4. Flowers can't be horizontally or vertically touching.
                val s = if (!hasNeighbor.f()) AllowedObjectState.Normal else AllowedObjectState.Error
                o.state = s
                if (s == AllowedObjectState.Error) isSolved = false
            } else {
                // 4. Flowers can't be horizontally or vertically touching.
                if (o !is GardenerForbiddenObject && allowedObjectsOnly && hasNeighbor.f()) set(p, GardenerForbiddenObject())
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for ((p, node) in pos2node) {
            for (os in TierraDelFuegoGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        }
        // 5. All the remaining Garden space where there are no Flowers must be
        // interconnected (horizontally or vertically), as he wants to be able
        // to reach every part of the Garden without treading over Flowers.
        g.setRootNode(fj.data.HashMap.fromMap(pos2node).values().head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false

        // 3. A number tells you how many Flowers you must plant in that Flowerbed.
        // A Flowerbed without number can have any quantity of Flowers.
        for ((p, value) in game!!.pos2hint) {
            val n2 = value!!._1()
            val i = value._2()
            val area = game!!.areas[i]
            var n1 = 0
            for (p2 in area!!) if (get(p2) is GardenerTreeObject) n1++
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly) for (p2 in area) {
                val o = get(p2)
                if (o is GardenerEmptyObject || o is GardenerMarkerObject) set(p2, GardenerForbiddenObject())
            }
        }
        val spaces: MutableList<Position> = ArrayList()
        invalidSpacesHorz.clear()
        invalidSpacesVert.clear()
        // 6. Lastly, there must be enough balance in the Garden, so a straight
        // line (horizontally or vertically) of non-planted tiles can't span
        // for more than two Flowerbeds.
        // 7. In other words, a straight path of empty space can't pass through
        // three or more Flowerbeds.
        val checkSpaces = Effect1 { isHorz: Boolean ->
            if (HashSet(List.iterableList(spaces).map { p: Position? -> game!!.pos2area[p] }.toJavaList()).size > 2) {
                isSolved = false
                (if (isHorz) invalidSpacesHorz else invalidSpacesVert).addAll(spaces)
            }
            spaces.clear()
        }
        for (r in 0 until rows()) {
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val o = get(p)
                if (o is GardenerTreeObject) checkSpaces.f(true) else spaces.add(p)
            }
            checkSpaces.f(true)
        }
        for (c in 0 until cols()) {
            for (r in 0 until rows()) {
                val p = Position(r, c)
                val o = get(p)
                if (o is GardenerTreeObject) checkSpaces.f(false) else spaces.add(p)
            }
            checkSpaces.f(false)
        }
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        for (i in objArray.indices) objArray[i] = GardenerEmptyObject()
        updateIsSolved()
    }
}