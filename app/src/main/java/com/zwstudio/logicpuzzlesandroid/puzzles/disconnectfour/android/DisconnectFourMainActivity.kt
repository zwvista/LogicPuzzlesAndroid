package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.data.DisconnectFourDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGame
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain.DisconnectFourGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class DisconnectFourMainActivity : GameMainActivity<DisconnectFourGame, DisconnectFourDocument, DisconnectFourGameMove, DisconnectFourGameState>() {
    @Bean
    protected lateinit var document: DisconnectFourDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        DisconnectFourOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        DisconnectFourGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class DisconnectFourOptionsActivity : GameOptionsActivity<DisconnectFourGame, DisconnectFourDocument, DisconnectFourGameMove, DisconnectFourGameState>() {
    @Bean
    protected lateinit var document: DisconnectFourDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class DisconnectFourHelpActivity : GameHelpActivity<DisconnectFourGame, DisconnectFourDocument, DisconnectFourGameMove, DisconnectFourGameState>() {
    @Bean
    protected lateinit var document: DisconnectFourDocument
    override fun doc() = document
}