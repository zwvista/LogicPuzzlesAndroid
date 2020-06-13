package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class NoughtsAndCrossesMainActivity : GameMainActivity<NoughtsAndCrossesGame, NoughtsAndCrossesDocument, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>() {
    @Bean
    protected lateinit var document: NoughtsAndCrossesDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        NoughtsAndCrossesOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        NoughtsAndCrossesGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class NoughtsAndCrossesOptionsActivity : GameOptionsActivity<NoughtsAndCrossesGame, NoughtsAndCrossesDocument, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>() {
    @Bean
    protected lateinit var document: NoughtsAndCrossesDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class NoughtsAndCrossesHelpActivity : GameHelpActivity<NoughtsAndCrossesGame, NoughtsAndCrossesDocument, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>() {
    @Bean
    protected lateinit var document: NoughtsAndCrossesDocument
    override val doc get() = document
}