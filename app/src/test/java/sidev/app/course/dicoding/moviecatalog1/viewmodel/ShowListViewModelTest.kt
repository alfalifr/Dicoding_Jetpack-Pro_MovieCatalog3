package sidev.app.course.dicoding.moviecatalog1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import sidev.app.course.dicoding.moviecatalog1.UnitTestingUtil.waitForValue
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.lib.`val`.SuppressLiteral

class ShowListViewModelTest {

    companion object {
        // These props are lazy val so there's no way the value will be changed after initialization.
        private val repo: ShowRepo by lazy { mock(ShowRepo::class.java) }

        private val movie = AppConfig.dummyMovieItem
        private val tv = AppConfig.dummyTvItem

        @BeforeClass
        @JvmStatic
        fun initSetup(): Unit = runBlocking {
            Mockito.`when`(repo.getPopularMovieList(Mockito.any())).thenReturn(
                Success(listOf(movie))
            )
            Mockito.`when`(repo.getPopularTvList(Mockito.any())).thenReturn(
                Success(listOf(tv))
            )
        }
    }

    private lateinit var vm: ShowListViewModel
    private lateinit var mockObserver: Observer<List<Show>> //var cuz it's different Observer on every test case.

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        mockObserver = mock(Observer::class.java) as Observer<List<Show>>
    }

    @Test
    fun downloadPopularMovieList(): Unit = runBlocking {
        vm = ShowListViewModel(null, repo, Const.ShowType.MOVIE)
        vm.showList.observeForever(mockObserver)
        vm.downloadShowPopularList()

        // Still need this line cuz Observer.onChanged() is invoked async.
        val list = vm.showList.waitForValue()

        verify(repo).getPopularMovieList(null)
        verify(mockObserver).onChanged(listOf(movie))

        assertNotNull(list)
        assert(list.isNotEmpty())

        val dataFromList = list.first()
        assertEquals(movie, dataFromList)
    }

    @Test
    fun downloadPopularTvList(): Unit = runBlocking {
        vm = ShowListViewModel(null, repo, Const.ShowType.TV)
        vm.showList.observeForever(mockObserver)
        vm.downloadShowPopularList()

        // Still need this line cuz Observer.onChanged() is invoked async.
        val list = vm.showList.waitForValue()

        verify(repo).getPopularTvList(null)
        verify(mockObserver).onChanged(listOf(tv))

        assertNotNull(list)
        assert(list.isNotEmpty())

        val dataFromList = list.first()
        assertEquals(tv, dataFromList)
    }
}