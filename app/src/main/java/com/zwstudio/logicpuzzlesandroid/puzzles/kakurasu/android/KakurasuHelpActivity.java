package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.data.KakurasuDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain.KakurasuGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class KakurasuHelpActivity extends GameHelpActivity<KakurasuGame, KakurasuDocument, KakurasuGameMove, KakurasuGameState> {
    @Bean
    protected KakurasuDocument document;
    public KakurasuDocument doc() {return document;}
}
