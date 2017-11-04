package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain;

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

public class KropkiGame extends CellsGame<KropkiGame, KropkiGameMove, KropkiGameState> {
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

    public Map<Position, KropkiHint> pos2horzHint = new HashMap<>();
    public Map<Position, KropkiHint> pos2vertHint = new HashMap<>();
    public List<List<Position>> areas = new ArrayList<>();
    public Map<Position, Integer> pos2area = new HashMap<>();
    public GridDots dots;
    public boolean bordered;

    public KropkiGame(List<String> layout, boolean bordered, GameInterface<KropkiGame, KropkiGameMove, KropkiGameState> gi, GameDocumentInterface gdi) {
        super(gi, gdi);
        size = new Position(bordered ? layout.size() / 4 : layout.size() / 2 + 1, layout.get(0).length());
        this.bordered = bordered;

        for (int r = 0; r < rows() * 2 - 1; r++) {
            String str = layout.get(r);
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r / 2, c);
                char ch = str.charAt(c);
                KropkiHint kh = ch == 'W' ? KropkiHint.Consecutive :
                ch == 'B' ? KropkiHint.Twice : KropkiHint.None;
                (r % 2 == 0 ? pos2horzHint : pos2vertHint).put(p, kh);
            }
        }
        if (bordered) {
            dots = new GridDots(rows() + 1, cols() + 1);
            for (int r = 0; r < rows() + 1; r++) {
                String str = layout.get(rows() * 2 - 1 + 2 * r);
                for (int c = 0; c < cols(); c++) {
                    Position p = new Position(r, c);
                    char ch = str.charAt(c * 2 + 1);
                    if (ch == '-') {
                        dots.set(r, c, 1, GridLineObject.Line);
                        dots.set(r, c + 1, 3, GridLineObject.Line);
                    }
                }
                if (r == rows()) break;
                str = layout.get(rows() * 2 - 1 + 2 * r + 1);
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
                        if (dots.get(p.add(KropkiGame.offset2[i]), KropkiGame.dirs[i]) != GridLineObject.Line)
                            g.connectNode(pos2node.get(p), pos2node.get(p.add(KropkiGame.offset[i * 2])));
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
        }
        KropkiGameState state = new KropkiGameState(this);
        states.add(state);
        levelInitilized(state);
    }

    private boolean changeObject(KropkiGameMove move, F2<KropkiGameState, KropkiGameMove, Boolean> f) {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size()).clear();
            moves.subList(stateIndex, states.size()).clear();
        }
        KropkiGameState state = cloner.deepClone(state());
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

    public boolean switchObject(KropkiGameMove move) {
        return changeObject(move, (state, move2) -> state.switchObject(move2));
    }

    public boolean setObject(KropkiGameMove move) {
        return changeObject(move, (state, move2) -> state.setObject(move2));
    }

    public int getObject(Position p) {
        return state().get(p);
    }

    public int getObject(int row, int col) {
        return state().get(row, col);
    }

    public HintState getHorzState(Position p) {
        return state().pos2horzHint.get(p);
    }

    public HintState getVertState(Position p) {
        return state().pos2vertHint.get(p);
    }
}
