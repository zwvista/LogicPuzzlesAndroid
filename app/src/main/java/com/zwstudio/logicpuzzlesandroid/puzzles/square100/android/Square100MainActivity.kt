package com.zwstudio.logicpuzzlesandroid.puzzles.square100.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.square100.domain.Square100Game
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

_
@EActivity(R.layout.activity_game_main)
class Square100MainActivity : GameMainActivity<Square100Game?, Square100Document?, Square100GameMove?, Square100GameState?>() {
    @Bean
    protected var document: Square100Document? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        Square100OptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        Square100GameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class Square100OptionsActivity : GameOptionsActivity<Square100Game?, Square100Document?, Square100GameMove?, Square100GameState?>() {
    @Bean
    protected var document: Square100Document? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class Square100HelpActivity : GameHelpActivity<Square100Game?, Square100Document?, Square100GameMove?, Square100GameState?>() {
    @Bean
    protected var document: Square100Document? = null
    override fun doc() = document
}