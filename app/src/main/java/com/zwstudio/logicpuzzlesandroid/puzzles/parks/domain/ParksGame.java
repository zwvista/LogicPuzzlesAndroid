package com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame;
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
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

public class ParksGame extends CellsGame<ParksGame, ParksGameMove, ParksGameState> {
    public static Position offset[] = {
            new Position(-1, 0),
            new Position(-1, 1),
            new Position(0, 1),
            new Position(1, 1),
            new Position(1, 0),
            new Position(1, -1),
            new Position(0, -1),
            new Position(-1, -1),
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
    public ParksDots dots;
    public int treesInEachArea = 1;

    public ParksGame(List<String> layout, GameInterface<ParksGame, ParksGameMove, ParksGameState> gi) {
        super(gi);
        size = new Position(layout.size() / 2, layout.get(0).length() / 2);
        dots = new ParksDots(rows() + 1, cols() + 1);
        for (int r = 0; r < rows() + 1; r++) {
            String str = layout.get(r * 2);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c * 2 + 1);
                if (ch == '-') {
                    dots.set(r, c, 1, true);
                    dots.set(r, c + 1, 3, true);
                }
            }
            if (r == rows()) break;
            str = layout.get(r * 2 + 1);
            for (int c = 0; c < cols() + 1; c++) {
                Position p = new Position(r, c);
                char ch = str.charAt(c * 2);
                if (ch == '|') {
                    dots.set(r, c, 2, true);
                    dots.set(r + 1, c, 0, true);
                }
            }
        }
        Set<Position> rng = new HashSet<>();
        Graph g = new Graph();
        Map<Position, Node> pos2Node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                rng.add(p);
                Node node = new Node(p.toString());
                g.addNode(node);
                pos2Node.put(p, node);
            }
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                for (int i = 0; i < 4; i++)
                    if (!dots.get(p.add(ParksGame.offset2[i]), ParksGame.dirs[i]))
                        g.connectNode(pos2Node.get(p), pos2Node.get(p.add(ParksGame.offset[i * 2])));
            }
        while (!rng.isEmpty()) {
            g.setRootNode(pos2Node.get(iterableList(rng).head()));
            List<Node> nodeList = g.bfs();
            List<Position> area = iterableList(rng).filter(p -> nodeList.contains(pos2Node.get(p))).toJavaList();
            int n = areas.size();
            for(Position p : area)
                pos2area.put(p, n);
            areas.add(area);
            rng.removeAll(area);
        }
        ParksGameState state = new ParksGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(ParksGameMove move, F2<ParksGameState, ParksGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        ParksGameState state = cloner.deepClone(state());
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

    public boolean switchObject(ParksGameMove move, MarkerOptions markerOption, boolean allowedObjectsOnly) {
        return changeObject(move, (state, move2) -> state.switchObject(move2, markerOption, allowedObjectsOnly));
    }

    public boolean setObject(ParksGameMove move, boolean allowedObjectsOnly) {
        return changeObject(move, (state, move2) -> state.setObject(move2, allowedObjectsOnly));
    }

    public ParksObject getObject(Position p) {
        return state().get(p);
    }

    public ParksObject getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getHintState(Position p) {
        return state().pos2state.get(p);
    }
}
