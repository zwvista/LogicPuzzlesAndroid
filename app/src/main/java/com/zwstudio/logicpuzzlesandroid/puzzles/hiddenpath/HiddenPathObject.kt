package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenpath

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class HiddenPathObject(var obj: Int = 0, var state: HintState = HintState.Normal)

class HiddenPathGameMove(val p: Position, var obj: Int = 0)
