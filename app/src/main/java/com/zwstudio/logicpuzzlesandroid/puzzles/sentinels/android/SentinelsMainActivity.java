package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.data.SentinelsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain.SentinelsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class SentinelsMainActivity extends GameMainActivity<SentinelsGame, SentinelsDocument, SentinelsGameMove, SentinelsGameState> {
    @Bean
    protected SentinelsDocument document;
    public SentinelsDocument doc() {return document;}

    @Click
    void btnOptions() {
        SentinelsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        SentinelsGameActivity_.intent(this).start();
    }
}
