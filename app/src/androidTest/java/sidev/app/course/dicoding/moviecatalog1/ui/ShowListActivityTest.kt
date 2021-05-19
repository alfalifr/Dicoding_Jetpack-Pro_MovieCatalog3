package sidev.app.course.dicoding.moviecatalog1.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sidev.app.course.dicoding.moviecatalog1.AndroidTestingUtil
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.data.datasource.ShowRemoteRetrofitSource
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowApiRepo
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowEmptyRepo
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowErrorRepo
import sidev.app.course.dicoding.moviecatalog1.ui.activity.ShowListActivity
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig

class ShowListActivityTest {

    @get:Rule
    val actRule = ActivityScenarioRule(ShowListActivity::class.java)

    @Before
    fun setup(){
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
    fun getShowList(){
        onView(withId(R.id.rv)).apply {
            // Assert RecyclerView is displayed and not empty
            check(
                AndroidTestingUtil.RecyclerViewAssertion.isChildInPositionDisplayed(
                    0, ViewMatchers.isDisplayed()
                )
            )
            // Assert first item title is displayed and not template
            val strTitle = ApplicationProvider.getApplicationContext<Context>().getString(R.string.title)
            check(
                AndroidTestingUtil.RecyclerViewAssertion.isChildIdInPositionDisplayed(
                    0, R.id.tv_title,
                    AndroidTestingUtil.ViewMatchers.textMatchesAndDisplayed {
                        it.isNotBlank() && it != strTitle
                    }
                )
            )
            // Assert first item release date is displayed and not template
            val relDatTitle = ApplicationProvider.getApplicationContext<Context>().getString(R.string.release_date)
            check(
                AndroidTestingUtil.RecyclerViewAssertion.isChildIdInPositionDisplayed(
                    0, R.id.tv_release,
                    AndroidTestingUtil.ViewMatchers.textMatchesAndDisplayed {
                        it.isNotBlank() && it != relDatTitle
                    }
                )
            )
        }
        // Assert loading progress bar should be gone.
        onView(withId(R.id.pb_loading)).check(
            ViewAssertions.matches(
                AndroidTestingUtil.ViewMatchers.isNotDisplayed()
            )
        )
        // Assert no data TextView should be gone.
        onView(withId(R.id.tv_no_data)).check(
            ViewAssertions.matches(
                AndroidTestingUtil.ViewMatchers.isNotDisplayed()
            )
        )
    }

    @Test
    fun getShowListOnError(){
        AppConfig.defaultShowRepo = ShowErrorRepo
        // Assert RecyclerView should be gone.
        onView(withId(R.id.rv)).check(
            ViewAssertions.matches(
                AndroidTestingUtil.ViewMatchers.isNotDisplayed()
            )
        )
        // Assert loading progress should be gone.
        onView(withId(R.id.pb_loading)).check(
            ViewAssertions.matches(
                AndroidTestingUtil.ViewMatchers.isNotDisplayed()
            )
        )
        // Assert no data TextView is displayed with text starts with 'Error:' and contains 'cause:'.
        onView(withId(R.id.tv_no_data)).check(
            ViewAssertions.matches(
                AndroidTestingUtil.ViewMatchers.textMatches {
                    it.startsWith("Error:")
                            && it.contains("cause:")
                }
            )
        )
    }

    @Test
    fun getShowListOnNoData(){
        AppConfig.defaultShowRepo = ShowEmptyRepo
        // Assert RecyclerView should be gone.
        onView(withId(R.id.rv)).check(
            ViewAssertions.matches(
                AndroidTestingUtil.ViewMatchers.isNotDisplayed()
            )
        )
        // Assert loading progress should be gone.
        onView(withId(R.id.pb_loading)).check(
            ViewAssertions.matches(
                AndroidTestingUtil.ViewMatchers.isNotDisplayed()
            )
        )
        // Assert no data TextView is displayed with text same as R.string.no_data.
        val strNoData = ApplicationProvider.getApplicationContext<Context>().getString(R.string.no_data)
        onView(withId(R.id.tv_no_data)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(strNoData)
            )
        )
    }
}