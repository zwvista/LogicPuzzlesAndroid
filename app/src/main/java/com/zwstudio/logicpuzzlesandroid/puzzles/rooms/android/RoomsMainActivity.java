package com.zwstudio.logicpuzzlesandroid.puzzles.rooms.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.data.RoomsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class RoomsMainActivity extends GameMainActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState> {
    public RoomsDocument doc() {return app.roomsDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        RoomsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        RoomsGameActivity_.intent(this).start();
    }
}
