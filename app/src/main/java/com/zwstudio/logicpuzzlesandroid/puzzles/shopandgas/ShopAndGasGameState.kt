package com.zwstudio.logicpuzzlesandroid.puzzles.shopandgas

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.runinaloop.RunInALoopGame

class ShopAndGasGameState(game: ShopAndGasGame) : CellsGameState<ShopAndGasGame, ShopAndGasGameMove, ShopAndGasGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: ShopAndGasGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + ShopAndGasGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 10/Shop & Gas

        Summary
        A Hard day at shopping!

        Description
        1. In Shop & Gas you take the typical day at shopping. By the way:
           you just bought a new hyper-ecological car.
        2. This car goes on a ultra-green combustible, which is the saviour
           of the environment. It costs close to zero, it does not pollute
           and is found in abundance everywhere.
        3. The only small problem is that the car consumes about 10 liter
           per Km. Yes, that's a problem.
        4. So while shopping you have to constantly refuel your car. Thus,
           Shop & Gas rules are as follows:
        5. You start from your house. Right away, you're low on fuel so you
           must pass a fuel station.
        6. All these prototype fuel stations are shaped like corners. Don't
           ask why. You just have to turn on those tiles.
        7. Each time you pass a gas station, you then have to go shopping.
        8. Shopping malls are a lot more consumer friendly and have straight
           roads. So you have to go straight on those tiles.
        9. After a shopping mall you are almost empty again. The next thing
           you must pass is a gas station. Then shopping, gas, etc.
        10.After you passed all the shopping malls and gas station, you have
           to go back to your house, forming a closed path.
        11.The last thing you have to pass before going back home, is a gas
           station.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = game[p]
                val dirs = (0 until 4).filter { this[p][it] }
                if (dirs.size == 2) {
                    pos2dirs[p] = dirs
                    if (ch == ShopAndGasGame.PUZ_BLACK_PEARL) {
                        // 4. Lines passing through Black Pearls must do a 90 degree turn in them.
                        if (dirs[1] - dirs[0] == 2) { isSolved = false; return }
                    } else if (ch == ShopAndGasGame.PUZ_WHITE_PEARL) {
                        // 3. Lines passing through White Pearls must go straight through them.
                        if (dirs[1] - dirs[0] != 2) { isSolved = false; return }
                    }
                } else if (!(dirs.isEmpty() && ch == ' ')) {
                    // 1. The goal is to draw a single Loop(Necklace) through every circle(Pearl)
                    //    that never branches-off or crosses itself.
                    isSolved = false; return
                }
            }
        val pos2dirs2 = pos2dirs.toMap()
        // Check the loop
        val p = pos2dirs.keys.firstOrNull()
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += RunInALoopGame.offset[n]
            if (p2 == p) break
        }
        // 3. At least at one side of the White Pearl(or both), they must do a 90 degree turn.
        // 4. Lines passing through Black Pearls must go straight in the next tile in both directions.
        // 5. Lines passing where there are no Pearls can do what they want.
        if (!pos2dirs2.all { (p, dirs) ->
            when (val ch = game[p]) {
                ' ' -> true
                else -> {
                    val turns = dirs.reduce { acc, d ->
                        val dirs2 = pos2dirs[p + ShopAndGasGame.offset[d]]!!
                        acc + (if (dirs2[1] - dirs2[0] != 2) 1 else 0)
                    }
                    ch == ShopAndGasGame.PUZ_BLACK_PEARL && turns == 0 || ch == ShopAndGasGame.PUZ_WHITE_PEARL && turns > 0
                }
            }
        }) isSolved = false
    }
}