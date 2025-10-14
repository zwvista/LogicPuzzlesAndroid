package com.zwstudio.logicpuzzlesandroid.puzzles.hidoku

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.TreeMap

class HidokuGameState(game: HidokuGame) : CellsGameState<HidokuGame, HidokuGameMove, HidokuGameState>(game) {
    var objArray: Array<HidokuObject>
    var nextNum = 0
    val num2pos = TreeMap<Int, Position>()
    var focusPos: Position? = null
    var hintPos: Position? = null

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: HidokuObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: HidokuObject) {this[p.row, p.col] = obj}

    init {
        objArray = Array (game.maxNum) { HidokuObject() }
        for (i in 0 until game.maxNum)
            objArray[i].obj = game.objArray[i]
        updateIsSolved()
        updateState()
    }

    override fun setObject(move: HidokuGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p].obj != HidokuGame.PUZ_UNKNOWN) return GameOperationType.Invalid
        this[p].obj = move.obj
        focusPos = p
        updateIsSolved()
        updateState()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: HidokuGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        when (this[p].obj) {
            HidokuGame.PUZ_UNKNOWN -> {
                move.obj = nextNum
                return setObject(move)
            }
            HidokuGame.PUZ_FORBIDDEN -> return GameOperationType.Invalid
            else -> {
                focusPos = p
                updateState()
                return GameOperationType.PartialMove
            }
        }
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 4/Hidoku

        Summary
        Jump from one neighboring tile to another and fill the board

        Description
        1. Starting at the tile number 1, reach the last tile by jumping from
           tile to tile.
        2. When jumping from a tile, you can land on any tile around it,
           horizontally, vertically or diagonally touching.
        3. The goal is to jump on every tile, only once and reach the last tile.
    */
    private fun updateIsSolved() {
        isSolved = true
        num2pos.clear()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = this[p].obj
                if (n == -1) // forbidden
                    this[p].obj = 0
                if (n != 0 && n != -1)
                    num2pos[n] = p
            }
        if (focusPos == null)
            focusPos = num2pos[1]!!
        for ((n, p) in num2pos) {
            if (n == game.maxNum) continue
            if (!num2pos.contains(n + 1)) {
                isSolved = false
                this[p].state = HintState.Normal
            } else {
                val p2 = num2pos[n + 1]!!
                val b = game.pos2range[p]!!.contains(p2)
                this[p].state = if (b) HintState.Complete else HintState.Error
                if (!b)
                    isSolved = false
                if (b && n + 1 == game.maxNum)
                    this[p2].state = HintState.Complete
            }
        }
    }

    private fun updateState() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        var currentNum = this[focusPos!!].obj
        var currentPos = focusPos!!
        hintPos = num2pos.firstNotNullOf { (k, v) -> if (k > currentNum) v else null }
        nextNum = 0
        for ((n, p) in num2pos) {
            if (n == game.maxNum) continue
            if (currentNum + 1 == n && nextNum == 0)
                currentNum = n
            if (!num2pos.contains(n + 1) && currentNum == n && nextNum == 0) {
                currentPos = p
                nextNum = n + 1
            }
        }
        if (!allowedObjectsOnly) return
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p].obj != 0) continue
                val b = game.pos2range[currentPos]!!.contains(p)
                if (!b) // forbidden
                    this[p].obj = -1
            }
    }
}
