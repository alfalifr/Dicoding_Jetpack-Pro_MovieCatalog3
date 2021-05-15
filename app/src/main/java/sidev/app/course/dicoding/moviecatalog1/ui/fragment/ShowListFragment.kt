package sidev.app.course.dicoding.moviecatalog1.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.databinding.PageShowListBinding
import sidev.app.course.dicoding.moviecatalog1.ui.activity.DetailActivity
import sidev.app.course.dicoding.moviecatalog1.ui.adapter.ShowAdp
import sidev.app.course.dicoding.moviecatalog1.util.Const
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.viewmodel.ShowListViewModel
import sidev.lib.android.std.tool.util.`fun`.startAct


class ShowListFragment: Fragment() {
    private lateinit var binding: PageShowListBinding
    private lateinit var adp: ShowAdp
    private lateinit var vm: ShowListViewModel
    private lateinit var type: Const.ShowType
    private lateinit var showRepo: ShowRepo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            type = getSerializable(Const.KEY_TYPE) as Const.ShowType
            showRepo = getSerializable(Const.KEY_REPO) as? ShowRepo ?: AppConfig.defaultShowRepo
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PageShowListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adp = ShowAdp().apply {
            setOnItemClick { _, data ->
                startAct<DetailActivity>(
                    Const.KEY_SHOW to data,
                    Const.KEY_TYPE to type,
                )
            }
        }
        binding.apply {
            rv.apply {
                adapter = adp
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }

        vm = ShowListViewModel.getInstance(this, requireActivity().application, showRepo, type).apply {
            onPreAsyncTask {
                AppConfig.incUiAsync()
                showNoData(false)
                showLoading()
            }
            onCallNotSuccess { code, e ->
                showLoading(false)
                showDataError(true, code, e)
                AppConfig.decUiAsync()
            }
            showList.observe(this@ShowListFragment) {
                adp.dataList = it
                showLoading(false)
                showNoData(it == null || it.isEmpty())
                AppConfig.decUiAsync()
            }
            downloadShowPopularList(forceDownload = true)
        }
    }

    private fun showNoData(show: Boolean = true) {
        showDataAnomaly(show)
        binding.tvNoData.text = getString(R.string.no_data)
    }

    @Suppress("SameParameterValue")
    private fun showDataError(show: Boolean = true, code: Int = -1, e: Throwable? = null) {
        showDataAnomaly(show)
        val eClass = if(e != null) e::class.java.simpleName else "null"
        binding.tvNoData.text = getString(R.string.error_data, "$eClass ($code)", e?.message ?: "null")
    }

    private fun showDataAnomaly(show: Boolean = true) = binding.apply {
        if(show){
            tvNoData.visibility = View.VISIBLE
            rv.visibility = View.GONE
        } else {
            tvNoData.visibility = View.GONE
            rv.visibility = View.VISIBLE
        }
    }

    private fun showLoading(show: Boolean = true) = binding.apply {
        if(show){
            pbLoading.visibility = View.VISIBLE
            rv.visibility = View.GONE
        } else {
            pbLoading.visibility = View.GONE
            rv.visibility = View.VISIBLE
        }
    }
}