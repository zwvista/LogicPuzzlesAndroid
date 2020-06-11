package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGame
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class MasyuMainActivity : GameMainActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState>() {
    @Bean
    protected lateinit var document: MasyuDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        MasyuOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        MasyuGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class MasyuOptionsActivity : GameOptionsActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState>() {
    @Bean
    protected lateinit var document: MasyuDocument
    override fun doc() = document

    protected fun onDefault() {}
}

@EActivity(R.layout.activity_game_help)
class MasyuHelpActivity : GameHelpActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState>() {
    @Bean
    protected lateinit var document: MasyuDocument
    override fun doc() = document
}