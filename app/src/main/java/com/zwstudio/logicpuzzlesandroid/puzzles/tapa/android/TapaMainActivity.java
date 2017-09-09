package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.data.TapaDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class TapaMainActivity extends GameMainActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState> {
    @Bean
    protected TapaDocument document;
    public TapaDocument doc() {return document;}

    @Click
    void btnOptions() {
        TapaOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        TapaGameActivity_.intent(this).start();
    }
}
