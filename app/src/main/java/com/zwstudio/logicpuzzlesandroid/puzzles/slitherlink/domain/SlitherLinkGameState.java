package com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.Array.array;
import static fj.data.List.iterableList;

public class SlitherLinkGameState extends CellsGameState<SlitherLinkGame, SlitherLinkGameMove, SlitherLinkGameState> {
    public GridLineObject[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public SlitherLinkGameState(SlitherLinkGame game) {
        super(game);
        objArray = new GridLineObject[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new GridLineObject[4];
            Arrays.fill(objArray[i], GridLineObject.Empty);
        }
        updateIsSolved();
    }

    public GridLineObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public GridLineObject[] get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, GridLineObject[] dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, GridLineObject[] obj) {
        set(p.row, p.col, obj);
    }

    private boolean isValidMove(SlitherLinkGameMove move) {
        return !(move.p.row == rows() - 1 && move.dir == 2 || move.p.col == cols() - 1 && move.dir == 1);
    }

    public boolean setObject(SlitherLinkGameMove move) {
        if (!isValidMove(move)) return false;
        Position p1 = move.p;
        int dir = move.dir, dir2 = (dir + 2) % 4;
        GridLineObject o = get(p1)[dir];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(SlitherLinkGame.offset[dir]);
        get(p2)[dir2] = get(p1)[dir] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(SlitherLinkGameMove move) {
        if (!isValidMove(move)) return false;
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
        iOS Game: Logic Games/Puzzle Set 3/SlitherLink

        Summary
        Draw a loop a-la-minesweeper!

        Description
        1. Draw a single looping path with the aid of the numbered hints. The path
           cannot have branches or cross itself.
        2. Each number in a tile tells you on how many of its four sides are touched
           by the path.
        3. For example:
        4. A 0 tells you that the path doesn't touch that square at all.
        5. A 1 tells you that the path touches that square ONLY one-side.
        6. A 3 tells you that the path does a U-turn around that square.
        7. There can't be tiles marked with 4 because that would form a single
           closed loop in it.
        8. Empty tiles can have any number of sides touched by that path.
    */
    private void updateIsSolved() {
        isSolved = true;
        // 2. Each number in a tile tells you on how many of its four sides are touched
        // by the path.
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            if (get(p)[1] == GridLineObject.Line) n1++;
            if (get(p)[2] == GridLineObject.Line) n1++;
            if (get(p.add(new Position(1, 1)))[0] == GridLineObject.Line) n1++;
            if (get(p.add(new Position(1, 1)))[3] == GridLineObject.Line) n1++;
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = array(get(p)).filter(o -> o == GridLineObject.Line).length();
                switch (n) {
                case 0:
                    continue;
                case 2:
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                    break;
                default:
                    // 1. The path cannot have branches or cross itself.
                    isSolved = false;
                    return;
                }
            }

        for (Position p : pos2node.keySet()) {
            GridLineObject[] dotObj = get(p);
            for (int i = 0; i < 4; i++) {
                if (dotObj[i] != GridLineObject.Line) continue;
                Position p2 = p.add(SlitherLinkGame.offset[i]);
                g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        }
        // 1. Draw a single looping path with the aid of the numbered hints.
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;
    }
}
