package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain;

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

public class PaintTheNurikabeGameState extends CellsGameState<PaintTheNurikabeGame, PaintTheNurikabeGameMove, PaintTheNurikabeGameState> {
    public PaintTheNurikabeObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public PaintTheNurikabeGameState(PaintTheNurikabeGame game) {
        super(game);
        objArray = new PaintTheNurikabeObject[rows() * cols()];
        Arrays.fill(objArray, PaintTheNurikabeObject.Empty);
        for (Position p : game.pos2hint.keySet())
            pos2state.put(p, HintState.Normal);
        updateIsSolved();
    }

    public PaintTheNurikabeObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public PaintTheNurikabeObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, PaintTheNurikabeObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, PaintTheNurikabeObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(PaintTheNurikabeGameMove move) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(PaintTheNurikabeGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<PaintTheNurikabeObject, PaintTheNurikabeObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        PaintTheNurikabeObject.Marker : PaintTheNurikabeObject.Filled;
            case Filled:
                return markerOption == MarkerOptions.MarkerLast ?
                        PaintTheNurikabeObject.Marker : PaintTheNurikabeObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        PaintTheNurikabeObject.Filled : PaintTheNurikabeObject.Empty;
            }
            return obj;
        };
        PaintTheNurikabeObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 6/Paint The Nurikabe

        Summary
        Paint areas, find Nurikabes

        Description
        1. By painting (filling) the areas you have to complete a Nurikabe.
           Specifically:
        2. A number indicates how many painted tiles are adjacent to it.
        3. The painted tiles form an orthogonally continuous area, like a
           Nurikabe.
        4. There can't be any 2*2 area of the same color(painted or empty).
    */
    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            for (Position os : PaintTheNurikabeGame.offset) {
                Position p2 = p.add(os);
                if (!isValid(p2)) continue;
                if (get(p2) == PaintTheNurikabeObject.Filled) n1++;
            }
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
    }
}
