package com.zwstudio.logicpuzzlesandroid.puzzles.tatami.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.data.TatamiDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.tatami.domain.TatamiGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class TatamiHelpActivity extends GameHelpActivity<TatamiGame, TatamiDocument, TatamiGameMove, TatamiGameState> {
    @Bean
    protected TatamiDocument document;
    public TatamiDocument doc() {return document;}
}
