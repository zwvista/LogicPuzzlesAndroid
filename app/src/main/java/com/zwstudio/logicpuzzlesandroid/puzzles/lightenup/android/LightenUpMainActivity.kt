package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGame
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class LightenUpMainActivity : GameMainActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState>() {
    @Bean
    protected lateinit var document: LightenUpDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        LightenUpOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        LightenUpGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class LightenUpOptionsActivity : GameOptionsActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState>() {
    @Bean
    protected lateinit var document: LightenUpDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class LightenUpHelpActivity : GameHelpActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState>() {
    @Bean
    protected lateinit var document: LightenUpDocument
    override fun doc() = document
}