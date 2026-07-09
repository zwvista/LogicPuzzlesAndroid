package com.zwstudio.logicpuzzlesandroid.puzzles.proofofquilt

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ProofOfQuiltObject {
    Empty, Filled, Forbidden, Marker,
    TriangleA, TriangleB, TriangleC, TriangleD;

    val isBlank get() =
        listOf(Empty, Forbidden, Marker).contains(this)
    val isTriangle get() =
        listOf(TriangleA, TriangleB, TriangleC, TriangleD).contains(this)

}

class ProofOfQuiltGameMove(val p: Position, var obj: ProofOfQuiltObject = ProofOfQuiltObject.Empty)
