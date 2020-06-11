package com.zwstudio.logicpuzzlesandroid.common.domain

import java.util.*

class GridDots(rows: Int, cols: Int) {
    var rows = 0
    var cols = 0
    var objArray: Array<Array<GridLineObject?>>
    operator fun get(row: Int, col: Int, dir: Int): GridLineObject? {
        return objArray[row * cols + col][dir]
    }

    operator fun get(p: Position, dir: Int): GridLineObject? {
        return get(p.row, p.col, dir)
    }

    operator fun set(row: Int, col: Int, dir: Int, obj: GridLineObject?) {
        objArray[row * cols + col][dir] = obj
    }

    operator fun set(p: Position, dir: Int, obj: GridLineObject?) {
        set(p.row, p.col, dir, obj)
    }

    init {
        this.rows = rows
        this.cols = cols
        objArray = arrayOfNulls(rows * cols)
        for (i in objArray.indices) {
            objArray[i] = arrayOfNulls(4)
            Arrays.fill(objArray[i], GridLineObject.Empty)
        }
    }
}