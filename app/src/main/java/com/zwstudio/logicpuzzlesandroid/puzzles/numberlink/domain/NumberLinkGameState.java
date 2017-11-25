package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.Equal;
import fj.function.Effect2;

import static fj.data.Array.array;
import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;
import static fj.data.Stream.range;

/**
 * Created by zwvista on 2016/09/29.
 */

public class NumberLinkGameState extends CellsGameState<NumberLinkGame, NumberLinkGameMove, NumberLinkGameState> {
    public Boolean[][] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public NumberLinkGameState(NumberLinkGame game) {
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

    public boolean setObject(NumberLinkGameMove move) {
        Position p = move.p;
        int dir = move.dir;
        Position p2 = p.add(NumberLinkGame.offset[dir]);
        int dir2 = (dir + 2) % 4;
        if (!isValid(p2)) return false;
        get(p)[dir] = !get(p)[dir];
        get(p2)[dir2] = !get(p2)[dir2];
        updateIsSolved();
        return true;
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/NumberLink

        Summary
        Connect the same numbers without the crossing paths

        Description
        1. Connect the couples of equal numbers (i.e. 2 with 2, 3 with 3 etc)
           with a continuous line.
        2. The line can only go horizontally or vertically and can't cross
           itself or other lines.
        3. Lines must originate on a number and must end in the other equal
           number.
        4. At the end of the puzzle, you must have covered ALL the squares with
           lines and no line can cover a 2*2 area (like a 180 degree turn).
        5. In other words you can't turn right and immediately right again. The
           same happens on the left, obviously. Be careful not to miss this rule.

        Variant
        6. In some levels there will be a note that tells you don't need to cover
           all the squares.
        7. In some levels you will have more than a couple of the same number.
           In these cases, you must connect ALL the same numbers together.
    */
    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        Map<Position, List<Integer>> pos2indexes = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = array(get(p)).filter(o -> o).length();
                boolean b = game.pos2hint.get(p) != null;
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
                if (b && n == 1 || !b && n == 2)
                    pos2indexes.put(p, range(0, 4).filter(i -> get(p)[i]).toJavaList());
                else
                    isSolved = false;
            }
        for (Map.Entry<Position, Node> entry : pos2node.entrySet()) {
            Position p = entry.getKey();
            Node node = entry.getValue();
            pos2state.put(p, HintState.Normal);
            List<Integer> indexes = pos2indexes.get(p);
            if (indexes == null) continue;
            for (int i : indexes) {
                Position p2 = p.add(NumberLinkGame.offset[i]);
                Node node2 = pos2node.get(p2);
                g.connectNode(node, node2);
            }
            if (indexes.size() != 2) continue;
            int i1 = indexes.get(0), i2 = indexes.get(1);
            Effect2<Integer, Boolean> f = (i, isRight) -> {
                Position p2 = p.add(NumberLinkGame.offset[i]);
                List<Integer> indexes2 = pos2indexes.get(p2);
                if (indexes2 == null || indexes2.size() != 2) return;
                int i3 = (i + 2) % 4;
                indexes2.remove(Integer.valueOf(i1));
                int i4 = indexes2.get(0);
                if (isRight && (i3 + 3) % 4 == i4 || !isRight && (i3 + 1) % 4 == i4) {
                    pos2state.put(p, HintState.Error);
                    isSolved = false;
                }
            };
            if ((i1 + 3) % 4 == i2) f.f(i2, true);
            if ((i2 + 3) % 4 == i1) f.f(i1, true);
            if ((i1 + 1) % 4 == i2) f.f(i2, false);
            if ((i2 + 1) % 4 == i1) f.f(i1, false);
        }
        while (!pos2node.isEmpty()) {
            g.setRootNode(fromMap(pos2node).values().head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            for (Position p : area)
                pos2node.remove(p);
            List<Position> rng1 = iterableList(area).filter(p -> game.pos2hint.get(p) != null).toJavaList();
            if (rng1.isEmpty()) {isSolved = false; continue;}
            List<Position> rng2 = game.pos2rng.get(game.pos2hint.get(rng1.get(0)));
            boolean b1 = iterableList(rng1).minus(Equal.anyEqual(), iterableList(rng2)).isEmpty();
            boolean b2 = iterableList(rng2).minus(Equal.anyEqual(), iterableList(rng1)).isEmpty();
            boolean b3 = iterableList(area).forall(p -> pos2state.get(p) != HintState.Error);
            HintState s = !b1 || !b3 ? HintState.Error : b2 ? HintState.Complete : HintState.Normal;
            if (s != HintState.Complete) isSolved = false;
            for (Position p : rng1)
                pos2state.put(p, s);
        }
    }
}
