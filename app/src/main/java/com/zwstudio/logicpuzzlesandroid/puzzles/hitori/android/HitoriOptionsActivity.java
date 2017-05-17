package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class HitoriOptionsActivity extends GameOptionsActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState> {
    @Bean
    protected HitoriDocument document;
    public HitoriDocument doc() {return document;}
}
