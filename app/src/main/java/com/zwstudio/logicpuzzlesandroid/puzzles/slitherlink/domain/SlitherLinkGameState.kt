package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.data.List
import java.util.*

class SlitherLinkGameState(game: SlitherLinkGame?) : CellsGameState<SlitherLinkGame?, SlitherLinkGameMove?, SlitherLinkGameState?>(game) {
    var objArray: Array<Array<GridLineObject?>>
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject?>) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: Array<GridLineObject?>) {
        set(p.row, p.col, obj)
    }

    private fun isValidMove(move: SlitherLinkGameMove) = !(move.p!!.row == rows() - 1 && move.dir == 2 || move.p!!.col == cols() - 1 && move.dir == 1)

    fun setObject(move: SlitherLinkGameMove): Boolean {
        if (!isValidMove(move)) return false
        val p1: Position = move.p
        val dir: Int = move.dir
        val dir2 = (dir + 2) % 4
        val o: GridLineObject? = get(p1)[dir]
        if (o == move.obj) return false
        val p2 = p1.add(SlitherLinkGame.offset.get(dir))
        get(p1)[dir] = move.obj
        get(p2)[dir2] = get(p1)[dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: SlitherLinkGameMove): Boolean {
        if (!isValidMove(move)) return false
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<GridLineObject, GridLineObject> = label@ F<GridLineObject, GridLineObject> { obj: GridLineObject? ->
            when (obj) {
                GridLineObject.Empty -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
                GridLineObject.Line -> return@label if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
                GridLineObject.Marker -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            }
            obj
        }
        val dotObj: Array<GridLineObject?> = get(move.p)
        move.obj = f.f(dotObj[move.dir])
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/SlitherLink

        Summary
        Draw a loop a-la-minesweeper!

        Description
        1. Draw a single looping path with the aid of the numbered hints. The path
           cannot have branches or cross itself.
        2. Each number in a tile tells you on how many of its four sides are touched
           by the path.
        3. For example:
        4. A 0 tells you that the path doesn't touch that square at all.
        5. A 1 tells you that the path touches that square ONLY one-side.
        6. A 3 tells you that the path does a U-turn around that square.
        7. There can't be tiles marked with 4 because that would form a single
           closed loop in it.
        8. Empty tiles can have any number of sides touched by that path.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. Each number in a tile tells you on how many of its four sides are touched
        // by the path.
        for ((p, n2) in game.pos2hint.entries) {
            var n1 = 0
            if (get(p)[1] == GridLineObject.Line) n1++
            if (get(p)[2] == GridLineObject.Line) n1++
            if (get(p.add(Position(1, 1)))[0] == GridLineObject.Line) n1++
            if (get(p.add(Position(1, 1)))[3] == GridLineObject.Line) n1++
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val n: Int = fj.data.Array.array<GridLineObject>(*get(p)).filter(F<GridLineObject, Boolean> { o: GridLineObject -> o == GridLineObject.Line }).length()
            when (n) {
                0 -> continue
                2 -> {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
                else -> {
                    // 1. The path cannot have branches or cross itself.
                    isSolved = false
                    return
                }
            }
        }
        for (p in pos2node.keys) {
            val dotObj: Array<GridLineObject?> = get(p)
            for (i in 0..3) {
                if (dotObj[i] != GridLineObject.Line) continue
                val p2 = p.add(SlitherLinkGame.offset.get(i))
                g.connectNode(pos2node[p], pos2node[p2])
            }
        }
        // 1. Draw a single looping path with the aid of the numbered hints.
        g.setRootNode(List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }

    init {
        objArray = arrayOfNulls<Array<GridLineObject?>>(rows() * cols())
        for (i in objArray.indices) {
            objArray[i] = arrayOfNulls<GridLineObject>(4)
            Arrays.fill(objArray[i], GridLineObject.Empty)
        }
        updateIsSolved()
    }
}