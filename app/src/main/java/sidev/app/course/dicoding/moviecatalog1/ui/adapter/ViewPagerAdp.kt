package sidev.app.course.dicoding.moviecatalog1.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.ui.fragment.ShowListFragment
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.util.Const

class ViewPagerAdp(act: FragmentActivity, private val showRepo: ShowRepo): FragmentStateAdapter(act) {
    private val showTypes = Const.ShowType.values()
    override fun getItemCount(): Int = showTypes.size

    override fun createFragment(position: Int): Fragment = ShowListFragment().apply {
        arguments = Bundle().apply {
            putSerializable(Const.KEY_TYPE, showTypes[position])
            putSerializable(Const.KEY_REPO, showRepo)
        }
    }
}