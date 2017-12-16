package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fj.F;
import fj.F0;

public class BootyIslandGameState extends CellsGameState<BootyIslandGame, BootyIslandGameMove, BootyIslandGameState> {
    public BootyIslandObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public BootyIslandGameState(BootyIslandGame game) {
        super(game);
        objArray = new BootyIslandObject[rows() * cols()];
        Arrays.fill(objArray, new BootyIslandEmptyObject());
        for (Position p : game.pos2hint.keySet())
            set(p, new BootyIslandHintObject());
        updateIsSolved();
    }

    public BootyIslandObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public BootyIslandObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, BootyIslandObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, BootyIslandObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(BootyIslandGameMove move) {
        if (get(move.p).equals(move.obj)) return false;
        set(move.p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(BootyIslandGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<BootyIslandObject, BootyIslandObject> f = obj -> {
            if (obj instanceof BootyIslandEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new BootyIslandMarkerObject() : new BootyIslandTreasureObject();
            if (obj instanceof BootyIslandTreasureObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new BootyIslandMarkerObject() : new BootyIslandEmptyObject();
            if (obj instanceof BootyIslandMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new BootyIslandTreasureObject() : new BootyIslandEmptyObject();
            return obj;
        };
        BootyIslandObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Booty Island

        Summary
        Overcrowded Piracy

        Description
        1. Overcrowded by Greedy Pirates (tm), this land has Treasures buried
           almost everywhere and the relative maps scattered around.
        2. In fact there's only one Treasure for each row and for each column.
        3. On the island you can see maps with a number: these tell you how
           many steps are required, horizontally or vertically, to reach a
           Treasure.
        4. For how stupid the Pirates are, they don't bury their Treasures
           touching each other, even diagonally, however at times they are so
           stupid that two or more maps point to the same Treasure!

        Bigger Islands
        5. On bigger islands, there will be two Treasures per row and column.
        6. In this case, the number on the map doesn't necessarily point to the
           closest Treasure on that row or column.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                BootyIslandObject o = get(r, c);
                if (o instanceof BootyIslandForbiddenObject)
                    set(r, c, new BootyIslandEmptyObject());
                else if (o instanceof BootyIslandTreasureObject)
                    ((BootyIslandTreasureObject) o).state = AllowedObjectState.Normal;
            }
        // 4. Pirates don't bury their Treasures touching each other, even diagonally.
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                F0<Boolean> hasNeighbor = () -> {
                    for (Position os : BootyIslandGame.offset) {
                        Position p2 = p.add(os);
                        if (isValid(p2) && get(p2) instanceof BootyIslandTreasureObject)
                            return true;
                    }
                    return false;
                };
                BootyIslandObject o = get(r, c);
                if (o instanceof BootyIslandTreasureObject) {
                    BootyIslandTreasureObject o2 = (BootyIslandTreasureObject)o;
                    AllowedObjectState s = o2.state == AllowedObjectState.Normal && !hasNeighbor.f() ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                    o2.state = s;
                    if (s == AllowedObjectState.Error) isSolved = false;
                } else if ((o instanceof BootyIslandEmptyObject || o instanceof BootyIslandMarkerObject) &&
                        allowedObjectsOnly && hasNeighbor.f())
                    set(r, c, new BootyIslandForbiddenObject());
            }
        // 2. In fact there's only one Treasure for each row.
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = 1;
            for (int c = 0; c < cols(); c++) {
                if (get(r, c) instanceof BootyIslandTreasureObject) n1++;
            }
            if (n1 != n2) isSolved = false;
            for (int c = 0; c < cols(); c++) {
                BootyIslandObject o = get(r, c);
                if (o instanceof BootyIslandTreasureObject) {
                    BootyIslandTreasureObject o2 = (BootyIslandTreasureObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof BootyIslandEmptyObject || o instanceof BootyIslandMarkerObject) &&
                        n1 == n2 && allowedObjectsOnly)
                    set(r, c, new BootyIslandForbiddenObject());
            }
        }
        // 2. In fact there's only one Treasure for each column.
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = 1;
            for (int r = 0; r < rows(); r++) {
                if (get(r, c) instanceof BootyIslandTreasureObject) n1++;
            }
            if (n1 != n2) isSolved = false;
            for (int r = 0; r < rows(); r++) {
                BootyIslandObject o = get(r, c);
                if (o instanceof BootyIslandTreasureObject) {
                    BootyIslandTreasureObject o2 = (BootyIslandTreasureObject)o;
                    o2.state = o2.state == AllowedObjectState.Normal && n1 <= n2 ?
                            AllowedObjectState.Normal : AllowedObjectState.Error;
                } else if ((o instanceof BootyIslandEmptyObject || o instanceof BootyIslandMarkerObject) &&
                        n1 == n2 && allowedObjectsOnly)
                    set(r, c, new BootyIslandForbiddenObject());
            }
        }
        // 3. On the island you can see maps with a number: these tell you how
        // many steps are required, horizontally or vertically, to reach a
        // Treasure.
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            F0<HintState> f = () -> {
                boolean possible = false;
                next: for (int i = 0; i < 4; i++) {
                    Position os = BootyIslandGame.offset[i * 2];
                    int n1 = 1;
                    boolean possible2 = false;
                    for (Position p2 = p.add(os); isValid(p2); p2.addBy(os)) {
                        BootyIslandObject o2 = get(p2);
                        if (o2 instanceof BootyIslandTreasureObject) {
                            if (n1 == n2) return HintState.Complete;
                            continue next;
                        }
                        if (o2 instanceof BootyIslandEmptyObject) {
                            if (n1 == n2) possible2 = true;
                        } else if (n1 == n2)
                            continue next;
                        n1++;
                    }
                    if (possible2) possible = true;
                }
                return possible ? HintState.Normal : HintState.Error;
            };
            HintState s = f.f();
            pos2state.put(p, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
