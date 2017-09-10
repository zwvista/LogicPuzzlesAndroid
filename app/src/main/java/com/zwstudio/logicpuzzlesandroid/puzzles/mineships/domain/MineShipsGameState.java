package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.data.Array;

import static fj.data.Array.array;
import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class MineShipsGameState extends CellsGameState<MineShipsGame, MineShipsGameMove, MineShipsGameState> {
    public MineShipsObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public MineShipsGameState(MineShipsGame game) {
        super(game);
        objArray = new MineShipsObject[rows() * cols()];
        for (int i = 0; i < objArray.length; i++)
            objArray[i] = new MineShipsEmptyObject();
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            MineShipsHintObject o = new MineShipsHintObject();
            set(p, o);
        }
        updateIsSolved();
    }

    public MineShipsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public MineShipsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, MineShipsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, MineShipsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(MineShipsGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.pos2hint.containsKey(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(MineShipsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<MineShipsObject, MineShipsObject> f = obj -> {
            if(obj instanceof MineShipsEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new MineShipsMarkerObject() : new MineShipsBattleShipUnitObject();
            else if(obj instanceof MineShipsBattleShipUnitObject)
                return new MineShipsBattleShipMiddleObject();
            else if(obj instanceof MineShipsBattleShipMiddleObject)
                return new MineShipsBattleShipLeftObject();
            else if(obj instanceof MineShipsBattleShipLeftObject)
                return new MineShipsBattleShipTopObject();
            else if(obj instanceof MineShipsBattleShipTopObject)
                return new MineShipsBattleShipRightObject();
            else if(obj instanceof MineShipsBattleShipRightObject)
                return new MineShipsBattleShipBottomObject();
            else if(obj instanceof MineShipsBattleShipBottomObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new MineShipsMarkerObject() : new MineShipsEmptyObject();
            else if(obj instanceof MineShipsMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new MineShipsBattleShipUnitObject() : new MineShipsEmptyObject();
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 8/Mine Ships

        Summary
        Warning! Naval Mines in the water!

        Description
        1. There are actually no mines in the water, but this is a mix between
           Minesweeper and Battle Ships.
        2. You must find the same set of ships like 'Battle Ships'
           (1*4, 2*3, 3*2, 4*1).
        3. However this time the hints are given in the same form as 'Minesweeper',
           where a number tells you how many pieces of ship are around it.
        4. Usual Battle Ships rules apply!
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                if (get(r, c) instanceof MineShipsForbiddenObject)
                    set(r, c, new MineShipsEmptyObject());
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n1 = 0, n2 = entry.getValue();
            List<Position> rng = new ArrayList<>();
            for (Position os : MineShipsGame.offset) {
                Position p2 = p.add(os);
                if (!isValid(p2)) continue;
                MineShipsObject o = get(p2);
                if (o instanceof MineShipsBattleShipTopObject || o instanceof MineShipsBattleShipBottomObject ||
                        o instanceof MineShipsBattleShipLeftObject || o instanceof MineShipsBattleShipRightObject ||
                        o instanceof MineShipsBattleShipMiddleObject || o instanceof MineShipsBattleShipUnitObject)
                    n1++;
                else if(o instanceof  MineShipsEmptyObject)
                    rng.add(p2.plus());
            }
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
            else if(allowedObjectsOnly)
                for (Position p2 : rng)
                    set(p2, new MineShipsForbiddenObject());
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                MineShipsObject o = get(p);
                if (o instanceof MineShipsBattleShipTopObject || o instanceof MineShipsBattleShipBottomObject ||
                        o instanceof MineShipsBattleShipLeftObject || o instanceof MineShipsBattleShipRightObject ||
                        o instanceof MineShipsBattleShipMiddleObject || o instanceof MineShipsBattleShipUnitObject) {
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (int i = 0; i < 4; i++) {
                Position p2 = p.add(MineShipsGame.offset[i * 2]);
                if (pos2node.containsKey(p2))
                    g.connectNode(node, pos2node.get(p2));
            }
        }
        Integer[] shipNumbers = {0, 0, 0, 0, 0};
        Integer[] shipNumbers2 = {0, 4, 3, 2, 1};
        while (!pos2node.isEmpty()) {
            g.setRootNode(iterableList(pos2node.values()).head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            for (Position p : area)
                pos2node.remove(p);
            area.sort(Position::compareTo);
            if (!(area.size() == 1 && get(area.get(0)) instanceof MineShipsBattleShipUnitObject ||
                    area.size() > 1 && area.size() < 5 && ((
                    iterableList(area).forall(p -> p.row == area.get(0).row) &&
                    get(area.get(0)) instanceof MineShipsBattleShipLeftObject &&
                    get(area.get(area.size() - 1)) instanceof MineShipsBattleShipRightObject ||
                    iterableList(area).forall(p -> p.col == area.get(0).col) &&
                    get(area.get(0)) instanceof MineShipsBattleShipTopObject &&
                    get(area.get(area.size() - 1)) instanceof MineShipsBattleShipBottomObject) &&
                    Array.range(1, area.size() - 2).forall(i -> get(area.get(i)) instanceof MineShipsBattleShipMiddleObject)) &&
                    array(MineShipsGame.offset2).forall(os -> iterableList(area).forall(p -> {
                        Position p2 = p.add(os);
                        if (!isValid(p2)) return true;
                        MineShipsObject o = get(p2);
                        return o instanceof MineShipsEmptyObject || o instanceof MineShipsForbiddenObject || o instanceof MineShipsMarkerObject || o instanceof MineShipsHintObject;
                    })))) {isSolved = false; return;}
            shipNumbers[area.size()]++;
        }
        if (!Arrays.equals(shipNumbers, shipNumbers2)) isSolved = false;
    }
}
