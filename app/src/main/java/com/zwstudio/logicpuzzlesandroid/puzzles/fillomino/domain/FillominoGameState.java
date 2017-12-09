package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain;

import com.rits.cloning.Cloner;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridDots;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class FillominoGameState extends CellsGameState<FillominoGame, FillominoGameMove, FillominoGameState> {
    protected Cloner cloner = new Cloner();

    public char[] objArray;
    public GridDots dots;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public FillominoGameState(FillominoGame game) {
        super(game);
        objArray = new char[rows() * cols()];
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.length);
        updateIsSolved();
    }

    public char get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public char get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, char obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, char obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(FillominoGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(FillominoGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != ' ') return false;
        char o = get(p);
        move.obj = o == ' ' ? '1' : o == game.chMax ? ' ' : (char)(o + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/Fillomino

        Summary
        Detect areas marked by their extension

        Description
        1. The goal is to detect areas marked with the tile count of the area
           itself.
        2. So for example, areas marked '1', will always consist of one single
           tile. Areas marked with '2' will consist of two (horizontally or
           vertically) adjacent tiles. Tiles numbered '3' will appear in a group
           of three and so on.
        3. Some areas can also be totally hidden at the start.

        Variation
        4. Fillomino has several variants.
        5. No Rectangles: Areas can't form Rectangles.
        6. Only Rectangles: Areas can ONLY form Rectangles.
        7. Non Consecutive: Areas can't touch another area which has +1 or -1
           as number (orthogonally).
        8. Consecutive: Areas MUST touch another area which has +1 or -1
           as number (orthogonally).
        9. All Odds: There are only odd numbers on the board.
        10.All Evens: There are only even numbers on the board.
    */
    private void updateIsSolved() {
        isSolved = true;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) == ' ')
                    isSolved = false;
                else {
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = get(p);
                if (ch == ' ') continue;
                for (Position os : FillominoGame.offset) {
                    Position p2 = p.add(os);
                    if (isValid(p2) && get(p2) == ch)
                        g.connectNode(pos2node.get(p), pos2node.get(p2));
                }
            }
        dots = cloner.deepClone(game.dots);
        while (!pos2node.isEmpty()) {
            g.setRootNode(iterableList(pos2node.values()).head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            for (Position p : area)
                pos2node.remove(p);
            char ch = get(area.get(0));
            int n1 = area.size(), n2 = ch - '0';
            HintState s = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            for (Position p : area) {
                pos2state.put(p, s);
                for (int i = 0; i < 4; i++) {
                    Position p2 = p.add(FillominoGame.offset[i]);
                    char ch2 = !isValid(p2) ? '.' : get(p2);
                    if (ch2 != ch && (n1 <= n2 || ch2 != ' '))
                        dots.set(p.add(FillominoGame.offset2[i]), FillominoGame.dirs[i], GridLineObject.Line);
                }
            }
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
