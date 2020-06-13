package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*

class CalcudokuGame(layout: List<String>, gi: GameInterface<CalcudokuGame, CalcudokuGameMove, CalcudokuGameState>, gdi: GameDocumentInterface) : CellsGame<CalcudokuGame, CalcudokuGameMove, CalcudokuGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        var dirs = intArrayOf(1, 0, 3, 2)
    }

    var areas = mutableListOf<List<Position>>()
    var pos2area = mutableMapOf<Position, Int>()
    var dots: GridDots
    var objArray: CharArray
    var pos2hint = mutableMapOf<Position, CalcudokuHint>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position): Char = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length / 4)
        dots = GridDots(rows + 1, cols + 1)
        objArray = CharArray(rows * cols) { ' ' }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch1 = str[c * 4]
                val s = str.substring(c * 4 + 1, c * 4 + 3)
                val ch2 = str[c * 4 + 3]
                this[p] = ch1
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (s == "  ") continue
                pos2hint[p] = CalcudokuHint(ch2, if (s == "  ") 0 else s.trim(' ').toInt())
            }
        }
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = this[p]
                if (ch == ' ') continue
                for (os in offset) {
                    val p2 = p + os
                    if (isValid(p2) && get(p2) == ch)
                        g.connectNode(pos2node[p]!!, pos2node[p2]!!)
                }
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            val n = areas.size
            val ch = this[area[0]]
            for (p in area) {
                pos2area[p] = n
                pos2node.remove(p)
                for (i in 0 until 4) {
                    val p2 = p + offset[i]
                    val ch2 = if (!isValid(p2)) '.' else this[p2]
                    if (ch2 != ch)
                        dots[p + offset2[i], dirs[i]] = GridLineObject.Line
                }
            }
            areas.add(area)
        }
        for (r in 0 until rows) {
            dots[r, 0, 2] = GridLineObject.Line
            dots[r + 1, 0, 0] = GridLineObject.Line
            dots[r, cols, 2] = GridLineObject.Line
            dots[r + 1, cols, 0] = GridLineObject.Line
        }
        for (c in 0 until cols) {
            dots[0, c, 1] = GridLineObject.Line
            dots[0, c + 1, 3] = GridLineObject.Line
            dots[rows, c, 1] = GridLineObject.Line
            dots[rows, c + 1, 3] = GridLineObject.Line
        }
        val state = CalcudokuGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: CalcudokuGameMove, f: (CalcudokuGameState, CalcudokuGameMove) -> Boolean): Boolean {
        if (canRedo) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(currentState)
        val changed = f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: CalcudokuGameMove) = changeObject(move, CalcudokuGameState::switchObject)
    fun setObject(move: CalcudokuGameMove) = changeObject(move, CalcudokuGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
    fun getPosState(p: Position) = currentState.pos2state[p]
}
