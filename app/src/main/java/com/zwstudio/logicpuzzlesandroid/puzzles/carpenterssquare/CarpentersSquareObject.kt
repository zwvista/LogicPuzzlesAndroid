package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class CarpenterSquareHint

class CarpentersSquareCornerHint(var tiles: Int = 0) : CarpenterSquareHint()

object CarpentersSquareDownHint : CarpenterSquareHint()

object CarpentersSquareLeftHint : CarpenterSquareHint()

object CarpentersSquareRightHint : CarpenterSquareHint()

object CarpentersSquareUpHint : CarpenterSquareHint()

class CarpentersSquareGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
