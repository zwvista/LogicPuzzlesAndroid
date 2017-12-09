package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain;

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridDots;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F2;

import static fj.data.HashMap.fromMap;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class CalcudokuGame extends CellsGame<CalcudokuGame, CalcudokuGameMove, CalcudokuGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(0, -1),
    };
    public static Position offset2[] = {
            new Position(0, 0),
            new Position(1, 1),
            new Position(1, 1),
            new Position(0, 0),
    };
    public static int dirs[] = {1, 0, 3, 2};

    public List<List<Position>> areas = new ArrayList<>();
    public Map<Position, Integer> pos2area = new HashMap<>();
    public GridDots dots;

    public char[] objArray;
    public Map<Position, CalcudokuHint> pos2hint = new HashMap<>();

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

    public CalcudokuGame(List<String> layout, GameInterface<CalcudokuGame, CalcudokuGameMove, CalcudokuGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size(), layout.get(0).length() / 4);
        dots = new GridDots(rows() + 1, cols() + 1);
        objArray = new char[rows() * cols()];
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch1 = str.charAt(c * 4);
                String s = str.substring(c * 4 + 1, c * 4 + 3);
                char ch2 = str.charAt(c * 4 + 3);
                set(p, ch1);
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
                if (s.equals("  ")) continue;
                pos2hint.put(p, new CalcudokuHint() {{
                    op = ch2;
                    result = s.equals("  ") ? 0 : Integer.parseInt(s.trim());
                }});
            }
        }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = get(p);
                if (ch == ' ') continue;
                for (Position os : CalcudokuGame.offset) {
                    Position p2 = p.add(os);
                    if (isValid(p2) && get(p2) == ch)
                        g.connectNode(pos2node.get(p), pos2node.get(p2));
                }
            }
        while (!pos2node.isEmpty()) {
            g.setRootNode(iterableList(pos2node.values()).head());
            List<Node> nodeList = g.bfs();
            List<Position> area = fromMap(pos2node).toStream().filter(e -> nodeList.contains(e._2())).map(e -> e._1()).toJavaList();
            int n = areas.size();
            char ch = get(area.get(0));
            for (Position p : area) {
                pos2area.put(p, n);
                pos2node.remove(p);
                for (int i = 0; i < 4; i++) {
                    Position p2 = p.add(CalcudokuGame.offset[i]);
                    char ch2 = !isValid(p2) ? '.' : get(p2);
                    if (ch2 != ch)
                        dots.set(p.add(CalcudokuGame.offset2[i]), CalcudokuGame.dirs[i], GridLineObject.Line);
                }
            }
            areas.add(area);
        }
        for (int r = 0; r < rows(); r++) {
            dots.set(r, 0, 2, GridLineObject.Line);
            dots.set(r + 1, 0, 0, GridLineObject.Line);
            dots.set(r, cols(), 2, GridLineObject.Line);
            dots.set(r + 1, cols(), 0, GridLineObject.Line);
        }
        for (int c = 0; c < cols(); c++) {
            dots.set(0, c, 1, GridLineObject.Line);
            dots.set(0, c + 1, 3, GridLineObject.Line);
            dots.set(rows(), c, 1, GridLineObject.Line);
            dots.set(rows(), c + 1, 3, GridLineObject.Line);
        }

        CalcudokuGameState state = new CalcudokuGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(CalcudokuGameMove move, F2<CalcudokuGameState, CalcudokuGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        CalcudokuGameState state = cloner.deepClone(state());
        boolean changed = f.f(state, move);
        if (changed) {
            states.add(state);
            stateIndex++;
            moves.add(move);
            moveAdded(move);
            levelUpdated(states.get(stateIndex - 1), state);
        }
        return changed;
   }

    public boolean switchObject(CalcudokuGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(CalcudokuGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public int getObject(Position p) {
        return state().get(p);
    }

    public int getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getRowState(int row) {
        return state().row2state[row];
    }

    public HintState getColState(int col) {
        return state().col2state[col];
    }

    public HintState getPosState(Position p) {
        return state().pos2state.get(p);
    }
}
