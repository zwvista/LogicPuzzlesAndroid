package com.zwstudio.logicpuzzlesandroid.common.domain;

import java.util.Arrays;

public class GridDots {
    public int rows = 0;
    public int cols = 0;
    private GridLineObject[][] objArray;

    public GridDots(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        objArray = new GridLineObject[rows * cols][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new GridLineObject[4];
            Arrays.fill(objArray[i], GridLineObject.Empty);
        }
    }
    public GridLineObject get(int row, int col, int dir) {
        return objArray[row * cols + col][dir];
    }
    public GridLineObject get(Position p, int dir) {
        return get(p.row, p.col, dir);
    }
    public void set(int row, int col, int dir, GridLineObject obj) {
        objArray[row * cols + col][dir] = obj;
    }
    public void set(Position p, int dir, GridLineObject obj) {
        set(p.row, p.col, dir, obj);
    }
}
