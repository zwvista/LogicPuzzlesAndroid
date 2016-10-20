package com.zwstudio.logicgamesandroid.bridges.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.util.Map;

import static fj.data.Array.arrayArray;
import static fj.function.Integers.add;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BridgesGameState implements Cloneable {
    public BridgesGame game;
    public BridgesObject[] objArray;
    public boolean isSolved;

    public Position size() {return game.size;}
    public int rows() {return game.rows();}
    public int cols() {return game.cols();}
    public boolean isValid(int row, int col) { return game.isValid(row, col); }
    public boolean isValid(Position p) {
        return game.isValid(p);
    }

    public BridgesGameState(BridgesGame game) {
        this.game = game;
        objArray = new BridgesObject[rows() * cols()];
        for (BridgesObject obj: objArray)
            obj = new BridgesEmptyObject();
    }

    @Override
    public BridgesGameState clone(){
        try {
            BridgesGameState o = (BridgesGameState)super.clone();
            o.game = game;
            o.objArray = new BridgesObject[objArray.length];
            for (int i = 0; i < objArray.length; i++)
                o.objArray[i] = objArray[i].clone();
            o.isSolved = isSolved;
            return o;
        } catch(CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    public BridgesObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public BridgesObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, BridgesObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, BridgesObject obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, BridgesIslandInfo> entry : game.islandsInfo.entrySet()) {
            Position p = entry.getKey();
            BridgesIslandInfo info = entry.getValue();
            BridgesIslandObject o = (BridgesIslandObject) get(p);
            int n1 = arrayArray(o.bridges).foldLeft(add, 0);
            int n2 = info.bridges;
            o.state = n1 < n2 ? LogicGamesHintState.Normal : n1 == n2 ? LogicGamesHintState.Complete : LogicGamesHintState.Error;
            if (n1 != n2) isSolved = false;
        }
    }

    public boolean switchBridges(BridgesGameMove move) {
        Position pFrom = move.pFrom, pTo = move.pTo;
        if (!(pFrom.compareTo(pTo) < 0 && (pFrom.row == pTo.row || pFrom.col == pTo.col))) return false;
        BridgesObject o11 = get(pFrom), o22 = get(pTo);
        if (!(o11 instanceof BridgesIslandObject && o22 instanceof BridgesIslandObject)) return false;
        BridgesIslandObject o1 = (BridgesIslandObject) o11, o2 = (BridgesIslandObject) o22;
        int n1 = pFrom.row == pTo.row ? 1 : 2;
        int n2 = (n1 + 2) % 4;
        Position os = BridgesGame.offset[n1];
        for (Position p = pFrom.add(os); !p.equals(pTo); p.addBy(os))
            switch (o1.bridges[n1]) {
            case 0:
                {
                    BridgesObject o = get(p);
                    if (!(o instanceof BridgesEmptyObject)) return false;
                    set(p, new BridgesBridgeObject());
                }
                break;
            case 1:
                set(p, new BridgesEmptyObject());
                break;
            }
        int n = (o1.bridges[n1] + 1) % 3;
        o1.bridges[n1] = o2.bridges[n2] = n;
        updateIsSolved();
        return true;
    }
}
