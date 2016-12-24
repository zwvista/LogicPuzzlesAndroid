package com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.Arrays;

/**
 * Created by zhaowei on 2016/12/23.
 */

public class ParksDots {
    public int rows = 0;
    public int cols = 0;
    private Boolean[][] objArray;

    public ParksDots(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        objArray = new Boolean[rows * cols][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new Boolean[4];
            Arrays.fill(objArray[i], false);
        }
    }
    public Boolean get(int row, int col, int dir) {
        return objArray[row * cols + col][dir];
    }
    public Boolean get(Position p, int dir) {
        return get(p.row, p.col, dir);
    }
    public void set(int row, int col, int dir, Boolean obj) {
        objArray[row * cols + col][dir] = obj;
    }
    public void set(Position p, int dir, Boolean obj) {
        set(p.row, p.col, dir, obj);
    }
}
