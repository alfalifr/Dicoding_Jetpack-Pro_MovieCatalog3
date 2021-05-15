package sidev.app.course.dicoding.moviecatalog1.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.app.course.dicoding.moviecatalog1.data.Failure
import sidev.lib.`val`.SuppressLiteral

class ShowListViewModel(
    c: Application?,
    private val repo: ShowRepo,
    private val type: Const.ShowType,
): AsyncVm(c) {

    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            c: Application?,
            repo: ShowRepo,
            type: Const.ShowType,
        ): ShowListViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = ShowListViewModel(c, repo, type) as T
            }
        ).get(ShowListViewModel::class.java)
    }

    val showList: LiveData<List<Show>>
        get()= mShowList
    private val mShowList: MutableLiveData<List<Show>> = MutableLiveData()


    fun downloadShowPopularList(forceDownload: Boolean = false){
        if(!forceDownload && mShowList.value != null) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            val result = when(type){
                Const.ShowType.MOVIE -> repo.getPopularMovieList(ctx)
                Const.ShowType.TV -> repo.getPopularTvList(ctx)
            }
            when(result){
                is Success -> mShowList.postValue(result.data)
                is Failure -> doCallNotSuccess(result.code, result.e)
            }
        }
    }
}