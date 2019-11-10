package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import java.util.*

class WallSentinels2GameState(game: WallSentinels2Game) : CellsGameState<WallSentinels2Game, WallSentinels2GameMove, WallSentinels2GameState>(game) {
    // https://stackoverflow.com/questions/46846025/how-to-clone-or-copy-a-list-in-kotlin
    private var objArray = game.objArray.toMutableList()
    var pos2state: Map<Position, HintState> = HashMap()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: WallSentinels2Object) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: WallSentinels2Object) {this[p.row, p.col] = obj}

    fun setObject(move: WallSentinels2GameMove): Boolean {
        if (this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: WallSentinels2GameMove): Boolean {
        val o = this[move.p]
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        move.obj = when(o) {
            is WallSentinels2EmptyObject ->
                if (markerOption == MarkerOptions.MarkerFirst) WallSentinels2MarkerObject()
                else WallSentinels2WallObject()
            is WallSentinels2WallObject ->
                if (markerOption == MarkerOptions.MarkerLast) WallSentinels2MarkerObject()
                else WallSentinels2EmptyObject()
            is WallSentinels2MarkerObject ->
                if (markerOption == MarkerOptions.MarkerFirst) WallSentinels2WallObject()
                else WallSentinels2EmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 11/Wall Sentinels

        Summary
        It's midnight and all is well!

        Description
        1. On the board there is a single continuous castle wall, which you
           must discover.
        2. The numbers on the board represent Sentinels (in a similar way to
           'Sentinels'). The Sentinels can be placed on the Wall or Land.
        3. The number tells you how many tiles that Sentinel can control (see)
           from there vertically and horizontally - of his type of tile.
        4. That means the number of a Land Sentinel indicates how many Land tiles
           are visible from there, up to Wall tiles or the grid border.
        5. That works the opposite way for Wall Sentinels: they control all the
           Wall tiles up to Land tiles or the grid border.
        6. The number includes the tile itself, where the Sentinel is located
           (again, like 'Sentinels').
        7. Lastly there is a single, orthogonally contiguous, Wall and it cannot
           contain 2*2 Wall tiles - just like Nurikabe.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val o = this[p]
                fun f(isWall: Boolean, n2: Int) {
                    var n1 = 1
                    for (os in WallSentinels2Game.offset) {
                        val p2 = p.add(os)
                        while (isValid(p2)) {
                            val o2 = this[p2]
                            val isWall2 = o2 is WallSentinels2WallObject || o2 is WallSentinels2HintWallObject
                            if (isWall2 != isWall) break
                            n1++
                            p2.addBy(os)
                        }
                    }
                    // 3. The number tells you how many tiles that Sentinel can control (see)
                    // from there vertically and horizontally - of his type of tile.
                    val s = when {
                        if (isWall) n1 < n2 else n1 > n2 -> HintState.Normal
                        n1 == n2 -> HintState.Complete
                        else -> HintState.Error
                    }
                    if (isWall)
                        (o as WallSentinels2HintWallObject).state = s
                    else
                        (o as WallSentinels2HintLandObject).state = s
                    if (s != HintState.Complete) isSolved = false
                }
                if (o is WallSentinels2HintLandObject)
                    f(false, o.tiles)
                else if (o is WallSentinels2HintWallObject)
                    f(true, o.tiles)
            }
        if (!isSolved) return
        val g = Graph()
        val pos2node = HashMap<Position, Node>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val o = this[p]
                if (o is WallSentinels2WallObject || o is WallSentinels2HintWallObject) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                    // 7. The Wall cannot contain 2*2 Wall tiles.
                    if (WallSentinels2Game.offset2.all {
                        val p2 = p.add(it)
                        if (!isValid(p2))
                            false
                        else {
                            val o2 = this[p2]
                            o2 is WallSentinels2WallObject || o2 is WallSentinels2HintWallObject
                        }
                    }) { isSolved = false; return }
                }
            }
        for ((p, node) in pos2node)
            for (os in WallSentinels2Game.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        // 7. Lastly there is a single, orthogonally contiguous, Wall - just like Nurikabe.
        g.setRootNode(pos2node.values.elementAt(0))
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}
