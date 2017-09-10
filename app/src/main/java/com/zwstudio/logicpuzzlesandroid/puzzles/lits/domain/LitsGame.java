package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fj.F2;

import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LitsGame extends CellsGame<LitsGame, LitsGameMove, LitsGameState> {
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
    public static Position offset3[] = {
            new Position(0, 0),
            new Position(0, 1),
            new Position(1, 0),
            new Position(1, 1),
    };

    public static Position tetrominoes[][][] = {
        { // L
            {new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(2, 1)},
            {new Position(0, 1), new Position(1, 1), new Position(2, 0), new Position(2, 1)},
            {new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(1, 0)},
            {new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(1, 2)},
            {new Position(0, 0), new Position(0, 1), new Position(1, 0), new Position(2, 0)},
            {new Position(0, 0), new Position(0, 1), new Position(1, 1), new Position(2, 1)},
            {new Position(0, 0), new Position(1, 0), new Position(1, 1), new Position(1, 2)},
            {new Position(0, 2), new Position(1, 0), new Position(1, 1), new Position(1, 2)},
        },
        { // I
            {new Position(0, 0), new Position(1, 0), new Position(2, 0), new Position(3, 0)},
            {new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(0, 3)},
        },
        { // T
            {new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(1, 1)},
            {new Position(0, 1), new Position(1, 0), new Position(1, 1), new Position(2, 1)},
            {new Position(0, 1), new Position(1, 0), new Position(1, 1), new Position(1, 2)},
            {new Position(0, 0), new Position(1, 0), new Position(1, 1), new Position(2, 0)},
        },
        { // S
            {new Position(0, 0), new Position(0, 1), new Position(1, 1), new Position(1, 2)},
            {new Position(0, 1), new Position(0, 2), new Position(1, 0), new Position(1, 1)},
            {new Position(0, 0), new Position(1, 0), new Position(1, 1), new Position(2, 1)},
            {new Position(0, 1), new Position(1, 0), new Position(1, 1), new Position(2, 0)},
        },
    };

    public List<List<Position>> areas = new ArrayList<>();
    public Map<Position, Integer> pos2area = new HashMap<>();
    public GridDots dots;
    public int treesInEachArea = 1;

    public LitsGame(List<String> layout, GameInterface<LitsGame, LitsGameMove, LitsGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(layout.size() / 2, layout.get(0).length() / 2);
        dots = new GridDots(rows() + 1, cols() + 1);
        for (int r = 0; r < rows() + 1; r++) {
            String str = layout.get(r * 2);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c * 2 + 1);
                if (ch == '-') {
                    dots.set(r, c, 1, GridLineObject.Line);
                    dots.set(r, c + 1, 3, GridLineObject.Line);
                }
            }
            if (r == rows()) break;
            str = layout.get(r * 2 + 1);
            for (int c = 0; c < cols() + 1; c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c * 2);
                if (ch == '|') {
                    dots.set(r, c, 2, GridLineObject.Line);
                    dots.set(r + 1, c, 0, GridLineObject.Line);
                }
            }
        }
        Set<Position> rng = new HashSet<>();
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                rng.add(p.plus());
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2node.put(p, node);
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                for (int i = 0; i < 4; i++)
                    if (dots.get(p.add(LitsGame.offset2[i]), LitsGame.dirs[i]) != GridLineObject.Line)
                        g.connectNode(pos2node.get(p), pos2node.get(p.add(LitsGame.offset[i])));
            }
        while (!rng.isEmpty()) {
            g.setRootNode(pos2node.get(iterableList(rng).head()));
            List<Node> nodeList = g.bfs();
            List<Position> area = iterableList(rng).filter(p -> nodeList.contains(pos2node.get(p))).toJavaList();
            int n = areas.size();
            for(Position p : area)
                pos2area.put(p, n);
            areas.add(area);
            rng.removeAll(area);
        }
        LitsGameState state = new LitsGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(LitsGameMove move, F2<LitsGameState, LitsGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        LitsGameState state = cloner.deepClone(state());
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

    public boolean switchObject(LitsGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(LitsGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public LitsObject getObject(Position p) {
        return state().get(p);
    }

    public LitsObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState hint2State(Position p) {
        return state().pos2state.get(p);
    }
}
