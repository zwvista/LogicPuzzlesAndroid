package com.zwstudio.logicpuzzlesandroid.puzzles.snakeomino

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnakeominoGameState(game: SnakeominoGame) : CellsGameState<SnakeominoGame, SnakeominoGameMove, SnakeominoGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, HintState>()
    val snakes = mutableListOf<List<Position>>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    override fun setObject(move: SnakeominoGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != SnakeominoGame.PUZ_EMPTY || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: SnakeominoGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != SnakeominoGame.PUZ_EMPTY) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            SnakeominoGame.PUZ_EMPTY -> 2
            game.nMax -> SnakeominoGame.PUZ_EMPTY
            else -> o + 1
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 4/Snake-omino

        Summary
        Snakes on a Plain

        Description
        1. Find Snakes by numbering them:
        2. A snake is a one-cell-wide path at least two cells long. A snake cannot touch itself,
           not even diagonally.
        3. A cell with a circle must be at one of the ends of a snake. A snake may contain one
           circled cell, two circled cells, or no circled cells at all.
        4. A cell with a number must be part of a snake with a length of exactly that number of cells.
        5. Two snakes of the same length must not be orthogonally adjacent.
        6. A cell with a cross cannot be an end of a snake.
        7. every cell in the board is part of a snake.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                // 7. every cell in the board is part of a snake.
                if (this[p] == SnakeominoGame.PUZ_EMPTY)
                    isSolved = false
                else {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            val o = this[p]
            for (os in SnakeominoGame.offset) {
                val p2 = p + os
                if (isValid(p2) && this[p2] == o)
                    g.connectNode(node, pos2node[p2]!!)
            }
        }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val snake = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in snake)
                pos2node.remove(p)
            if (run {
                // 2. A snake is at least two cells long.
                // 4. A cell with a number must be part of a snake with a length of exactly that number of cells.
                if (!(snake.size >= 2 && snake.size == this[snake[0]])) return@run false
                val num2rng = mutableMapOf<Int, MutableList<Position>>()
                for (p in snake) {
                    val cnt = SnakeominoGame.offset.count { snake.contains(p + it) }
                    num2rng.getOrPut(cnt) { mutableListOf() }.add(p)
                }
                // 2. A snake is a one-cell-wide path.
                if (num2rng.any { (num, _) -> num > 2 }) return@run false
                val (rng1, rng2) = num2rng[1] to num2rng[2]
                // 2. A snake cannot touch itself, not even diagonally.
                // 3. A cell with a circle must be at one of the ends of a snake. A snake may contain one
                //   circled cell, two circled cells, or no circled cells at all.
                // 6. A cell with a cross cannot be an end of a snake.
                if (rng1?.any {
                    game.pos2hint[it] == SnakeominoGame.PUZ_NOT_END
                } ?: true || rng2?.any {
                    game.pos2hint[it] == SnakeominoGame.PUZ_END
                } ?: false) return@run false
                return@run true
            })
                snakes.add(snake)
            else
                isSolved = false
        }
    }
}
