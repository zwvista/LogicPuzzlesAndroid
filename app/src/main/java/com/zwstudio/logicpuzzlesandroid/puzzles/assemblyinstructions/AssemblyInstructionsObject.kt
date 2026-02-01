package com.zwstudio.logicpuzzlesandroid.puzzles.assemblyinstructions

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class AssemblyInstructionsPart(val part: List<Position>, val hint: Position)

class AssemblyInstructionsGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
