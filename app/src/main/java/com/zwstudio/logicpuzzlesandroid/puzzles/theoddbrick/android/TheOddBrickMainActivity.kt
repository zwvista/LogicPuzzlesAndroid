package com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.data.TheOddBrickDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.domain.TheOddBrickGame
import com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.domain.TheOddBrickGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.domain.TheOddBrickGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TheOddBrickMainActivity : GameMainActivity<TheOddBrickGame, TheOddBrickDocument, TheOddBrickGameMove, TheOddBrickGameState>() {
    @Bean
    protected lateinit var document: TheOddBrickDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        TheOddBrickOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        TheOddBrickGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TheOddBrickOptionsActivity : GameOptionsActivity<TheOddBrickGame, TheOddBrickDocument, TheOddBrickGameMove, TheOddBrickGameState>() {
    @Bean
    protected lateinit var document: TheOddBrickDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class TheOddBrickHelpActivity : GameHelpActivity<TheOddBrickGame, TheOddBrickDocument, TheOddBrickGameMove, TheOddBrickGameState>() {
    @Bean
    protected lateinit var document: TheOddBrickDocument
    override val doc get() = document
}