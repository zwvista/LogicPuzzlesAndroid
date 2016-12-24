package com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fj.F;

/**
 * Created by zwvista on 2016/09/29.
 */

public class RoomsGameState extends CellsGameState<RoomsGame, RoomsGameMove, RoomsGameState> {
    public RoomsObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public RoomsGameState(RoomsGame game) {
        super(game);
        objArray = new RoomsObject[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new RoomsObject[4];
            Arrays.fill(objArray[i], RoomsObject.Empty);
        }
        updateIsSolved();
    }

    public RoomsObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public RoomsObject[] get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, RoomsObject[] dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, RoomsObject[] obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            for (int i = 0; i < 4; i++) {
                Position os1 = RoomsGame.offset[i], os2 = RoomsGame.offset2[i];
                int dir = RoomsGame.dirs[i];
                for (Position p2 = p.plus(); isValid(p2.add(os1)) && get(p2.add(os2))[dir] != RoomsObject.Line; p2.addBy(os1))
                    n1++;
            }
            pos2state.put(p, n1 > n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
    }

    public boolean setObject(RoomsGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        RoomsObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(RoomsGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(RoomsGameMove move, MarkerOptions markerOption) {
        F<RoomsObject, RoomsObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        RoomsObject.Marker : RoomsObject.Line;
            case Line:
                return markerOption == MarkerOptions.MarkerLast ?
                        RoomsObject.Marker : RoomsObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        RoomsObject.Line : RoomsObject.Empty;
            }
            return obj;
        };
        RoomsObject[] dotObj = get(move.p);
        move.obj = f.f(dotObj[move.dir]);
        return setObject(move);
    }
}
