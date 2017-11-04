package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.data.RobotFencesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain.RobotFencesGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class RobotFencesHelpActivity extends GameHelpActivity<RobotFencesGame, RobotFencesDocument, RobotFencesGameMove, RobotFencesGameState> {
    @Bean
    protected RobotFencesDocument document;
    public RobotFencesDocument doc() {return document;}
}
