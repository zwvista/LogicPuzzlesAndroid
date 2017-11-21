package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.data.GardenerDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain.GardenerGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class GardenerMainActivity extends GameMainActivity<GardenerGame, GardenerDocument, GardenerGameMove, GardenerGameState> {
    @Bean
    protected GardenerDocument document;
    public GardenerDocument doc() {return document;}

    @Click
    void btnOptions() {
        GardenerOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        GardenerGameActivity_.intent(this).start();
    }
}
