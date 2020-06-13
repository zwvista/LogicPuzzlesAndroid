package com.zwstudio.logicpuzzlesandroid.puzzles.walls

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class WallsMainActivity : GameMainActivity<WallsGame, WallsDocument, WallsGameMove, WallsGameState>() {
    @Bean
    protected lateinit var document: WallsDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        WallsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        WallsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class WallsOptionsActivity : GameOptionsActivity<WallsGame, WallsDocument, WallsGameMove, WallsGameState>() {
    @Bean
    protected lateinit var document: WallsDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class WallsHelpActivity : GameHelpActivity<WallsGame, WallsDocument, WallsGameMove, WallsGameState>() {
    @Bean
    protected lateinit var document: WallsDocument
    override val doc get() = document
}