package com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain;

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

import static fj.data.List.iterableList;

public class SnakeGameState extends CellsGameState<SnakeGame, SnakeGameMove, SnakeGameState> {
    public SnakeObject[] objArray;
    public HintState[] row2state;
    public HintState[] col2state;

    public SnakeGameState(SnakeGame game) {
        super(game);
        objArray = new SnakeObject[rows() * cols()];
        Arrays.fill(objArray, SnakeObject.Empty);
        for (Position p : game.pos2snake)
            set(p, SnakeObject.Snake);
        row2state = new HintState[rows()];
        col2state = new HintState[cols()];
        updateIsSolved();
    }

    public SnakeObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public SnakeObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, SnakeObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, SnakeObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(SnakeGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.pos2snake.contains(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(SnakeGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<SnakeObject, SnakeObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        SnakeObject.Marker : SnakeObject.Snake;
            case Snake:
                return markerOption == MarkerOptions.MarkerLast ?
                        SnakeObject.Marker : SnakeObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        SnakeObject.Snake : SnakeObject.Empty;
            }
            return obj;
        };
        Position p = move.p;
        if (!isValid(p) || game.pos2snake.contains(p))  return false;
        move.obj = f.f(get(p));
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Snake

        Summary
        Still lives inside your pocket-sized computer

        Description
        1. Complete the Snake, head to tail, inside the board.
        2. The two tiles given at the start are the head and the tail of the snake
           (it is irrelevant which is which).
        3. Numbers on the border tell you how many tiles the snake occupies in that
           row or column.
        4. The snake can't touch itself, not even diagonally.
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == SnakeObject.Forbidden)
                    set(r, c, SnakeObject.Empty);
        // 3. Numbers on the border tell you how many tiles the snake occupies in that row.
        for (int r = 0; r < rows(); r++) {
            int n1 = 0, n2 = game.row2hint[r];
            for (int c = 0; c < cols(); c++)
                if (get(r, c) == SnakeObject.Snake)
                    n1++;
            HintState s = n1 < n2 ? HintState.Normal : n1 == n2 || n2 == -1 ? HintState.Complete : HintState.Error;
            row2state[r] = s;
            if (s != HintState.Complete) isSolved = false;
        }
        // 3. Numbers on the border tell you how many tiles the snake occupies in that column.
        for (int c = 0; c < cols(); c++) {
            int n1 = 0, n2 = game.col2hint[c];
            for (int r = 0; r < rows(); r++)
                if (get(r, c) == SnakeObject.Snake)
                    n1++;
            HintState s = n1 < n2 ? HintState.Normal : n1 == n2 || n2 == -1 ? HintState.Complete : HintState.Error;
            col2state[c] = s;
            if (s != HintState.Complete) isSolved = false;
        }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                SnakeObject o = get(r, c);
                if ((o == SnakeObject.Empty || o == SnakeObject.Marker) && allowedObjectsOnly && (
                        row2state[r] != HintState.Normal && game.row2hint[r] != -1 ||
                        col2state[c] != HintState.Normal && game.col2hint[c] != -1))
                    set(r, c, SnakeObject.Forbidden);
            }
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) != SnakeObject.Snake) continue;
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
            }
        for (Position p : pos2node.keySet())
            for (Position os : SnakeGame.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2node.values().size();
        if (n1 != n2) isSolved = false;
        for (Position p : pos2node.keySet()) {
            List<Position> rngEmpty = new ArrayList<>();
            List<Position> rngSnake = new ArrayList<>();
            for (Position os : SnakeGame.offset) {
                Position p2 = p.add(os);
                if (!isValid(p2)) continue;
                SnakeObject o = get(p2);
                if (o == SnakeObject.Empty || o == SnakeObject.Marker)
                    rngEmpty.add(p2);
                else if (o == SnakeObject.Snake)
                    rngSnake.add(p2);
            }
            // 2. The two tiles given at the start are the head and the tail of the snake.
            // 4. The snake can't touch itself, not even diagonally.
            boolean b = game.pos2snake.contains(p);
            int cnt = rngSnake.size();
            if (b && cnt >= 1 || !b && cnt >= 2) {
                for (Position p2 : rngEmpty)
                    if (allowedObjectsOnly)
                        set(p2, SnakeObject.Forbidden);
                if (b && cnt > 1 || !b && cnt > 2) isSolved = false;
            }
        }
    }
}
