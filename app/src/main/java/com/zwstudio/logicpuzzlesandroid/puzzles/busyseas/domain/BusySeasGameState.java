package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F0;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BusySeasGameState extends CellsGameState<BusySeasGame, BusySeasGameMove, BusySeasGameState> {
    public BusySeasObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public BusySeasGameState(BusySeasGame game) {
        super(game);
        objArray = new BusySeasObject[rows() * cols()];
        Arrays.fill(objArray, new BusySeasEmptyObject());
        for (Position p : game.pos2hint.keySet())
            set(p, new BusySeasHintObject());
        updateIsSolved();
    }

    public BusySeasObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public BusySeasObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, BusySeasObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, BusySeasObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(BusySeasGameMove move) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(BusySeasGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<BusySeasObject, BusySeasObject> f = obj -> {
            if (obj instanceof BusySeasEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new BusySeasMarkerObject() : new BusySeasLighthouseObject();
            if (obj instanceof BusySeasLighthouseObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new BusySeasMarkerObject() : new BusySeasEmptyObject();
            if (obj instanceof BusySeasMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new BusySeasLighthouseObject() : new BusySeasEmptyObject();
            return obj;
        };
        BusySeasObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                BusySeasObject o = get(r, c);
                if (o instanceof BusySeasLighthouseObject)
                    ((BusySeasLighthouseObject) o).state = AllowedObjectState.Normal;
                else if (o instanceof BusySeasForbiddenObject)
                    set(r, c, new BusySeasEmptyObject());
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasLightedBoat = () -> {
                    for (Position os : BusySeasGame.offset)
                        for (Position p2 = p.add(os); isValid(p2); p2.addBy(os))
                            if (isValid(p2) && get(p2) instanceof BusySeasHintObject)
                                return true;
                    return false;
                };
                BusySeasObject o = get(r, c);
                if (o instanceof BusySeasLighthouseObject) {
                    BusySeasLighthouseObject o2 = (BusySeasLighthouseObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && hasLightedBoat.f() ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                }
            }
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int[] nums = {0, 0, 0, 0};
            List<Position> rng = new ArrayList<>();
            next: for (int i = 0; i < 4; i++) {
                Position os = BusySeasGame.offset[i];
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    BusySeasObject o2 = get(p2);
                    if (o2 instanceof BusySeasHintObject) continue next;
                    if (o2 instanceof BusySeasEmptyObject)
                        rng.add(p2.plus());
                    else if (o2 instanceof BusySeasLighthouseObject)
                        nums[i]++;
                }
            }
            int n1 = nums[0] + nums[1] + nums[2] + nums[3];
            HintState s = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            pos2state.put(p, s);
            if (s != HintState.Complete)
                isSolved = false;
            else
                for (Position p2 : rng)
                    set(p2, new BusySeasForbiddenObject());
        }
    }
}
