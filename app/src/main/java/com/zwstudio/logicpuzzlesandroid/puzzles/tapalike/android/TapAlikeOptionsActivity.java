package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.data.TapAlikeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain.TapAlikeGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class TapAlikeOptionsActivity extends GameOptionsActivity<TapAlikeGame, TapAlikeDocument, TapAlikeGameMove, TapAlikeGameState> {
    @Bean
    protected TapAlikeDocument document;
    public TapAlikeDocument doc() {return document;}
}
