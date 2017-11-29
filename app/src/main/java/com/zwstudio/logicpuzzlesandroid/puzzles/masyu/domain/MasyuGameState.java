package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class MasyuGameState extends CellsGameState<MasyuGame, MasyuGameMove, MasyuGameState> {
    public Boolean[][] objArray;

    public MasyuGameState(MasyuGame game) {
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
    public void set(int row, int col, Boolean[] dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, Boolean[] obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(MasyuGameMove move) {
        Position p = move.p;
        int dir = move.dir;
        Position p2 = p.add(MasyuGame.offset[dir]);
        int dir2 = (dir + 2) % 4;
        get(p)[dir] = !get(p)[dir];
        get(p2)[dir2] = !get(p2)[dir2];
        updateIsSolved();
        return true;
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/Masyu

        Summary
        Draw a Necklace that goes through every Pearl

        Description
        1. The goal is to draw a single Loop(Necklace) through every circle(Pearl)
           that never branches-off or crosses itself.
        2. The rules to pass Pearls are:
        3. Lines passing through White Pearls must go straight through them.
           However, at least at one side of the White Pearl(or both), they must
           do a 90 degree turn.
        4. Lines passing through Black Pearls must do a 90 degree turn in them.
           Then they must go straight in the next tile in both directions.
        5. Lines passing where there are no Pearls can do what they want.
    */
    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        Map<Position, List<Integer>> pos2Dirs = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                Boolean[] o = get(r, c);
                char ch = game.get(r, c);
                List<Integer> dirs = new ArrayList<>();
                for (int i = 0; i < 4; i++)
                    if (o[i])
                        dirs.add(i);
                switch (dirs.size()) {
                case 0:
                    // 1. The goal is to draw a single Loop(Necklace) through every circle(Pearl)
                    if (ch != ' ') {isSolved = false; return;}
                    break;
                case 2:
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                    pos2Dirs.put(p, dirs);
                    switch (ch) {
                    case 'B':
                        // 4. Lines passing through Black Pearls must do a 90 degree turn in them.
                        if (dirs.get(1) - dirs.get(0) == 2) {isSolved = false; return;}
                        break;
                    case 'W':
                        // 3. Lines passing through White Pearls must go straight through them.
                        if (dirs.get(1) - dirs.get(0) != 2) {isSolved = false; return;}
                        break;
                    }
                    break;
                default:
                    // 1. The goal is to draw a single Loop(Necklace)
                    // that never branches-off or crosses itself.
                    isSolved = false; return;
                }
            }

        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            List<Integer> dirs = pos2Dirs.get(p);
            char ch = game.get(p);
            boolean bW = ch != 'W';
            for (int i : dirs) {
                Position p2 = p.add(MasyuGame.offset[i]);
                Node node2 = pos2node.get(p2);
                if (node2 == null) {isSolved = false; return;}
                List<Integer> dirs2 = pos2Dirs.get(p2);
                switch (ch) {
                    case 'B':
                        // 4. Lines passing through Black Pearls must go straight
                        // in the next tile in both directions.
                        if (!((i == 0 || i == 2) && dirs2.get(0) == 0 && dirs2.get(1) == 2 ||
                                (i == 1 || i == 3) && dirs2.get(0) == 1 && dirs2.get(1) == 3))
                        {isSolved = false; return;}
                        break;
                    case 'W':
                        // 3. At least at one side of the White Pearl(or both),
                        // Lines passing through White Pearls must do a 90 degree turn.
                        int n1 = (i + 1) % 4, n2 = (i + 3) % 4;
                        if (dirs2.get(0) == n1 || dirs2.get(0) == n2 || dirs2.get(1) == n1 || dirs2.get(1) == n2)
                            bW = true;
                        if (dirs.get(1) - dirs.get(0) != 2) {isSolved = false; return;}
                        break;
                }
                g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
            if (!bW ) {isSolved = false; return;}
        }
        // 1. The goal is to draw a single Loop(Necklace).
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2node.values().size();
        if (n1 != n2) isSolved = false;
    }
}
