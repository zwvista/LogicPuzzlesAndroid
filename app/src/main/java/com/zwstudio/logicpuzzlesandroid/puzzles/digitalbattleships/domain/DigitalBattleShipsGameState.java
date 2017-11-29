package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain;

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

public class DigitalBattleShipsGameState extends CellsGameState<DigitalBattleShipsGame, DigitalBattleShipsGameMove, DigitalBattleShipsGameState> {
    public DigitalBattleShipsObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public DigitalBattleShipsGameState(DigitalBattleShipsGame game) {
        super(game);
        objArray = new DigitalBattleShipsObject[rows() * cols()];
        Arrays.fill(objArray, DigitalBattleShipsObject.Empty);
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved();
    }

    public DigitalBattleShipsObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public DigitalBattleShipsObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, DigitalBattleShipsObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, DigitalBattleShipsObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(DigitalBattleShipsGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(DigitalBattleShipsGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<DigitalBattleShipsObject, DigitalBattleShipsObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        DigitalBattleShipsObject.Marker : DigitalBattleShipsObject.BattleShipUnit;
            case BattleShipUnit:
                return DigitalBattleShipsObject.BattleShipMiddle;
            case BattleShipMiddle:
                return DigitalBattleShipsObject.BattleShipLeft;
            case BattleShipLeft:
                return DigitalBattleShipsObject.BattleShipTop;
            case BattleShipTop:
                return DigitalBattleShipsObject.BattleShipRight;
            case BattleShipRight:
                return DigitalBattleShipsObject.BattleShipBottom;
            case BattleShipBottom:
                return markerOption == MarkerOptions.MarkerLast ?
                        DigitalBattleShipsObject.Marker : DigitalBattleShipsObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        DigitalBattleShipsObject.BattleShipUnit : DigitalBattleShipsObject.Empty;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p)) return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Digital Battle Ships

        Summary
        Please divert your course 12+1+2 to avoid collision

        Description
        1. Play like Solo Battle Ships, with a difference.
        2. Each number on the outer board tells you the SUM of the ship or
           ship pieces you're seeing in that row or column.
        3. A ship or ship piece is worth the number it occupies on the board.
        4. Standard rules apply: a ship or piece of ship can't touch another,
           not even diagonally.
        5. In each puzzle there are
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
                if (get(r, c) == DigitalBattleShipsObject.Forbidden)
                    set(r, c, DigitalBattleShipsObject.Empty);
        // 2. Each number on the outer board tells you the SUM of the ship or
        // ship pieces you're seeing in that row.
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++) {
                DigitalBattleShipsObject o = get(r, c);
                if (o == DigitalBattleShipsObject.BattleShipTop || o == DigitalBattleShipsObject.BattleShipBottom ||
                        o == DigitalBattleShipsObject.BattleShipLeft || o == DigitalBattleShipsObject.BattleShipRight ||
                        o == DigitalBattleShipsObject.BattleShipMiddle || o == DigitalBattleShipsObject.BattleShipUnit)
                    // 3. A ship or ship piece is worth the number it occupies on the board.
                    n1 += game.get(r, c);
            }
            row2state[r] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        // 2. Each number on the outer board tells you the SUM of the ship or
        // ship pieces you're seeing in that column.
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++) {
                DigitalBattleShipsObject o = get(r, c);
                if (o == DigitalBattleShipsObject.BattleShipTop || o == DigitalBattleShipsObject.BattleShipBottom ||
                        o == DigitalBattleShipsObject.BattleShipLeft || o == DigitalBattleShipsObject.BattleShipRight ||
                        o == DigitalBattleShipsObject.BattleShipMiddle || o == DigitalBattleShipsObject.BattleShipUnit)
                    // 3. A ship or ship piece is worth the number it occupies on the board.
                    n1 += game.get(r, c);
            }
            col2state[c] = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            if (n1 != n2) isSolved = false;
        }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                DigitalBattleShipsObject o = get(r, c);
                if ((o == DigitalBattleShipsObject.Empty || o == DigitalBattleShipsObject.Marker) && allowedObjectsOnly && (
                        row2state[r] != HintState.Normal || col2state[c] != HintState.Normal))
                    set(r, c, DigitalBattleShipsObject.Forbidden);
            }
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                DigitalBattleShipsObject o = get(p);
                if (o == DigitalBattleShipsObject.BattleShipTop || o == DigitalBattleShipsObject.BattleShipBottom ||
                        o == DigitalBattleShipsObject.BattleShipLeft || o == DigitalBattleShipsObject.BattleShipRight ||
                        o == DigitalBattleShipsObject.BattleShipMiddle || o == DigitalBattleShipsObject.BattleShipUnit) {
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            for (int i = 0; i < 4; i++) {
                Position p2 = p.add(DigitalBattleShipsGame.offset[i * 2]);
                Node node2 = pos2node.get(p2);
                if (node2 != null) g.connectNode(node, node2);
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
            if (!(area.size() == 1 && get(area.get(0)) == DigitalBattleShipsObject.BattleShipUnit ||
                    area.size() > 1 && area.size() < 5 && (
                    iterableList(area).forall(p -> p.row == area.get(0).row) &&
                    get(area.get(0)) == DigitalBattleShipsObject.BattleShipLeft &&
                    get(area.get(area.size() - 1)) == DigitalBattleShipsObject.BattleShipRight ||
                    iterableList(area).forall(p -> p.col == area.get(0).col) &&
                    get(area.get(0)) == DigitalBattleShipsObject.BattleShipTop &&
                    get(area.get(area.size() - 1)) == DigitalBattleShipsObject.BattleShipBottom) &&
                    range(1, area.size() - 2).forall(i -> get(area.get(i)) == DigitalBattleShipsObject.BattleShipMiddle))) {
                isSolved = false; continue;
            }
            for (Position p : area)
                for (Position os : DigitalBattleShipsGame.offset) {
                    // 4. A ship or piece of ship can't touch another, not even diagonally.
                    Position p2 = p.add(os);
                    if (!isValid(p2) || area.contains(p2)) continue;
                    DigitalBattleShipsObject o = get(p2);
                    if (!(o == DigitalBattleShipsObject.Empty || o == DigitalBattleShipsObject.Forbidden || o == DigitalBattleShipsObject.Marker))
                        isSolved = false;
                    else if (allowedObjectsOnly)
                        set(p, DigitalBattleShipsObject.Forbidden);
                }
            shipNumbers[area.size()]++;
        }
        // 5. In each puzzle there are
        //    1 Aircraft Carrier (4 squares)
        //    2 Destroyers (3 squares)
        //    3 Submarines (2 squares)
        //    4 Patrol boats (1 square)
        if (!Arrays.equals(shipNumbers, shipNumbers2)) isSolved = false;
    }
}
