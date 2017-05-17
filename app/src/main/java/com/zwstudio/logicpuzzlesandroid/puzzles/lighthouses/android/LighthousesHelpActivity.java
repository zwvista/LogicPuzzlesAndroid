package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.data.LighthousesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain.LighthousesGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class LighthousesHelpActivity extends GameHelpActivity<LighthousesGame, LighthousesDocument, LighthousesGameMove, LighthousesGameState> {
    public LighthousesDocument doc() {return app.lighthousesDocument;}
}
