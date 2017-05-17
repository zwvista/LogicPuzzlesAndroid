package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class CloudsHelpActivity extends GameHelpActivity<CloudsGame, CloudsDocument, CloudsGameMove, CloudsGameState> {
    @Bean
    protected CloudsDocument document;
    public CloudsDocument doc() {return document;}
}
