package com.zwstudio.logicpuzzlesandroid.puzzles.hidoku

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class HidokuObject(var obj: Int = 0, var state: HintState = HintState.Normal)

class HidokuGameMove(val p: Position)
