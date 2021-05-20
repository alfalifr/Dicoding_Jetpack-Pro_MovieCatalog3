package sidev.app.course.dicoding.moviecatalog1.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.ui.fragment.ShowListFragment

class ShowListActivity: ShowListAbsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.show_list)
    }

    override fun createFragment(pos: Int): Fragment = ShowListFragment()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.menu_fav -> {
            startActivity(
                Intent(this, ShowFavListActivity::class.java)
            )
            true
        }
        else -> false
    }
}