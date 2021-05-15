package sidev.app.course.dicoding.moviecatalog1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import sidev.app.course.dicoding.moviecatalog1.UnitTestingUtil.waitForValue
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.lib.`val`.SuppressLiteral

class ShowDetailViewModelTest {

    companion object {
        // These props are lazy val so there's no way the value will be changed after initialization.
        private val repo: ShowRepo by lazy { mock(ShowRepo::class.java) }

        private val movieDetail = AppConfig.dummyMovieDetail
        private val tvDetail = AppConfig.dummyTvDetail

        @BeforeClass
        @JvmStatic
        fun initSetup(): Unit = runBlocking {
            `when`(repo.getMovieDetail(any(), anyString())).thenReturn(
                Success(movieDetail)
            )
            `when`(repo.getTvDetail(any(), anyString())).thenReturn(
                Success(tvDetail)
            )
        }
    }

    private lateinit var vm: ShowDetailViewModel
    private lateinit var mockObserver: Observer<ShowDetail> //var cuz it's different Observer on every test case.
    private lateinit var data: Show

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        mockObserver = mock(Observer::class.java) as Observer<ShowDetail>
    }

    @Test
    fun downloadMovieDetail(): Unit = runBlocking {
        data = AppConfig.dummyMovieItem
        vm = ShowDetailViewModel(null, repo, Const.ShowType.MOVIE)
        vm.showDetail.observeForever(mockObserver)
        vm.downloadShowDetail(data.id)

        // Still need this line cuz Observer.onChanged() is invoked async.
        val dataFromCall = vm.showDetail.waitForValue()

        verify(repo).getMovieDetail(null, data.id)
        verify(mockObserver).onChanged(movieDetail)

        assertEquals(movieDetail, dataFromCall)
    }

    @Test
    fun downloadTvDetail(): Unit = runBlocking {
        data = AppConfig.dummyTvItem
        vm = ShowDetailViewModel(null, repo, Const.ShowType.TV)
        vm.showDetail.observeForever(mockObserver)
        vm.downloadShowDetail(data.id)

        // Still need this line cuz Observer.onChanged() is invoked async.
        val dataFromCall = vm.showDetail.waitForValue()

        verify(repo).getTvDetail(null, data.id)
        verify(mockObserver).onChanged(tvDetail)

        assertEquals(tvDetail, dataFromCall)
    }
}