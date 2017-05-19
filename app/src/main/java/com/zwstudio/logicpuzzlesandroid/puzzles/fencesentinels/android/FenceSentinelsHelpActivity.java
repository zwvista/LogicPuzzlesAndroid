package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.data.FenceSentinelsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class FenceSentinelsHelpActivity extends GameHelpActivity<FenceSentinelsGame, FenceSentinelsDocument, FenceSentinelsGameMove, FenceSentinelsGameState> {
    @Bean
    protected FenceSentinelsDocument document;
    public FenceSentinelsDocument doc() {return document;}
}
