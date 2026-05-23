package com.zwstudio.logicpuzzlesandroid.puzzles.fussywaiter

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FussyWaiterGameState(game: FussyWaiterGame) : CellsGameState<FussyWaiterGame, FussyWaiterGameMove, FussyWaiterGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2stateFood = mutableMapOf<Position, AllowedObjectState>()
    val pos2stateDrink = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: FussyWaiterObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: FussyWaiterObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FussyWaiterGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || (if (move.isDrink) this[p].drink else this[p].food) == move.obj) return GameOperationType.Invalid
        if (move.isDrink) this[p].drink = move.obj else this[p].food = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FussyWaiterGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || (if (move.isDrink) game[p].drink else game[p].food) != ' ') return GameOperationType.Invalid
        val chMin = if (move.isDrink) 'A' else 'a'
        val chMax = chMin + rows
        val o = if (move.isDrink) this[p].drink else this[p].food
        move.obj = if (o == ' ') chMin else if (o == chMax) ' ' else o + 1
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 15/Fussy Waiter

        Summary
        Won't give you what you asked for

        Description
        1. This restaurant has a peculiar waiter. Priding himself on a math
           degree, he is very fussy about how you order.
        2. Respecting university nutrition balance, he only accepts unique
           pairings of food and drinks.
        3. Thus, a type of food can be ordered along with the same drink only
           on a single table.
        4. Moreover, touting sudoku nutrition, he also maintains that each row
           and column of tables must have each food and drinks represented
           exactly once.
        5. He is indeed, very fussy.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                pos2stateFood[p] = AllowedObjectState.Normal
                pos2stateDrink[p] = AllowedObjectState.Normal
            }
        fun f(arr: List<Pair<Position, Char>>, pos2state: MutableMap<Position, AllowedObjectState>) {
            var m = arr.groupBy { it.second }
            if (m.contains(' ')) isSolved = false
            m = m.filter { (ch, arr2) -> ch != ' ' && arr2.size > 1 }
            if (m.isNotEmpty()) {
                isSolved = false
                for (arr2 in m.values)
                    for ((p, _) in arr2)
                        pos2state[p] = AllowedObjectState.Error
            }
        }
        for (r in 0..<rows) {
            val foods = mutableListOf<Pair<Position, Char>>()
            val drinks = mutableListOf<Pair<Position, Char>>()
            for (c in 0..<cols) {
                val p = Position(r, c)
                foods.add(p to this[p].food)
                drinks.add(p to this[p].drink)
            }
            f(foods, pos2stateFood)
            f(drinks, pos2stateDrink)
        }
        for (c in 0..<cols) {
            val foods = mutableListOf<Pair<Position, Char>>()
            val drinks = mutableListOf<Pair<Position, Char>>()
            for (r in 0..<rows) {
                val p = Position(r, c)
                foods.add(p to this[p].food)
                drinks.add(p to this[p].drink)
            }
            f(foods, pos2stateFood)
            f(drinks, pos2stateDrink)
        }
    }
}