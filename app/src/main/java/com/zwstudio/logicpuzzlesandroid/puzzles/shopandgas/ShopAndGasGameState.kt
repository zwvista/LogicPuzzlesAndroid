package com.zwstudio.logicpuzzlesandroid.puzzles.shopandgas

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

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
                    if (ch == ShopAndGasGame.PUZ_GAS) {
                        // 6. All these prototype fuel stations are shaped like corners. Don't
                        //    ask why. You just have to turn on those tiles.
                        if (dirs[1] - dirs[0] == 2) { isSolved = false; return }
                    } else if (ch == ShopAndGasGame.PUZ_SHOP) {
                        // 8. Shopping malls are a lot more consumer friendly and have straight
                        //    roads. So you have to go straight on those tiles.
                        if (dirs[1] - dirs[0] != 2) { isSolved = false; return }
                    }
                } else if (!(dirs.isEmpty() && ch == ' ')) {
                    // 5. You start from your house.
                    // 10.After you passed all the shopping malls and gas station, you have
                    //    to go back to your house, forming a closed path.
                    isSolved = false; return
                }
            }
        // Check the loop
        val p = game.home
        if (!pos2dirs.contains(p)) { isSolved = false; return }
        var p2 = p
        var n = -1
        var ch = game[p]
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += ShopAndGasGame.offset[n]
            val ch2 = game[p2]
            if (ch2 != ' ') {
                // 5. You start from your house. Right away, you're low on fuel so you
                //    must pass a fuel station.
                // 7. Each time you pass a gas station, you then have to go shopping.
                // 9. After a shopping mall you are almost empty again. The next thing
                //    you must pass is a gas station. Then shopping, gas, etc.
                // 11.The last thing you have to pass before going back home, is a gas
                //    station.
                if ((ch == ShopAndGasGame.PUZ_HOME || ch == ShopAndGasGame.PUZ_SHOP) && ch2 == ShopAndGasGame.PUZ_GAS ||
                    ch == ShopAndGasGame.PUZ_GAS && (ch2 == ShopAndGasGame.PUZ_SHOP || ch2 == ShopAndGasGame.PUZ_HOME) )
                    ch = ch2
                else {
                    isSolved = false; return
                }
            }
            if (p2 == p) break
        }
    }
}