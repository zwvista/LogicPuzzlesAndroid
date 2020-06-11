package com.zwstudio.logicpuzzlesandroid.common.android

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.common.domain.GameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity
abstract class GameMainActivity<G : Game<G, GM, GS>, GD : GameDocument<G, GM>, GM, GS : GameState> : BaseActivity() {
    abstract fun doc(): GD

    @ViewById
    protected lateinit var tvGame: TextView

    @ViewById
    protected lateinit var btnResumeLevel: Button

    @ViewById
    protected lateinit var btnPrevPage: Button

    @ViewById
    protected lateinit var btnNextPage: Button
    var currentPage = 0
    var countPerPage = 12
    var numPages = 1

    @AfterViews
    protected fun init() {
        val onClickListener = View.OnClickListener { v ->
            doc().selectedLevelID = (v as Button).text.toString()
            resumeGame()
        }
        // http://stackoverflow.com/questions/25905086/multiple-buttons-onclicklistener-android
        for (i in 0 until countPerPage) {
            val resID = resources.getIdentifier("btnLevel" + (i + 1), "id", "com.zwstudio.logicpuzzlesandroid")
            val button = findViewById<Button>(resID)
            button.setOnClickListener(onClickListener)
        }
        numPages = (doc().levels.size + countPerPage - 1) / countPerPage
        val index = List.iterableList(doc().levels).toStream().indexOf { o: GameLevel? -> o.id == doc().selectedLevelID }.orSome(0)
        currentPage = index / countPerPage
        if (numPages == 1) {
            btnPrevPage.visibility = View.INVISIBLE
            btnNextPage.visibility = View.INVISIBLE
        }
        showCurrentPage()
        tvGame.text = doc().gameTitle()
        val toResume = intent.getBooleanExtra("toResume", false)
        if (toResume) resumeGame()
    }

    override fun onResume() {
        super.onResume()
        btnResumeLevel.text = "Resume Level " + doc().selectedLevelID
    }

    private fun showCurrentPage() {
        for (i in 0 until countPerPage) {
            val resID = resources.getIdentifier("btnLevel" + (i + 1), "id", "com.zwstudio.logicpuzzlesandroid")
            val button = findViewById<Button>(resID)
            val index = currentPage * countPerPage + i
            val b = index < doc().levels.size
            button.visibility = if (b) View.VISIBLE else View.INVISIBLE
            if (b) button.text = doc().levels[index].id
        }
    }

    @Click
    protected fun btnPrevPage() {
        currentPage = (currentPage - 1 + numPages) % numPages
        showCurrentPage()
    }

    @Click
    protected fun btnNextPage() {
        currentPage = (currentPage + 1) % numPages
        showCurrentPage()
    }

    @Click
    protected fun btnResumeLevel() {
        resumeGame()
    }

    protected abstract fun resumeGame()

    @Click
    protected fun btnResetAllLevels() {
        yesNoDialog("Do you really want to reset the options?") { doc().resetAllLevels() }
    }
}