package sidev.app.course.dicoding.moviecatalog1.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sidev.app.course.dicoding.moviecatalog1.data.model.ShowDetail
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.data.Success
import sidev.app.course.dicoding.moviecatalog1.data.Failure
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.lib.`val`.SuppressLiteral

class ShowDetailViewModel(
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
        ): ShowDetailViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = ShowDetailViewModel(c, repo, type) as T
            }
        ).get(ShowDetailViewModel::class.java)
    }

    private val mShowDetail: MutableLiveData<ShowDetail> = MutableLiveData()
    val showDetail: LiveData<ShowDetail>
        get()= mShowDetail

    fun downloadShowDetail(id: String, forceDownload: Boolean = false){
        if(!forceDownload && mShowDetail.value != null) return
        cancelJob()
        doOnPreAsyncTask()
        job = GlobalScope.launch(Dispatchers.IO) {
            val result = when(type){
                Const.ShowType.MOVIE -> repo.getMovieDetail(ctx, id)
                Const.ShowType.TV -> repo.getTvDetail(ctx, id)
            }
            when(result){
                is Success -> mShowDetail.postValue(result.data)
                is Failure -> doCallNotSuccess(result.code, result.e)
            }
        }
    }
}