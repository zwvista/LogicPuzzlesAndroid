package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino.domain;

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

public class TataminoGameState extends CellsGameState<TataminoGame, TataminoGameMove, TataminoGameState> {
    protected Cloner cloner = new Cloner();

    public char[] objArray;
    public GridDots dots;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public TataminoGameState(TataminoGame game) {
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

    public boolean setObject(TataminoGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(TataminoGameMove move) {
        Position p = move.p;
        if (!isValid(p) || game.get(p) != ' ') return false;
        char o = get(p);
        move.obj = o == ' ' ? '1' : o == '3' ? ' ' : (char)(o + 1);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Tatamino

        Summary
        Which is a little Tatami

        Description
        1. Plays like Fillomino, in which you have to guess areas on the board
           marked by their number.
        2. In Tatamino, however, you only have areas 1, 2 and 3 tiles big.
        3. Please remember two areas of the same number size can't be touching.
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
                for (Position os : TataminoGame.offset) {
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
                    Position p2 = p.add(TataminoGame.offset[i]);
                    char ch2 = !isValid(p2) ? '.' : get(p2);
                    if (ch2 != ch && (n1 <= n2 || ch2 != ' '))
                        dots.set(p.add(TataminoGame.offset2[i]), TataminoGame.dirs[i], GridLineObject.Line);
                }
            }
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
