package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fj.data.Array.array;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LineSweeperGameState extends CellsGameState<LineSweeperGame, LineSweeperGameMove, LineSweeperGameState> {
    public Boolean[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public LineSweeperGameState(LineSweeperGame game) {
        super(game);
        objArray = new Boolean[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new Boolean[4];
            Arrays.fill(objArray[i], false);
        }
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n = entry.getValue();
            pos2state.put(p, n == 0 ? HintState.Complete : HintState.Normal);
        }
    }

    public Boolean[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public Boolean[] get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, Boolean[] obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, Boolean[] obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(LineSweeperGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.isHint(p)) return false;
        int dir = move.dir;
        Position p2 = p.add(LineSweeperGame.offset[dir * 2]);
        if (!isValid(p2) || game.isHint(p2)) return false;
        int dir2 = (dir + 2) % 4;
        get(p)[dir] = !get(p)[dir];
        get(p2)[dir2] = !get(p2)[dir2];
        updateIsSolved();
        return true;
    }

    /*
        iOS Game: Logic Games/Puzzle Set 4/LineSweeper

        Summary
        Draw a single loop, minesweeper style

        Description
        1. Draw a single closed looping path that never crosses itself or branches off.
        2. A number in a cell denotes how many of the 8 adjacent cells are passed
           by the loop.
        3. The loop can only go horizontally or vertically between cells, but
           not over the numbers.
        4. The loop doesn't need to cover all the board.
    */
    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            for (Position os : LineSweeperGame.offset) {
                Position p2 = p.add(os);
                if (!isValid(p2)) continue;
                boolean hasLine = false;
                for (boolean b : get(p2))
                    if (b) hasLine = true;
                if (hasLine) n1++;
            }
            pos2state.put(p, n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error);
            if (n1 != n2) isSolved = false;
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = array(get(p)).filter(o -> o).length();
                switch (n) {
                    case 0:
                        continue;
                    case 2:
                    {
                        Node node = new Node(p.toString());
                        g.addNode(node);
                        pos2node.put(p, node);
                    }
                    break;
                    default:
                        isSolved = false;
                        return;
                }
            }

        for (Position p : pos2node.keySet()) {
            Boolean[] o = get(p);
            for (int i = 0; i < 4; i++) {
                if (!o[i]) continue;
                Position p2 = p.add(LineSweeperGame.offset[i * 2]);
                g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2node.values().size();
        if (n1 != n2) isSolved = false;
    }
}
