package com.zwstudio.logicpuzzlesandroid.puzzles.wishsandwich

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WishSandwichGameState(game: WishSandwichGame) : CellsGameState<WishSandwichGame, WishSandwichGameMove, WishSandwichGameState>(game) {
    var objArray = Array<WishSandwichObject>(rows * cols) { WishSandwichEmptyObject }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: WishSandwichObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: WishSandwichObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: WishSandwichGameMove): GameOperationType {
        if (this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: WishSandwichGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[move.p]) {
            is WishSandwichEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) WishSandwichMarkerObject else WishSandwichPostObject()
            is WishSandwichPostObject -> if (markerOption == MarkerOptions.MarkerLast) WishSandwichMarkerObject else WishSandwichEmptyObject
            is WishSandwichMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) WishSandwichPostObject() else WishSandwichEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 3/Wish Sandwich

        Summary
        ...ever heard of it ?

        Description
        1. Each row and column contains two Slices of Bread and a number of Pieces of
           Ham, which is given in the top right corner.
        2. A number at the edge indicates how many Pieces of Ham you managed to put
           between the two Slices of Bread in that row or column.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val o = this[r, c]
                if (o is WishSandwichForbiddenObject)
                    this[r, c] = WishSandwichEmptyObject
                else if (o is WishSandwichPostObject)
                    o.state = AllowedObjectState.Normal
            }
        for (r in 0 until rows) {
            val posts = mutableListOf<Position>()
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] is WishSandwichPostObject)
                    posts.add(p)
            }
            val n1 = posts.size
            val n2 = game.row2hint[r] + 1
            // 2. There are two Posts in each Row.
            // 3. The numbers on the side tell you the length of the cables between
            // the two Posts (in that Row).
            val s = if (n1 < 2) HintState.Normal else if (n1 == 2 && n2 == posts[1].col - posts[0].col) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
            if (s == HintState.Error)
                for (p in posts)
                    (this[p] as WishSandwichPostObject).state = AllowedObjectState.Error
            if (allowedObjectsOnly && n1 > 0)
                for (c in 0 until cols)
                    if (this[r, c] is WishSandwichEmptyObject && (n1 > 1 || n1 == 1 && n2 != Math.abs(posts[0].col - c)))
                        this[r, c] = WishSandwichForbiddenObject
        }
        for (c in 0 until cols) {
            val posts = mutableListOf<Position>()
            for (r in 0 until rows) {
                val p = Position(r, c)
                if (this[p] is WishSandwichPostObject)
                    posts.add(p)
            }
            val n1 = posts.size
            val n2 = game.col2hint[c] + 1
            // 2. There are two Posts in each Column.
            // 3. The numbers on the side tell you the length of the cables between
            // the two Posts (in that Column).
            val s = if (n1 < 2) HintState.Normal else if (n1 == 2 && n2 == posts[1].row - posts[0].row) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
            if (s == HintState.Error)
                for (p in posts)
                    (this[p] as WishSandwichPostObject).state = AllowedObjectState.Error
            if (allowedObjectsOnly && n1 > 0)
                for (r in 0 until rows)
                    if (this[r, c] is WishSandwichEmptyObject && (n1 > 1 || n1 == 1 && n2 != Math.abs(posts[0].row - r)))
                        this[r, c] = WishSandwichForbiddenObject
        }
    }
}