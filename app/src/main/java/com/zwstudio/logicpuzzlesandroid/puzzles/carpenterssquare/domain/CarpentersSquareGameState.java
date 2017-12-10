package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain;

import com.rits.cloning.Cloner;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;
import fj.F2;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class CarpentersSquareGameState extends CellsGameState<CarpentersSquareGame, CarpentersSquareGameMove, CarpentersSquareGameState> {
    public GridLineObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public CarpentersSquareGameState(CarpentersSquareGame game) {
        super(game);
        objArray = new Cloner().deepClone(game.objArray);
        updateIsSolved();
    }

    public GridLineObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public GridLineObject[] get(Position p) {
        return get(p.row, p.col);
    }

    public boolean setObject(CarpentersSquareGameMove move) {
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        if (game.get(p1)[dir] != GridLineObject.Empty) return false;
        GridLineObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(CarpentersSquareGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(CarpentersSquareGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<GridLineObject, GridLineObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        GridLineObject.Marker : GridLineObject.Line;
            case Line:
                return markerOption == MarkerOptions.MarkerLast ?
                        GridLineObject.Marker : GridLineObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        GridLineObject.Line : GridLineObject.Empty;
            }
            return obj;
        };
        GridLineObject[] dotObj = get(move.p);
        move.obj = f.f(dotObj[move.dir]);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Carpenter's Square

        Summary
        Angled Borders

        Description
        1. Similar to Carpenter's Wall, this time you have to respect the same
           rules, but instead of forming a Nurikabe, you just have to divide the
           board into many.Capenter's Squares (L shaped tools) of different size.
        2. The circled numbers on the board indicate the corner of the L.
        3. When a number is inside the circle, that indicates the total number of
           squares occupied by the L.
        4. The arrow always sits at the end of an arm and points to the corner of
           an L.
        5. All the tiles in the board have to be part of a Carpenter's Square.
    */
    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
            }
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                for (int i = 0; i < 4; i++)
                    if (get(p.add(CarpentersSquareGame.offset2[i]))[CarpentersSquareGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node.get(p), pos2node.get(p.add(CarpentersSquareGame.offset[i])));
            }
        while (!pos2node.isEmpty()) {
            g.setRootNode(iterableList(pos2node.values()).head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            for (Position p : area)
                pos2node.remove(p);
            List<Position> rngHint = iterableList(area).filter(p -> game.pos2hint.containsKey(p)).toJavaList();
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
            // 1. You just have to divide the board into many.Capenter's Squares (L shaped tools) of different size.
            int squareType =
                    f.f(cntR1, cntC1) ? 0 : // ┌
                    f.f(cntR1, cntC2) ? 1 : // ┐
                    f.f(cntR2, cntC1) ? 2 : // └
                    f.f(cntR2, cntC2) ? 3 : -1; // ┘
            // 5. All the tiles in the board have to be part of a Carpenter's Square.
            if (squareType == -1) isSolved = false;
            for (Position p : rngHint) {
                CarpenterSquareHint o = game.pos2hint.get(p);
                if (o instanceof CarpentersSquareCornerHint) {
                    // 2. The circled numbers on the board indicate the corner of the L.
                    // 3. When a number is inside the circle, that indicates the total number of
                    // squares occupied by the L.
                    int n2 = ((CarpentersSquareCornerHint) o).tiles;
                    HintState s = squareType == -1 ? HintState.Normal : !(n1 == n2 || n2 == 0) ? HintState.Error :
                            squareType == 0 && p.equals(new Position(r1, c1)) ||
                            squareType == 1 && p.equals(new Position(r1, c2)) ||
                            squareType == 2 && p.equals(new Position(r2, c1)) ||
                            squareType == 3 && p.equals(new Position(r2, c2)) ? HintState.Complete : HintState.Error;
                    pos2state.put(p, s);
                    if (s != HintState.Complete) isSolved = false;
                } else if (o instanceof CarpentersSquareLeftHint) {
                    // 4. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    HintState s = squareType == -1 ? HintState.Normal :
                            squareType == 0 && p.equals(new Position(r1, c2)) ||
                            squareType == 2 && p.equals(new Position(r2, c2)) ? HintState.Complete : HintState.Error;
                    pos2state.put(p, s);
                    if (s != HintState.Complete) isSolved = false;
                } else if (o instanceof CarpentersSquareUpHint) {
                    // 4. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    HintState s = squareType == -1 ? HintState.Normal :
                            squareType == 0 && p.equals(new Position(r2, c1)) ||
                            squareType == 1 && p.equals(new Position(r2, c2)) ? HintState.Complete : HintState.Error;
                    pos2state.put(p, s);
                    if (s != HintState.Complete) isSolved = false;
                } else if (o instanceof CarpentersSquareRightHint) {
                    // 4. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    HintState s = squareType == -1 ? HintState.Normal :
                            squareType == 1 && p.equals(new Position(r1, c1)) ||
                            squareType == 3 && p.equals(new Position(r2, c1)) ? HintState.Complete : HintState.Error;
                    pos2state.put(p, s);
                    if (s != HintState.Complete) isSolved = false;
                } else if (o instanceof CarpentersSquareDownHint) {
                    // 4. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    HintState s = squareType == -1 ? HintState.Normal :
                            squareType == 2 && p.equals(new Position(r1, c1)) ||
                            squareType == 3 && p.equals(new Position(r1, c2)) ? HintState.Complete : HintState.Error;
                    pos2state.put(p, s);
                    if (s != HintState.Complete) isSolved = false;
                }
            }
        }
    }
}
