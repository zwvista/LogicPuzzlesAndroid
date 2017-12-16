package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F2;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

public class CarpentersWallGameState extends CellsGameState<CarpentersWallGame, CarpentersWallGameMove, CarpentersWallGameState> {
    public CarpentersWallObject[] objArray;

    public CarpentersWallGameState(CarpentersWallGame game) {
        super(game);
        objArray = new CarpentersWallObject[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
    }

    public CarpentersWallObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public CarpentersWallObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, CarpentersWallObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, CarpentersWallObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(CarpentersWallGameMove move) {
        Position p = move.p;
        CarpentersWallObject objOld = get(p);
        CarpentersWallObject objNew = move.obj;
        if (objOld.isHint() || objOld.toString().equals(objNew.toString())) return false;
        set(p, objNew);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(CarpentersWallGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<CarpentersWallObject, CarpentersWallObject> f = obj -> {
            if (obj instanceof CarpentersWallEmptyObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new CarpentersWallMarkerObject() : new CarpentersWallWallObject();
            if (obj instanceof CarpentersWallWallObject)
                return markerOption == MarkerOptions.MarkerLast ?
                        new CarpentersWallMarkerObject() : new CarpentersWallEmptyObject();
            if (obj instanceof CarpentersWallMarkerObject)
                return markerOption == MarkerOptions.MarkerFirst ?
                        new CarpentersWallWallObject() : new CarpentersWallEmptyObject();
            return obj;
        };
        CarpentersWallObject objOld = get(move.p);
        if (objOld.isHint()) return false;
        move.obj = f.f(objOld);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 12/Carpenter's Wall

        Summary
        Angled Walls

        Description
        1. In this game you have to create a valid Nurikabe following a different
           type of hints.
        2. In the end, the empty spaces left by the Nurikabe will form many Carpenter's
           Squares (L shaped tools) of different size.
        3. The circled numbers on the board indicate the corner of the L.
        4. When a number is inside the circle, that indicates the total number of
           squares occupied by the L.
        5. The arrow always sits at the end of an arm and points to the corner of
           an L.
        6. Not all the Carpenter's Squares might be indicated: some could be hidden
           and no hint given.
    */
    private void updateIsSolved() {
        isSolved = true;
        // The wall can't form 2*2 squares.
        for (int r = 0; r < rows() - 1; r++)
            rule2x2: for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                for (Position os : CarpentersWallGame.offset2)
                    if (!(get(p.add(os)) instanceof CarpentersWallWallObject))
                        continue rule2x2;
                isSolved = false;
            }
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        List<Position> rngWalls = new ArrayList<>();
        List<Position> rngEmpty = new ArrayList<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
                CarpentersWallObject o = get(p);
                if (o instanceof CarpentersWallWallObject)
                    rngWalls.add(p);
                else
                    rngEmpty.add(p);
                if (o instanceof CarpentersWallCornerObject)
                    ((CarpentersWallCornerObject)o).state = HintState.Normal;
                else if (o instanceof CarpentersWallLeftObject)
                    ((CarpentersWallLeftObject)o).state = HintState.Normal;
                else if (o instanceof CarpentersWallRightObject)
                    ((CarpentersWallRightObject)o).state = HintState.Normal;
                else if (o instanceof CarpentersWallUpObject)
                    ((CarpentersWallUpObject)o).state = HintState.Normal;
                else if (o instanceof CarpentersWallDownObject)
                    ((CarpentersWallDownObject)o).state = HintState.Normal;
            }
        for (Position p : rngWalls)
            for (Position os : CarpentersWallGame.offset) {
                Position p2 = p.add(os);
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        for (Position p : rngEmpty)
            for (Position os : CarpentersWallGame.offset) {
                Position p2 = p.add(os);
                if (rngEmpty.contains(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        if (rngWalls.isEmpty())
            isSolved = false;
        else {
            // The garden is separated by a single continuous wall. This means all
            // wall tiles on the board must be connected horizontally or vertically.
            // There can't be isolated walls.
            g.setRootNode(pos2node.get(rngWalls.get(0)));
            List<Node> nodeList = g.bfs();
            if (rngWalls.size() != nodeList.size()) isSolved = false;
        }
        while (!rngEmpty.isEmpty()) {
            Node node = pos2node.get(rngEmpty.get(0));
            g.setRootNode(node);
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            rngEmpty = iterableList(rngEmpty).removeAll(p -> nodeList.contains(pos2node.get(p))).toJavaList();
            List<Position> rngHint = iterableList(area).filter(p -> get(p).isHint()).toJavaList();
            int n1 = nodeList.size();
            int r2 = 0, r1 = rows(), c2 = 0, c1 = cols();
            for (Position p : area) {
                if (r2 < p.row) r2 = p.row;
                if (r1 > p.row) r1 = p.row;
                if (c2 < p.col) c2 = p.col;
                if (c1 > p.col) c1 = p.col;
            }
            if (r1 == r2 || c1 == c2) {isSolved = false; continue;}
            int r12 = r1, r22 = r2, c12 = c1, c22 = c2;
            int cntR1 = iterableList(area).filter(p -> p.row == r12).length();
            int cntR2 = iterableList(area).filter(p -> p.row == r22).length();
            int cntC1 = iterableList(area).filter(p -> p.col == c12).length();
            int cntC2 = iterableList(area).filter(p -> p.col == c22).length();
            F2<Integer, Integer, Boolean> f = (a, b) -> a > 1 && b > 1 && a + b - 1 == n1;
            // 2. In the end, the empty spaces left by the Nurikabe will form many Carpenter's
            // Squares (L shaped tools) of different size.
            int squareType =
                    f.f(cntR1, cntC1) ? 0 : // ┌
                    f.f(cntR1, cntC2) ? 1 : // ┐
                    f.f(cntR2, cntC1) ? 2 : // └
                    f.f(cntR2, cntC2) ? 3 : -1; // ┘
            for (Position p : rngHint) {
                CarpentersWallObject o = get(p);
                if (o instanceof CarpentersWallCornerObject) {
                    // 3. The circled numbers on the board indicate the corner of the L.
                    // 4. When a number is inside the circle, that indicates the total number of
                    // squares occupied by the L.
                    CarpentersWallCornerObject o2 = (CarpentersWallCornerObject) o;
                    int n2 = o2.tiles;
                    HintState s = squareType == -1 ? HintState.Normal : !(n1 == n2 || n2 == 0) ? HintState.Error :
                            squareType == 0 && p.equals(new Position(r1, c1)) ||
                            squareType == 1 && p.equals(new Position(r1, c2)) ||
                            squareType == 2 && p.equals(new Position(r2, c1)) ||
                            squareType == 3 && p.equals(new Position(r2, c2)) ? HintState.Complete : HintState.Error;
                    o2.state = s;
                    if (s != HintState.Complete) isSolved = false;
                } else if (o instanceof CarpentersWallLeftObject) {
                    // 5. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    HintState s = squareType == -1 ? HintState.Normal :
                            squareType == 0 && p.equals(new Position(r1, c2)) ||
                            squareType == 2 && p.equals(new Position(r2, c2)) ? HintState.Complete : HintState.Error;
                    ((CarpentersWallLeftObject) o).state = s;
                    if (s != HintState.Complete) isSolved = false;
                } else if (o instanceof CarpentersWallUpObject) {
                    // 5. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    HintState s = squareType == -1 ? HintState.Normal :
                            squareType == 0 && p.equals(new Position(r2, c1)) ||
                            squareType == 1 && p.equals(new Position(r2, c2)) ? HintState.Complete : HintState.Error;
                    ((CarpentersWallUpObject) o).state = s;
                    if (s != HintState.Complete) isSolved = false;
                } else if (o instanceof CarpentersWallRightObject) {
                    // 5. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    HintState s = squareType == -1 ? HintState.Normal :
                            squareType == 1 && p.equals(new Position(r1, c1)) ||
                            squareType == 3 && p.equals(new Position(r2, c1)) ? HintState.Complete : HintState.Error;
                    ((CarpentersWallRightObject) o).state = s;
                    if (s != HintState.Complete) isSolved = false;
                } else if (o instanceof CarpentersWallDownObject) {
                    // 5. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    HintState s = squareType == -1 ? HintState.Normal :
                            squareType == 2 && p.equals(new Position(r1, c1)) ||
                            squareType == 3 && p.equals(new Position(r1, c2)) ? HintState.Complete : HintState.Error;
                    ((CarpentersWallDownObject) o).state = s;
                    if (s != HintState.Complete) isSolved = false;
                }
            }
        }
    }
}
