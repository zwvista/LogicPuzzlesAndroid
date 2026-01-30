package com.zwstudio.logicpuzzlesandroid.puzzles.newcarpentersquare

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class CarpenterSquareHint

class NewCarpenterSquareCornerHint(var tiles: Int = 0) : CarpenterSquareHint()

object NewCarpenterSquareDownHint : CarpenterSquareHint()

object NewCarpenterSquareLeftHint : CarpenterSquareHint()

object NewCarpenterSquareRightHint : CarpenterSquareHint()

object NewCarpenterSquareUpHint : CarpenterSquareHint()

class NewCarpenterSquareGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
