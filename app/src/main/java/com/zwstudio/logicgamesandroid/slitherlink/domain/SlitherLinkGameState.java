package com.zwstudio.logicgamesandroid.slitherlink.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.CellsGameState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Graph;
import com.zwstudio.logicgamesandroid.logicgames.domain.LogicGamesHintState;
import com.zwstudio.logicgamesandroid.logicgames.domain.Node;
import com.zwstudio.logicgamesandroid.logicgames.domain.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.F;

import static fj.data.Array.arrayArray;
import static fj.data.List.iterableList;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SlitherLinkGameState extends CellsGameState<SlitherLinkGame> {
    public SlitherLinkObject[][] objArray;
    public Map<Position, LogicGamesHintState> pos2state = new HashMap<>();

    public SlitherLinkGameState(SlitherLinkGame game) {
        super(game);
        objArray = new SlitherLinkObject[rows() * cols()][];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new SlitherLinkObject[4];
            Arrays.fill(objArray[i], SlitherLinkObject.Empty);
        }
    }

    public SlitherLinkObject[] get(int row, int col) {
        return objArray[row * cols() + col];
    }
    public SlitherLinkObject[] get(Position p) {
        return get(p.row, p.col);
    }
    public void set(int row, int col, SlitherLinkObject[] dotObj) {
        objArray[row * cols() + col] = dotObj;
    }
    public void set(Position p, SlitherLinkObject[] obj) {
        set(p.row, p.col, obj);
    }

    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2hint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue();
            int n1 = 0;
            if (get(p)[1] == SlitherLinkObject.Line) n1++;
            if (get(p)[2] == SlitherLinkObject.Line) n1++;
            if (get(p.add(new Position(1, 1)))[0] == SlitherLinkObject.Line) n1++;
            if (get(p.add(new Position(1, 1)))[3] == SlitherLinkObject.Line) n1++;
            pos2state.put(p, n1 < n2 ? LogicGamesHintState.Normal : n1 == n2 ? LogicGamesHintState.Complete : LogicGamesHintState.Error);
            if (n1 != n2) isSolved = false;
        }
        if (!isSolved) return;
        Graph g = new Graph();
        Map<Position, Node> pos2Node = new HashMap<>();
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                Position p = new Position(r, c);
                int n = arrayArray(get(p)).filter(o -> o == SlitherLinkObject.Line).length();
                switch (n) {
                case 0:
                    continue;
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
            SlitherLinkObject[] dotObj = get(p);
            for (int i = 0; i < 4; i++) {
                if (dotObj[i] != SlitherLinkObject.Line) continue;
                Position p2 = p.add(SlitherLinkGame.offset[i]);
                g.connectNode(pos2Node.get(p), pos2Node.get(p2));
            }
        }
        g.setRootNode(iterableList(pos2Node.values()).head());
        List<Node> nodeList = g.bfs();
        int n1 = nodeList.size();
        int n2 = pos2Node.values().size();
        if (n1 != n2) isSolved = false;
    }

    public boolean setObject(SlitherLinkGameMove move) {
        Position p1 = move.p;
        boolean isH = move.objOrientation == SlitherLinkObjectOrientation.Horizontal;
        int i1 = isH ? 1 : 2;
        SlitherLinkObject o = get(p1)[i1];
        if (o.equals(move.obj)) return false;
        Position p2 = p1.add(SlitherLinkGame.offset[isH ? 1 : 2]);
        int i2 = isH ? 3 : 0;
        get(p2)[i2] = get(p1)[i1] = move.obj;
        updateIsSolved();
        return true;
    }

    public boolean switchObject(SlitherLinkMarkerOptions markerOption, SlitherLinkGameMove move) {
        F<SlitherLinkObject, SlitherLinkObject> f = obj -> {
            switch (obj) {
            case Empty:
                return markerOption == SlitherLinkMarkerOptions.MarkerBeforeLine ?
                        SlitherLinkObject.Marker : SlitherLinkObject.Line;
            case Line:
                return markerOption == SlitherLinkMarkerOptions.MarkerAfterLine ?
                        SlitherLinkObject.Marker : SlitherLinkObject.Empty;
            case Marker:
                return markerOption == SlitherLinkMarkerOptions.MarkerBeforeLine ?
                        SlitherLinkObject.Line : SlitherLinkObject.Empty;
            }
            return obj;
        };
        SlitherLinkObject[] dotObj = get(move.p);
        move.obj = f.f(dotObj[move.objOrientation == SlitherLinkObjectOrientation.Horizontal ? 1 : 2]);
        return setObject(move);
    }
}
