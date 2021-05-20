package sidev.app.course.dicoding.moviecatalog1.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.*
import sidev.app.course.dicoding.moviecatalog1.AndroidTestingUtil
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.data.datasource.ShowRemoteRetrofitSource
import sidev.app.course.dicoding.moviecatalog1.data.db.ShowFavDao
import sidev.app.course.dicoding.moviecatalog1.data.db.ShowFavDb
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowApiRepo
import sidev.app.course.dicoding.moviecatalog1.ui.activity.ShowListActivity
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig

class ShowFavActivityTest {

    @get:Rule
    val actRule = ActivityScenarioRule(ShowListActivity::class.java)

    companion object {
        private val db: ShowFavDb by lazy {
            val ctx = ApplicationProvider.getApplicationContext<Context>()
            ShowFavDb.getInstance(ctx)
        }
        private val dao: ShowFavDao by lazy { db.dao() }

        @BeforeClass
        @JvmStatic
        fun setup() {

        }
    }


    @Before
    fun setupEach(){
        AppConfig.isUiAsyncTest = true
        AppConfig.defaultShowRepo = ShowApiRepo(ShowRemoteRetrofitSource)
        IdlingRegistry.getInstance().register(AppConfig.idlingRes)
    }

    @After
    fun finish(){
        IdlingRegistry.getInstance().unregister(AppConfig.idlingRes)
        AppConfig.resetDefautlShowRepo()
    }

    @Test
    fun showFavTest() {
        dao.clear()

        //Click item 1 to go to detail
        Espresso.onView(ViewMatchers.withId(R.id.rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click())
        )

        //Press to like the show
        Espresso.onView(ViewMatchers.withId(R.id.btn_fav)).perform(ViewActions.click())

        //Back to main list
        Espresso.pressBack()

        //Go to favourite list
        Espresso.onView(ViewMatchers.withId(R.id.menu_fav)).perform(ViewActions.click())

        //Assert item 1 is displayed
        Espresso.onView(ViewMatchers.withId(R.id.rv)).check(
            AndroidTestingUtil.RecyclerViewAssertion.isChildInPositionMatched(
                0, ViewMatchers.isDisplayed()
            )
        )

        //Enter the detail page
        Espresso.onView(ViewMatchers.withId(R.id.rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click())
        )

        //Dislike the show
        Espresso.onView(ViewMatchers.withId(R.id.btn_fav)).perform(ViewActions.click())

        //Back to favourite list
        Espresso.pressBack()

        //Assert no data is displayed
        Espresso.onView(ViewMatchers.withId(R.id.rv)).check(
            AndroidTestingUtil.RecyclerViewAssertion.isChildInPositionMatched(
                0, AndroidTestingUtil.ViewMatchers.isNotDisplayed()
            )
        )

        //Assert 'No Data' TextView is displayed
        Espresso.onView(ViewMatchers.withId(R.id.tv_no_data)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(R.string.no_data)
            )
        )

        //Assert loading bar is not displayed
        Espresso.onView(ViewMatchers.withId(R.id.pb)).check(
            ViewAssertions.matches(
                AndroidTestingUtil.ViewMatchers.isNotDisplayed()
            )
        )
    }
}