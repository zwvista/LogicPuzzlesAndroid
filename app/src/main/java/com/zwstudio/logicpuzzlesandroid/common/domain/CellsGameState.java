package com.zwstudio.logicpuzzlesandroid.common.domain;

/**
 * Created by TCC-2-9002 on 2016/10/27.
 */

public class CellsGameState<G extends CellsGame<G, GM, GS>, GM, GS extends GameState> extends GameState {
    public G game;

    public Position size() {return game.size;}
    public int rows() {return game.rows();}
    public int cols() {return game.cols();}
    public boolean isValid(int row, int col) { return game.isValid(row, col); }
    public boolean isValid(Position p) {
        return game.isValid(p);
    }

    public CellsGameState(G game) {
        this.game = game;
    }
}
