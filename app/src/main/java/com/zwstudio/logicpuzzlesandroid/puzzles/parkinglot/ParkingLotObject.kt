package com.zwstudio.logicpuzzlesandroid.puzzles.parkinglot

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ParkingLotObject {
    Empty, Forbidden, Marker, Wall
}

class ParkingLotGameMove(val p: Position, var obj: ParkingLotObject = ParkingLotObject.Empty)
