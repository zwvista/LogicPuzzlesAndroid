package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domain.SkyscrapersGame
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

_
@EActivity(R.layout.activity_game_main)
class SkyscrapersMainActivity : GameMainActivity<SkyscrapersGame?, SkyscrapersDocument?, SkyscrapersGameMove?, SkyscrapersGameState?>() {
    @Bean
    protected var document: SkyscrapersDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        SkyscrapersOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        SkyscrapersGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class SkyscrapersOptionsActivity : GameOptionsActivity<SkyscrapersGame?, SkyscrapersDocument?, SkyscrapersGameMove?, SkyscrapersGameState?>() {
    @Bean
    protected var document: SkyscrapersDocument? = null
    override fun doc() = document

    protected fun onDefault() {}
}

@EActivity(R.layout.activity_game_help)
class SkyscrapersHelpActivity : GameHelpActivity<SkyscrapersGame?, SkyscrapersDocument?, SkyscrapersGameMove?, SkyscrapersGameState?>() {
    @Bean
    protected var document: SkyscrapersDocument? = null
    override fun doc() = document
}