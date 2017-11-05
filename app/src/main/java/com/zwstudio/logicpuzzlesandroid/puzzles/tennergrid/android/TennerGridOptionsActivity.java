package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.data.TennerGridDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid.domain.TennerGridGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class TennerGridOptionsActivity extends GameOptionsActivity<TennerGridGame, TennerGridDocument, TennerGridGameMove, TennerGridGameState> {
    @Bean
    protected TennerGridDocument document;
    public TennerGridDocument doc() {return document;}
}
