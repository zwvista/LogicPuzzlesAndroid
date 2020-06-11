package com.zwstudio.logicpuzzlesandroid.puzzles.snail.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.data.SnailDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGame
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.snail.domain.SnailGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class SnailMainActivity : GameMainActivity<SnailGame, SnailDocument, SnailGameMove, SnailGameState>() {
    @Bean
    protected lateinit var document: SnailDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        SnailOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        SnailGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class SnailOptionsActivity : GameOptionsActivity<SnailGame, SnailDocument, SnailGameMove, SnailGameState>() {
    @Bean
    protected lateinit var document: SnailDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class SnailHelpActivity : GameHelpActivity<SnailGame, SnailDocument, SnailGameMove, SnailGameState>() {
    @Bean
    protected lateinit var document: SnailDocument
    override fun doc() = document
}