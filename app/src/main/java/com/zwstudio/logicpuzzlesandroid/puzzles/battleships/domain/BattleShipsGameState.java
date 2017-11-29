package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;
import static fj.data.Stream.range;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BattleShipsGameState extends CellsGameState<BattleShipsGame, BattleShipsGameMove, BattleShipsGameState> {
    public BattleShipsObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public BattleShipsGameState(BattleShipsGame game) {
        super(game);
        objArray = new BattleShipsObject[rows() * cols()];
        Arrays.fill(objArray, BattleShipsObject.Empty);
        for (Map.Entry<Position, BattleShipsObject> entry : game.pos2obj.entrySet()) {
            Position p = entry.getKey();
            BattleShipsObject o = entry.getValue();
            set(p, o);
        }
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved();
    }

    public BattleShipsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public BattleShipsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, BattleShipsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, BattleShipsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(BattleShipsGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.pos2obj.containsKey(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(BattleShipsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<BattleShipsObject, BattleShipsObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        BattleShipsObject.Marker : BattleShipsObject.BattleShipUnit;
            case BattleShipUnit:
                return BattleShipsObject.BattleShipMiddle;
            case BattleShipMiddle:
                return BattleShipsObject.BattleShipLeft;
            case BattleShipLeft:
                return BattleShipsObject.BattleShipTop;
            case BattleShipTop:
                return BattleShipsObject.BattleShipRight;
            case BattleShipRight:
                return BattleShipsObject.BattleShipBottom;
            case BattleShipBottom:
                return markerOption == MarkerOptions.MarkerLast ?
                        BattleShipsObject.Marker : BattleShipsObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        BattleShipsObject.BattleShipUnit : BattleShipsObject.Empty;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Battle Ships

        Summary
        Play solo Battleships, with the help of the numbers on the border.

        Description
        1. Standard rules of Battleships apply, but you are guessing the other
           player ships disposition, by using the numbers on the borders.
        2. Each number tells you how many ship or ship pieces you're seeing in
           that row or column.
        3. Standard rules apply: a ship or piece of ship can't touch another,
           not even diagonally.
        4. In each puzzle there are
           1 Aircraft Carrier (4 squares)
           2 Destroyers (3 squares)
           3 Submarines (2 squares)
           4 Patrol boats (1 square)

        Variant
        5. Some puzzle can also have a:
           1 Supertanker (5 squares)
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == BattleShipsObject.Forbidden)
                    set(r, c, BattleShipsObject.Empty);
        // 2. Each number tells you how many ship or ship pieces you're seeing in that row.
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++) {
                BattleShipsObject o = get(r, c);
                if (o == BattleShipsObject.BattleShipTop || o == BattleShipsObject.BattleShipBottom ||
                        o == BattleShipsObject.BattleShipLeft || o == BattleShipsObject.BattleShipRight ||
                        o == BattleShipsObject.BattleShipMiddle || o == BattleShipsObject.BattleShipUnit)
                    n1++;
            }
            row2state[r] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        // 2. Each number tells you how many ship or ship pieces you're seeing in that column.
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++) {
                BattleShipsObject o = get(r, c);
                if (o == BattleShipsObject.BattleShipTop || o == BattleShipsObject.BattleShipBottom ||
                        o == BattleShipsObject.BattleShipLeft || o == BattleShipsObject.BattleShipRight ||
                        o == BattleShipsObject.BattleShipMiddle || o == BattleShipsObject.BattleShipUnit)
                    n1++;
            }
            col2state[c] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                BattleShipsObject o = get(r, c);
                if ((o == BattleShipsObject.Empty || o == BattleShipsObject.Marker) && allowedObjectsOnly && (
                        row2state[r] != HintState.Normal || col2state[c] != HintState.Normal))
                    set(r, c, BattleShipsObject.Forbidden);
            }
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                BattleShipsObject o = get(p);
                if (o == BattleShipsObject.BattleShipTop || o == BattleShipsObject.BattleShipBottom ||
                        o == BattleShipsObject.BattleShipLeft || o == BattleShipsObject.BattleShipRight ||
                        o == BattleShipsObject.BattleShipMiddle || o == BattleShipsObject.BattleShipUnit) {
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (int i = 0; i < 4; i++) {
                Position p2 = p.add(BattleShipsGame.offset[i * 2]);
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
            Collections.sort(area, Position::compareTo);
            if (!(area.size() == 1 && get(area.get(0)) == BattleShipsObject.BattleShipUnit ||
                    area.size() > 1 && area.size() < 5 && (
                    iterableList(area).forall(p -> p.row == area.get(0).row) &&
                    get(area.get(0)) == BattleShipsObject.BattleShipLeft &&
                    get(area.get(area.size() - 1)) == BattleShipsObject.BattleShipRight ||
                    iterableList(area).forall(p -> p.col == area.get(0).col) &&
                    get(area.get(0)) == BattleShipsObject.BattleShipTop &&
                    get(area.get(area.size() - 1)) == BattleShipsObject.BattleShipBottom) &&
                    range(1, area.size() - 2).forall(i -> get(area.get(i)) == BattleShipsObject.BattleShipMiddle))) {
                isSolved = false; continue;
            }
            for (Position p : area)
                for (Position os : BattleShipsGame.offset) {
                    // 3. A ship or piece of ship can't touch another, not even diagonally.
                    Position p2 = p.add(os);
                    if (!isValid(p2) || area.contains(p2)) continue;
                    BattleShipsObject o = get(p2);
                    if (!(o == BattleShipsObject.Empty || o == BattleShipsObject.Forbidden || o == BattleShipsObject.Marker))
                        isSolved = false;
                    else if (allowedObjectsOnly)
                        set(p, BattleShipsObject.Forbidden);
                }
            shipNumbers[area.size()]++;
        }
        // 4. In each puzzle there are
        //    1 Aircraft Carrier (4 squares)
        //    2 Destroyers (3 squares)
        //    3 Submarines (2 squares)
        //    4 Patrol boats (1 square)
        if (!Arrays.equals(shipNumbers, shipNumbers2)) isSolved = false;
    }
}
