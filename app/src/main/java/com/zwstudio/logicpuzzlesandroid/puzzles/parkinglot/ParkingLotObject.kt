package com.zwstudio.logicpuzzlesandroid.puzzles.parkinglot

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ParkingLotObject {
    Empty, Marker,
    Left, Right, Horizontal, Top, Bottom, Vertical;
    fun isCar() = this !in listOf(Empty, Marker)
}

class ParkingLotGameMove(val p: Position, var obj: ParkingLotObject = ParkingLotObject.Empty)
