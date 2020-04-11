package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class CarpenterSquareHint

class CarpentersSquareCornerHint(var tiles: Int = 0) : CarpenterSquareHint()

class CarpentersSquareDownHint : CarpenterSquareHint()

class CarpentersSquareLeftHint : CarpenterSquareHint()

class CarpentersSquareRightHint : CarpenterSquareHint()

class CarpentersSquareUpHint : CarpenterSquareHint()

class CarpentersSquareGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
