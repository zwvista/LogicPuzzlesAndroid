package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph;
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions;
import com.zwstudio.logicpuzzlesandroid.common.domain.Node;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.Array.array;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class PaintTheNurikabeGameState extends CellsGameState<PaintTheNurikabeGame, PaintTheNurikabeGameMove, PaintTheNurikabeGameState> {
    public PaintTheNurikabeObject[] objArray;
    public Map<Position, HintState> pos2state = new HashMap<>();

    public PaintTheNurikabeGameState(PaintTheNurikabeGame game) {
        super(game);
        objArray = new PaintTheNurikabeObject[rows() * cols()];
        Arrays.fill(objArray, PaintTheNurikabeObject.Empty);
        for (Position p : game.pos2hint.keySet())
            pos2state.put(p, HintState.Normal);
        updateIsSolved();
    }

    public PaintTheNurikabeObject get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public PaintTheNurikabeObject get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, PaintTheNurikabeObject obj) {
        objArray[row * cols() + col] = obj;
    }
    public void set(Position p, PaintTheNurikabeObject obj) {
        set(p.row, p.col, obj);
    }

    public boolean setObject(PaintTheNurikabeGameMove move) {
        Position p = move.p;
        PaintTheNurikabeObject o = move.obj;
        if (!isValid(p) || get(p).equals(o)) return false;
        set(p, o);
        for (Position p2 : game.areas.get(game.pos2area.get(p)))
            set(p2, o);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(PaintTheNurikabeGameMove move) {
        MarkerOptions markerOption = MarkerOptions.values()[game.gdi.getMarkerOption()];
        F<PaintTheNurikabeObject, PaintTheNurikabeObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == MarkerOptions.MarkerFirst ?
                        PaintTheNurikabeObject.Marker : PaintTheNurikabeObject.Painted;
            case Painted:
                return markerOption == MarkerOptions.MarkerLast ?
                        PaintTheNurikabeObject.Marker : PaintTheNurikabeObject.Empty;
            case Marker:
                return markerOption == MarkerOptions.MarkerFirst ?
                        PaintTheNurikabeObject.Painted : PaintTheNurikabeObject.Empty;
            }
            return obj;
        };
        PaintTheNurikabeObject o = get(move.p);
        move.obj = f.f(o);
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Paint The Nurikabe

        Summary
        Paint areas, find Nurikabes

        Description
        1. By painting (filling) the areas you have to complete a Nurikabe.
           Specifically:
        2. A number indicates how many painted tiles are adjacent to it.
        3. The painted tiles form an orthogonally continuous area, like a
           Nurikabe.
        4. There can't be any 2*2 area of the same color(painted or empty).
    */
    private void updateIsSolved() {
        boolean allowedObjectsOnly = game.gdi.isAllowedObjectsOnly();
        isSolved = true;
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                PaintTheNurikabeObject o = get(r, c);
                if (o == PaintTheNurikabeObject.Forbidden)
                    set(r, c, PaintTheNurikabeObject.Empty);
            }
        // 2. A number indicates how many painted tiles are adjacent to it.
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            List<Position> rng = new ArrayList<>();
            int n1 = 0;
            for (Position os : PaintTheNurikabeGame.offset) {
                Position p2 = p.add(os);
                if (!isValid(p2)) continue;
                PaintTheNurikabeObject o = get(p2);
                if (o == PaintTheNurikabeObject.Painted)
                    n1++;
                else if (o == PaintTheNurikabeObject.Empty)
                    rng.add(p2);
            }
            HintState s = n1 < n2 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            pos2state.put(p, s);
            if (s != HintState.Complete)
                isSolved = false;
            else
                for (Position p2 : rng)
                    set(p2, PaintTheNurikabeObject.Forbidden);
        }
        // 4. There can't be any 2*2 area of the same color(painted or empty).
        for (int r = 0; r < rows() - 1; r++)
            for (int c = 0; c < cols() - 1; c++) {
                Position p = new Position(r, c);
                if (array(NurikabeGame.offset2).forall(os -> get(p.add(os)) == PaintTheNurikabeObject.Painted) ||
                        array(NurikabeGame.offset2).forall(os -> get(p.add(os)) == PaintTheNurikabeObject.Empty)) {
                    isSolved = false; return;
                }
            }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                if (get(p) == PaintTheNurikabeObject.Painted) {
                    Node node = new Node(p.toString());
                    g.addNode(node);
                    pos2node.put(p, node);
                }
            }
        for (Position p : pos2node.keySet())
            for (Position os : NurikabeGame.offset) {
                Position p2 = p.add(os);
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node.get(p), pos2node.get(p2));
            }
        // 3. The painted tiles form an orthogonally continuous area, like a
        // Nurikabe.
        g.setRootNode(iterableList(pos2node.values()).head());
        List<Node> nodeList = g.bfs();
        if (nodeList.size() != pos2node.size()) isSolved = false;
    }
}
