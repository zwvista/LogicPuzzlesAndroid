package com.zwstudio.logicpuzzlesandroid.puzzles.parkinglot

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ParkingLotGameState(game: ParkingLotGame) : CellsGameState<ParkingLotGame, ParkingLotGameMove, ParkingLotGameState>(game) {
    // https://stackoverflow.com/questions/43172947/kotlin-creating-a-mutable-list-with-repeating-elements
    var objArray = Array(rows * cols) { ParkingLotObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ParkingLotObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: ParkingLotObject) {this[p.row, p.col] = obj}

    override fun setObject(move: ParkingLotGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ParkingLotGameMove): GameOperationType {
        val p = move.p
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (this[p]) {
            ParkingLotObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) ParkingLotObject.Marker else ParkingLotObject.Left
            ParkingLotObject.Left -> ParkingLotObject.Right
            ParkingLotObject.Right -> ParkingLotObject.Horizontal
            ParkingLotObject.Horizontal -> ParkingLotObject.Top
            ParkingLotObject.Top -> ParkingLotObject.Bottom
            ParkingLotObject.Bottom -> ParkingLotObject.Vertical
            ParkingLotObject.Vertical -> if (markerOption == MarkerOptions.MarkerLast) ParkingLotObject.Marker else ParkingLotObject.Empty
            ParkingLotObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) ParkingLotObject.Left else ParkingLotObject.Empty
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 11/Parking Lot

        Summary
        BEEEEP BEEEEEEP !!!

        Description
        1. The board represents a parking lot seen from above.
        2. Each number identifies a car and all cars are identified by a number,
           there are no hidden cars.
        3. Cars can be regular sports cars (2*1 tiles) or limousines (3*1 tiles)
           and can be oriented horizontally or vertically.
        4. The number in itself specifies how far the car can move forward or
           backward, in tiles.
        5. For example, a car that has one tile free in front and one tile free
           in the back, would be marked with a '2'.
        6. Find all the cars !!
    */
    private fun updateIsSolved() {
        isSolved = true
        val cars = mutableListOf<List<Position>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val n = ParkingLotGame.car_offset.indices.firstOrNull { i ->
                    val offset = ParkingLotGame.car_offset[i]
                    val obj = ParkingLotGame.car_objects[i]
                    offset.indices.all {
                        val p2 = p + offset[it]
                        isValid(p2) && this[p2] == obj[it]
                    }
                } ?: continue
                val car = ParkingLotGame.car_offset[n].map { p + it }
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
