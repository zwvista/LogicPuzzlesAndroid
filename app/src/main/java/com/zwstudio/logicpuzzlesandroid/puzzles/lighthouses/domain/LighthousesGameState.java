package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain;

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

public class LighthousesGameState extends CellsGameState<LighthousesGame, LighthousesGameMove, LighthousesGameState> {
    public LighthousesObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public LighthousesGameState(LighthousesGame game) {
        super(game);
        objArray = new LighthousesObject[rows() * cols()];
        Arrays.fill(objArray, new LighthousesEmptyObject());
        for (Position p : game.pos2hint.keySet())
            set(p, new LighthousesHintObject());
        updateIsSolved();
    }

    public LighthousesObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public LighthousesObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, LighthousesObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, LighthousesObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(LighthousesGameMove move) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(LighthousesGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<LighthousesObject, LighthousesObject> f = obj -> {
            if (obj instanceof LighthousesEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new LighthousesMarkerObject() : new LighthousesLighthouseObject();
            if (obj instanceof LighthousesLighthouseObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new LighthousesMarkerObject() : new LighthousesEmptyObject();
            if (obj instanceof LighthousesMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new LighthousesLighthouseObject() : new LighthousesEmptyObject();
            return obj;
        };
        LighthousesObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                LighthousesObject o = get(r, c);
                if (o instanceof LighthousesLighthouseObject)
                    ((LighthousesLighthouseObject) o).state = AllowedObjectState.Normal;
                else if (o instanceof LighthousesForbiddenObject)
                    set(r, c, new LighthousesEmptyObject());
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasNeighbor = () -> {
                    for (Position os : LighthousesGame.offset) {
                        Position p2 = p.add(os);
                        if (!isValid(p2)) continue;
                        LighthousesObject o2 = get(p2);
                        if (o2 instanceof LighthousesHintObject || o2 instanceof LighthousesLighthouseObject)
                            return true;
                    }
                    return false;
                };
                F0<Boolean> hasLightedBoat = () -> {
                    for (int i = 0; i < 4; i++) {
                        Position os = LighthousesGame.offset[i * 2];
                        for (Position p2 = p.add(os); isValid(p2); p2.addBy(os))
                            if (get(p2) instanceof LighthousesHintObject)
                                return true;
                    }
                    return false;
                };
                LighthousesObject o = get(r, c);
                if (o instanceof LighthousesLighthouseObject) {
                    LighthousesLighthouseObject o2 = (LighthousesLighthouseObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && !hasNeighbor.f() && hasLightedBoat.f() ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof LighthousesEmptyObject || o instanceof LighthousesMarkerObject) &&
                        allowedObjectsOnly && hasNeighbor.f())
                    set(r, c, new LighthousesForbiddenObject());
            }
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int[] nums = {0, 0, 0, 0};
            List<Position> rng = new ArrayList<>();
            next: for (int i = 0; i < 4; i++) {
                Position os = LighthousesGame.offset[i * 2];
                for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                    LighthousesObject o2 = get(p2);
                    if (o2 instanceof LighthousesEmptyObject)
                        rng.add(p2.plus());
                    else if (o2 instanceof LighthousesLighthouseObject)
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
                    set(p2, new LighthousesForbiddenObject());
        }
    }
}
