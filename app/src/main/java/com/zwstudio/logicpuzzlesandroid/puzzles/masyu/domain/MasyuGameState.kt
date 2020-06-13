package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MasyuGameState(game: MasyuGame) : CellsGameState<MasyuGame, MasyuGameMove, MasyuGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: MasyuGameMove): Boolean {
        val p: Position = move.p
        val dir: Int = move.dir
        val p2 = p + MasyuGame.offset[dir]
        val dir2 = (dir + 2) % 4
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return true
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/Masyu

        Summary
        Draw a Necklace that goes through every Pearl

        Description
        1. The goal is to draw a single Loop(Necklace) through every circle(Pearl)
           that never branches-off or crosses itself.
        2. The rules to pass Pearls are:
        3. Lines passing through White Pearls must go straight through them.
           However, at least at one side of the White Pearl(or both), they must
           do a 90 degree turn.
        4. Lines passing through Black Pearls must do a 90 degree turn in them.
           Then they must go straight in the next tile in both directions.
        5. Lines passing where there are no Pearls can do what they want.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val pos2Dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = get(r, c)
                val ch = game[r, c]
                val dirs = mutableListOf<Int>()
                for (i in 0 until 4)
                    if (o[i])
                        dirs.add(i)
                when (dirs.size) {
                    0 ->                     // 1. The goal is to draw a single Loop(Necklace) through every circle(Pearl)
                        if (ch != ' ') {
                            isSolved = false
                            return
                        }
                    2 -> {
                        val node = Node(p.toString())
                        g.addNode(node)
                        pos2node[p] = node
                        pos2Dirs[p] = dirs
                        when (ch) {
                            'B' ->                         // 4. Lines passing through Black Pearls must do a 90 degree turn in them.
                                if (dirs[1] - dirs[0] == 2) {
                                    isSolved = false
                                    return
                                }
                            'W' ->                         // 3. Lines passing through White Pearls must go straight through them.
                                if (dirs[1] - dirs[0] != 2) {
                                    isSolved = false
                                    return
                                }
                        }
                    }
                    else -> {
                        // 1. The goal is to draw a single Loop(Necklace)
                        // that never branches-off or crosses itself.
                        isSolved = false
                        return
                    }
                }
            }
        for ((p, _) in pos2node) {
            val dirs = pos2Dirs[p]!!
            val ch = game[p]
            var bW = ch != 'W'
            for (i in dirs) {
                val p2 = p + MasyuGame.offset[i]
                val node2 = pos2node[p2]
                if (node2 == null) {
                    isSolved = false
                    return
                }
                val dirs2 = pos2Dirs[p2]!!
                when (ch) {
                    'B' ->                         // 4. Lines passing through Black Pearls must go straight
                        // in the next tile in both directions.
                        if (!((i == 0 || i == 2) && dirs2[0] == 0 && dirs2[1] == 2 ||
                                (i == 1 || i == 3) && dirs2[0] == 1 && dirs2[1] == 3)) {
                            isSolved = false
                            return
                        }
                    'W' -> {
                        // 3. At least at one side of the White Pearl(or both),
                        // Lines passing through White Pearls must do a 90 degree turn.
                        val n1 = (i + 1) % 4
                        val n2 = (i + 3) % 4
                        if (dirs2[0] == n1 || dirs2[0] == n2 || dirs2[1] == n1 || dirs2[1] == n2) bW = true
                        if (dirs[1] - dirs[0] != 2) {
                            isSolved = false
                            return
                        }
                    }
                }
                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
            if (!bW) {
                isSolved = false
                return
            }
        }
        // 1. The goal is to draw a single Loop(Necklace).
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}