package com.zwstudio.logicpuzzlesandroid.common.domain

enum class AllowedObjectState {
    Normal, Error
}

enum class MarkerOptions {
    NoMarker, MarkerFirst, MarkerLast
}

enum class GridLineObject {
    Empty, Line, Marker, Forbidden
}

class GridDots(val rows: Int, val cols: Int) {
    var objArray = Array(rows * cols) { Array(4) { GridLineObject.Empty } }
    operator fun get(row: Int, col: Int, dir: Int) = objArray[row * cols + col][dir]
    operator fun get(p: Position, dir: Int) = this[p.row, p.col, dir]
    operator fun set(row: Int, col: Int, dir: Int, obj: GridLineObject) {objArray[row * cols + col][dir] = obj}
    operator fun set(p: Position, dir: Int, obj: GridLineObject) {this[p.row, p.col, dir] = obj}
}
