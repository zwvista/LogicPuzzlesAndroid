package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fj.data.Array.arrayArray;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LoopyGameState extends CellsGameState<LoopyGame, LoopyGameMove, LoopyGameState> {
    public Boolean[][] objArray;

    public LoopyGameState(LoopyGame game) {
        super(game);
        objArray = new Boolean[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new Boolean[4];
            Arrays.fill(objArray[i], false);
        }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++)
                for (int dir = 1; dir <= 2; dir++) {
                    if (!game.get(r, c)[dir]) continue;
                    LoopyGameMove move = new LoopyGameMove();
                    move.p = new Position(r, c);
                    move.dir = dir;
                    setObject(move);
                }
    }

    public Boolean[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public Boolean[] get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, Boolean[] dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, Boolean[] obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2Node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = arrayArray(get(p)).filter(o -> o).length();
                switch (n) {
                case 2:
                    {
                        Node node = new Node(p.toString());
                        g.addNode(node);
                        pos2Node.put(p, node);
                    }
                    break;
                default:
                    isSolved = false;
                    return;
                }
            }

        for (Position p : pos2Node.keySet()) {
            Boolean[] dotObj = get(p);
            for (int i = 0; i < 4; i++) {
                if (!dotObj[i]) continue;
                Position p2 = p.add(LoopyGame.offset[i]);
                g.connectNode(pos2Node.get(p), pos2Node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2Node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2Node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(LoopyGameMove move) {
        Position p = move.p;
        int dir = move.dir;
        if (!isValid(p) || game.get(p)[dir] && get(p)[dir]) return false;
        Position p2 = p.add(LoopyGame.offset[dir]);
        int dir2 = (dir + 2) % 4;
        get(p)[dir] = !get(p)[dir];
        get(p2)[dir2] = !get(p2)[dir2];
        updateIsSolved();
        return true;
    }
}