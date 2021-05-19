package sidev.app.course.dicoding.moviecatalog1.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.ui.fragment.ShowFavListFragment

class ShowFavListActivity: ShowListAbsActivity() {
    override fun createFragment(pos: Int): Fragment = ShowFavListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.show_fav_list)
    }
}