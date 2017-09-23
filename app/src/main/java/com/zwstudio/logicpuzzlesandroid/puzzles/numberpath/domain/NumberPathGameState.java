package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static fj.data.Array.array;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class NumberPathGameState extends CellsGameState<NumberPathGame, NumberPathGameMove, NumberPathGameState> {
    public Boolean[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public NumberPathGameState(NumberPathGame game) {
        super(game);
        objArray = new Boolean[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new Boolean[4];
            Arrays.fill(objArray[i], false);
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

    public boolean setObject(NumberPathGameMove move) {
        Position p = move.p;
        int dir = move.dir;
        Position p2 = p.add(NumberPathGame.offset[dir]);
        int dir2 = (dir + 2) % 4;
        if (!isValid(p2)) return false;
        get(p)[dir] = !get(p)[dir];
        get(p2)[dir2] = !get(p2)[dir2];
        updateIsSolved();
        return true;
    }

    /*
        iOS Game: Logic Games/Puzzle Set 15/Number Path

        Summary
        Tangled, Scrambled Path

        Description
        1. Connect the top left corner (1) to the bottom right corner (N), including
           all the numbers between 1 and N, only once.
    */
    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        Position pStart = new Position(0, 0), pEnd = new Position(rows() - 1, cols() - 1);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = array(get(p)).filter(o -> o).length();
                if (p.equals(pStart) || p.equals(pEnd)) {
                    if (n != 1) {isSolved = false; return;}
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                    continue;
                }
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
        Set<Integer> nums = new HashSet<>();
        for (Position p : pos2node.keySet()) {
            Boolean[] o = get(p);
            nums.add(game.get(p));
            for (int i = 0; i < 4; i++) {
                if (!o[i]) continue;
                Position p2 = p.add(NumberPathGame.offset[i]);
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
