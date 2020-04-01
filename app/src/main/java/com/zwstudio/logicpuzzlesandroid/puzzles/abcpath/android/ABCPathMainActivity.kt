package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.data.ABCPathDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGame
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class ABCPathMainActivity : GameMainActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState>() {
    @Bean
    protected lateinit var document: ABCPathDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        ABCPathOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        ABCPathGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class ABCPathOptionsActivity : GameOptionsActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState>() {
    @Bean
    protected lateinit var document: ABCPathDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class ABCPathHelpActivity : GameHelpActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState>() {
    @Bean
    protected lateinit var document: ABCPathDocument
    override fun doc() = document
}
