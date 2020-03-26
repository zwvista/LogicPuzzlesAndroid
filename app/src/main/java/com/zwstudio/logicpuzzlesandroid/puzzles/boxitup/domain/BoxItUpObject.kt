package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BoxItUpGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
