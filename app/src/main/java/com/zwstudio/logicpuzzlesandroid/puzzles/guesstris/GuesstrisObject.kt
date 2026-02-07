package com.zwstudio.logicpuzzlesandroid.puzzles.guesstris

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GuesstrisGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
