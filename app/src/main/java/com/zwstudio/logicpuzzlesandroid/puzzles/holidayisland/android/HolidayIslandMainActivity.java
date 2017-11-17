package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.data.HolidayIslandDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain.HolidayIslandGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class HolidayIslandMainActivity extends GameMainActivity<HolidayIslandGame, HolidayIslandDocument, HolidayIslandGameMove, HolidayIslandGameState> {
    @Bean
    protected HolidayIslandDocument document;
    public HolidayIslandDocument doc() {return document;}

    @Click
    void btnOptions() {
        HolidayIslandOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        HolidayIslandGameActivity_.intent(this).start();
    }
}
