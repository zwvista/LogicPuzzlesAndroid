package com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class MosaikObject {
    Empty, Filled, Marker, Forbidden
}

class MosaikGameMove(val p: Position, var obj: MosaikObject = MosaikObject.Empty)
