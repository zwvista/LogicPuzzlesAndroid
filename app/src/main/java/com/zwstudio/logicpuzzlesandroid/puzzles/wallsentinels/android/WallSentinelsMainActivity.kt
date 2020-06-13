package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.data.WallSentinelsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain.WallSentinelsGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class WallSentinelsMainActivity : GameMainActivity<WallSentinelsGame, WallSentinelsDocument, WallSentinelsGameMove, WallSentinelsGameState>() {
    @Bean
    protected lateinit var document: WallSentinelsDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        WallSentinelsOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        WallSentinelsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class WallSentinelsOptionsActivity : GameOptionsActivity<WallSentinelsGame, WallSentinelsDocument, WallSentinelsGameMove, WallSentinelsGameState>() {
    @Bean
    protected lateinit var document: WallSentinelsDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class WallSentinelsHelpActivity : GameHelpActivity<WallSentinelsGame, WallSentinelsDocument, WallSentinelsGameMove, WallSentinelsGameState>() {
    @Bean
    protected lateinit var document: WallSentinelsDocument
    override val doc get() = document
}
