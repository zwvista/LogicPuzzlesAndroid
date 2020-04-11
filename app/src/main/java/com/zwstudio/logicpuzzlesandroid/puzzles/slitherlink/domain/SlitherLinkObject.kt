package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SlitherLinkGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
