package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.data.KakurasuDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class KakurasuMainActivity extends GameMainActivity<KakurasuGame, KakurasuDocument, KakurasuGameMove, KakurasuGameState> {
    @Bean
    protected KakurasuDocument document;
    public KakurasuDocument doc() {return document;}

    @Click
    void btnOptions() {
        KakurasuOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        KakurasuGameActivity_.intent(this).start();
    }
}
