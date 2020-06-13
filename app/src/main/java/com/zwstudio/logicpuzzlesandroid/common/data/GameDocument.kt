package com.zwstudio.logicpuzzlesandroid.common.data

import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.home.android.HomeChooseGameActivity
import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

interface GameDocumentInterface {
    val markerOption: Int
    fun setMarkerOption(rec: GameProgress, o: Int)
    val isAllowedObjectsOnly: Boolean
    fun setAllowedObjectsOnly(rec: GameProgress, o: Boolean)
}

@EBean(scope = EBean.Scope.Singleton)
abstract class GameDocument<G : Game<*, *, *>, GM> : GameDocumentInterface {
    val gameID: String
        get() {
            val name = javaClass.simpleName
            return name.substring(0, name.indexOf("Document"))
        }

    val gameTitle: String
        get() {
            val name = gameID
            return HomeChooseGameActivity.name2title[name] ?: name
        }

    var levels = mutableListOf<GameLevel>()
    lateinit var selectedLevelID: String
    val selectedLevelIDSolution get() = "$selectedLevelID Solution"

    var help = listOf<String>()

    @App
    lateinit var app: LogicPuzzlesApplication

    @AfterInject
    fun init() {
        val filename = gameID + ".xml"
        val in_s = app.applicationContext.assets.open("xml/$filename")
        loadXml(in_s)
        in_s.close()
        selectedLevelID = gameProgress().levelID
    }

    private fun loadXml(in_s: InputStream) {
        val pullParserFactory = XmlPullParserFactory.newInstance()
        val parser = pullParserFactory.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(in_s, null)
        parseXML(parser)
    }

    private fun parseXML(parser: XmlPullParser) {
        fun getCdata(strs: List<String>) = strs.filter { !it.isBlank() }
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG)
                when (parser.name) {
                    "level" -> {
                        val id = parser.getAttributeValue(null, "id")
                        val settings = mutableMapOf<String, String>()
                        for (i in 0 until parser.attributeCount)
                            settings[parser.getAttributeName(i)] = parser.getAttributeValue(i)
                        var layout = parser.nextText().split("\n").map { it.replace("\r", "") }
                        layout = getCdata(layout).map { it.replace("`", "") }
                        val level = GameLevel()
                        level.id = id
                        level.layout = layout
                        level.settings = settings
                        levels.add(level)
                    }
                    "help" -> {
                        help = parser.nextText().split("\n").map { it.replace("\r", "") }
                        help = getCdata(help)
                    }
                    else -> {}
                }
            eventType = parser.next()
        }
    }

    fun gameProgress(): GameProgress {
        var rec = app.daoGameProgress.queryBuilder()
            .where().eq("gameID", gameID)
            .queryForFirst()
        if (rec == null) {
            rec = GameProgress()
            rec.gameID = gameID
            rec.levelID = levels[0].id
            app.daoGameProgress.create(rec)
        }
        return rec
    }

    private fun levelProgress(levelID: String): LevelProgress {
        var rec = app.daoLevelProgress.queryBuilder()
            .where().eq("gameID", gameID)
            .and().eq("levelID", levelID).queryForFirst()
        if (rec == null) {
            rec = LevelProgress()
            rec.gameID = gameID
            rec.levelID = levelID
            app.daoLevelProgress.create(rec)
        }
        return rec
    }
    fun levelProgress() = levelProgress(selectedLevelID)
    fun levelProgressSolution() = levelProgress(selectedLevelIDSolution)

    private fun moveProgress(levelID: String): List<MoveProgress> {
        val builder = app.daoMoveProgress.queryBuilder()
        builder.where().eq("gameID", gameID)
            .and().eq("levelID", levelID)
        builder.orderBy("moveIndex", true)
        return builder.query()
    }
    fun moveProgress() = moveProgress(selectedLevelID)
    fun moveProgressSolution() = moveProgress(selectedLevelIDSolution)

    fun levelUpdated(game: Game<*, *, *>) {
        val rec = levelProgress()
        rec.moveIndex = game.moveIndex
        app.daoLevelProgress.update(rec)
    }

    fun gameSolved(game: Game<*, *, *>) {
        val recLP = levelProgress()
        val recLPS = levelProgressSolution()
        if (recLPS.moveIndex == 0 || recLPS.moveIndex > recLP.moveIndex) saveSolution(game)
    }

    fun moveAdded(game: Game<*, *, *>, move: GM) {
        val builder = app.daoMoveProgress.deleteBuilder()
        builder.where().eq("gameID", gameID)
            .and().eq("levelID", selectedLevelID)
            .and().ge("moveIndex", game.moveIndex)
        builder.delete()
        val rec = MoveProgress()
        rec.gameID = gameID
        rec.levelID = selectedLevelID
        rec.moveIndex = game.moveIndex
        saveMove(move, rec)
        app.daoMoveProgress.create(rec)
    }

    protected abstract fun saveMove(move: GM, rec: MoveProgress)
    abstract fun loadMove(rec: MoveProgress): GM
    fun resumeGame() {
        val rec = gameProgress()
        rec.levelID = selectedLevelID
        app.daoGameProgress.update(rec)
    }

    fun clearGame() {
        val builder = app.daoMoveProgress.deleteBuilder()
        builder.where().eq("gameID", gameID)
            .and().eq("levelID", selectedLevelID)
        builder.delete()
        val rec = levelProgress()
        rec.moveIndex = 0
        app.daoLevelProgress.update(rec)
    }

    private fun copyMoves(moveProgressFrom: List<MoveProgress>, levelIDTo: String) {
        val builder = app.daoMoveProgress.deleteBuilder()
        builder.where().eq("gameID", gameID)
            .and().eq("levelID", levelIDTo)
        builder.delete()
        for (recMP in moveProgressFrom) {
            val move = loadMove(recMP)
            val recMPS = MoveProgress()
            recMPS.gameID = gameID
            recMPS.levelID = levelIDTo
            recMPS.moveIndex = recMP.moveIndex
            saveMove(move, recMPS)
            app.daoMoveProgress.create(recMPS)
        }
    }

    fun saveSolution(game: Game<*, *, *>) {
        copyMoves(moveProgress(), selectedLevelIDSolution)
        val rec = levelProgressSolution()
        rec.moveIndex = game.moveIndex
        app.daoLevelProgress.update(rec)
    }

    fun loadSolution() {
        val mps = moveProgressSolution()
        copyMoves(mps, selectedLevelID)
        val rec = levelProgress()
        rec.moveIndex = mps.size
        app.daoLevelProgress.update(rec)
    }

    fun deleteSolution() {
        val builder = app.daoMoveProgress.deleteBuilder()
        builder.where().eq("gameID", gameID)
            .and().eq("levelID", selectedLevelIDSolution)
        builder.delete()
        val rec = levelProgressSolution()
        rec.moveIndex = 0
        app.daoLevelProgress.update(rec)
    }

    fun resetAllLevels() {
        val builder = app.daoMoveProgress.deleteBuilder()
        builder.where().eq("gameID", gameID)
        builder.delete()
        val builder2 = app.daoLevelProgress.deleteBuilder()
        builder2.where().eq("gameID", gameID)
        builder2.delete()
    }

    override val markerOption get() = gameProgress().option1?.toInt() ?: 0

    override fun setMarkerOption(rec: GameProgress, o: Int) {
        rec.option1 = o.toString()
    }

    override val isAllowedObjectsOnly get() = gameProgress().option2?.toBoolean() ?: false

    override fun setAllowedObjectsOnly(rec: GameProgress, o: Boolean) {
        rec.option2 = o.toString()
    }
}