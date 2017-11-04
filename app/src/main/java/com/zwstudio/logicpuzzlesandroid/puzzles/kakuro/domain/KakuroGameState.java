package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zwvista on 2016/09/29.
 */

public class KakuroGameState extends CellsGameState<KakuroGame, KakuroGameMove, KakuroGameState> {
    public Map<Position, Integer> pos2num = new HashMap<>();
    public Map<Position, HintState> pos2horzHint = new HashMap<>();
    public Map<Position, HintState> pos2vertHint = new HashMap<>();

    public KakuroGameState(KakuroGame game) {
        super(game);
        pos2num = new HashMap<>(game.pos2num);
        updateIsSolved();
    }

    public Integer get(Position p) {
        return pos2num.get(p);
    }
    public void set(Position p, Integer obj) {
        pos2num.put(p, obj);
    }

    public boolean setObject(KakuroGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == null || get(p) == move.obj) return false;
        set(p, move.obj);
        updateIsSolved();
        return true;
    }

    public boolean switchObject(KakuroGameMove move) {
        Position p = move.p;
        if (!isValid(p) || get(p) == null) return false;
        int o = get(p);
        move.obj = (o + 1) % 10;
        return setObject(move);
    }

    /*
        iOS Game: Logic Games/Puzzle Set 4/Kakuro

        Summary
        Fill the board with numbers 1 to 9 according to the sums

        Description
        1. Your goal is to write a number in every blank tile (without a diagonal
           line).
        2. The number on the top of a column or at the left of a row, gives you
           the sum of the numbers in that column or row.
        3. You can write numbers 1 to 9 in the tiles, however no same number should
           appear in a consecutive row or column.
        4. Tiles which only have a diagonal line aren't used in the game.
    */
    private void updateIsSolved() {
        isSolved = true;
        for (Map.Entry<Position, Integer> entry : game.pos2horzHint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue(), n1 = 0;
            Position os = KakuroGame.offset[1];
            Integer n;
            for (Position p2 = p.add(os); (n = pos2num.get(p2)) != null; p2.addBy(os))
                n1 += n;
            HintState s = n1 == 0 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            pos2horzHint.put(p, s);
            if (s != HintState.Complete) isSolved = false;
        }
        for (Map.Entry<Position, Integer> entry : game.pos2vertHint.entrySet()) {
            Position p = entry.getKey();
            int n2 = entry.getValue(), n1 = 0;
            Position os = KakuroGame.offset[2];
            Integer n;
            for (Position p2 = p.add(os); (n = pos2num.get(p2)) != null; p2.addBy(os))
                n1 += n;
            HintState s = n1 == 0 ? HintState.Normal : n1 == n2 ? HintState.Complete : HintState.Error;
            pos2vertHint.put(p, s);
            if (s != HintState.Complete) isSolved = false;
        }
    }
}
