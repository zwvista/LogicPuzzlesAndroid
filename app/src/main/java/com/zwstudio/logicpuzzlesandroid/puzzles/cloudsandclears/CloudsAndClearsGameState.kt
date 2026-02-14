package com.zwstudio.logicpuzzlesandroid.puzzles.cloudsandclears

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CloudsAndClearsGameState(game: CloudsAndClearsGame) : CellsGameState<CloudsAndClearsGame, CloudsAndClearsGameMove, CloudsAndClearsGameState>(game) {
    // https://stackoverflow.com/questions/43172947/kotlin-creating-a-mutable-list-with-repeating-elements
    var objArray = Array(rows * cols) { CloudsAndClearsObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: CloudsAndClearsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: CloudsAndClearsObject) {this[p.row, p.col] = obj}

    override fun setObject(move: CloudsAndClearsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: CloudsAndClearsGameMove): GameOperationType {
        val p = move.p
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (this[p]) {
            CloudsAndClearsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) CloudsAndClearsObject.Marker else CloudsAndClearsObject.Left
            CloudsAndClearsObject.Left -> CloudsAndClearsObject.Right
            CloudsAndClearsObject.Right -> CloudsAndClearsObject.Horizontal
            CloudsAndClearsObject.Horizontal -> CloudsAndClearsObject.Top
            CloudsAndClearsObject.Top -> CloudsAndClearsObject.Bottom
            CloudsAndClearsObject.Bottom -> CloudsAndClearsObject.Vertical
            CloudsAndClearsObject.Vertical -> if (markerOption == MarkerOptions.MarkerLast) CloudsAndClearsObject.Marker else CloudsAndClearsObject.Empty
            CloudsAndClearsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) CloudsAndClearsObject.Left else CloudsAndClearsObject.Empty
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 3/Clouds and Clears

        Summary
        Holes in the sky

        Description
        1. Paint the clouds according to the numbers.
        2. Each cloud or empty Sky move contains a single number that is the extension of the region
           itself.
        3. On a region there can be other numbers. These will indicate how many empty (non-cloud) tiles
           around it (diagonal too) including itself.
    */
    private fun updateIsSolved() {
        isSolved = true
        val cars = mutableListOf<List<Position>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = CloudsAndClearsGame.car_offset.indices.firstOrNull { i ->
                    val offset = CloudsAndClearsGame.car_offset[i]
                    val obj = CloudsAndClearsGame.car_objects[i]
                    offset.indices.all {
                        val p2 = p + offset[it]
                        isValid(p2) && this[p2] == obj[it]
                    }
                } ?: continue
                val car = CloudsAndClearsGame.car_offset[n].map { p + it }
                cars.add(car)
            }
        for (car in cars) {
            val rng = car.filter { game.pos2hint[it] != null }
            if (rng.size != 1) {
                isSolved = false
                for (p in rng) pos2stateHint[p] = HintState.Error
                continue
            }
            val pHint = rng[0]
            val n2 = game.pos2hint[pHint]!!
            val isHorz = car[1] - car[0] == Position.East
            val deltaMin = if (isHorz) -car[0].col else -car[0].row
            val deltaMax = if (isHorz) cols - 1 - car.last().col else rows - 1 - car.last().row
            val deltas = (deltaMin..deltaMax).filter { d ->
                car.all {
                    val p2 = it + (if (isHorz) Position(0, d) else Position(d, 0))
                    car.contains(p2) || !this[p2].isCar()
                }
            }
            val n1 = deltas.last() - deltas[0]
            val s = if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[pHint] = s
            if (s == HintState.Error) isSolved = false
        }
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p].isCar()) {
                    val s = if (cars.any {
                        it.contains(p)
                    }) AllowedObjectState.Normal else AllowedObjectState.Error
                    pos2stateAllowed[p] = s
                    if (s == AllowedObjectState.Error) { isSolved = false }
                }
                if (game.pos2hint[p] != null && pos2stateHint[p] == null) {
                    isSolved = false
                    pos2stateHint[p] = HintState.Normal
                }
            }
    }
}
