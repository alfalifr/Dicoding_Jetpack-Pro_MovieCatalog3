package sidev.app.course.dicoding.moviecatalog1.ui.fragment

import android.os.Bundle
import androidx.arch.core.util.Function
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import sidev.app.course.dicoding.moviecatalog1.data.db.ShowFavDb
import sidev.app.course.dicoding.moviecatalog1.data.model.Show
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowFavRepo
import sidev.app.course.dicoding.moviecatalog1.ui.activity.DetailActivity
import sidev.app.course.dicoding.moviecatalog1.ui.adapter.ShowFavAdp
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.viewmodel.ShowFavViewModel
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.startAct

class ShowFavListFragment: ShowListAbsFragment() {
    private lateinit var showRepo: ShowFavRepo

    override val adp: ShowFavAdp by lazy {
        ShowFavAdp().apply {
            setOnItemClick { _, data ->
                startAct<DetailActivity>(
                    Const.KEY_SHOW to data,
                    Const.KEY_TYPE to type,
                )
            }
        }
    }
    override val vm: ShowFavViewModel by lazy {
        ShowFavViewModel.getInstance(this, showRepo, type)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showRepo = ShowFavRepo(ShowFavDb.getInstance(requireContext()).dao())
    }

    override fun onAfterVmConfigured() {
        vm.apply {
            var data2: PagingData<Show>? = null
            adp.addLoadStateListener {
                showNoData(data2 == null || adp.itemCount == 0)
                loge("adp.itemCount= ${adp.itemCount}")
            }
            showSrc.observe(this@ShowFavListFragment) { data ->
                data2 = data
                adp.submitData(lifecycle, data)
                showLoading(false)
                AppConfig.decUiAsync()
            }
            queryFavList()
        }
    }
}