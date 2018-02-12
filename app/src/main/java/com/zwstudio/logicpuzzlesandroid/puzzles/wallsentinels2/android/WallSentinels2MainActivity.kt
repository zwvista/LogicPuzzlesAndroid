package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.data.WallSentinels2Document
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain.WallSentinels2Game
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain.WallSentinels2GameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain.WallSentinels2GameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class WallSentinels2MainActivity : GameMainActivity<WallSentinels2Game, WallSentinels2Document, WallSentinels2GameMove, WallSentinels2GameState>() {
    @Bean
    protected lateinit var document: WallSentinels2Document
    override fun doc() = document

    @Click
    fun btnOptions() = WallSentinels2OptionsActivity_.intent(this).start()

    override fun resumeGame() {
        doc().resumeGame()
        WallSentinels2GameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class WallSentinels2OptionsActivity : GameOptionsActivity<WallSentinels2Game, WallSentinels2Document, WallSentinels2GameMove, WallSentinels2GameState>() {
    @Bean
    protected lateinit var document: WallSentinels2Document
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class WallSentinels2HelpActivity : GameHelpActivity<WallSentinels2Game, WallSentinels2Document, WallSentinels2GameMove, WallSentinels2GameState>() {
    @Bean
    protected lateinit var document: WallSentinels2Document
    override fun doc() = document
}
