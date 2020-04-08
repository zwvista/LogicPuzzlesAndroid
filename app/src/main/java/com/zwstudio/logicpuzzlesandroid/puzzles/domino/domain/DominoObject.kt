package com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DominoGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
