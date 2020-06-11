package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.data.SumscrapersDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGame
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain.SumscrapersGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class SumscrapersMainActivity : GameMainActivity<SumscrapersGame, SumscrapersDocument, SumscrapersGameMove, SumscrapersGameState>() {
    @Bean
    protected lateinit var document: SumscrapersDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        SumscrapersOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        SumscrapersGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class SumscrapersOptionsActivity : GameOptionsActivity<SumscrapersGame, SumscrapersDocument, SumscrapersGameMove, SumscrapersGameState>() {
    @Bean
    protected lateinit var document: SumscrapersDocument
    override fun doc() = document

    protected fun onDefault() {}
}

@EActivity(R.layout.activity_game_help)
class SumscrapersHelpActivity : GameHelpActivity<SumscrapersGame, SumscrapersDocument, SumscrapersGameMove, SumscrapersGameState>() {
    @Bean
    protected lateinit var document: SumscrapersDocument
    override fun doc() = document
}