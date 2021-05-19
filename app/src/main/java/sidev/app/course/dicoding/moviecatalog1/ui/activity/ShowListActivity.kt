package sidev.app.course.dicoding.moviecatalog1.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import sidev.app.course.dicoding.moviecatalog1.R
import sidev.app.course.dicoding.moviecatalog1.data.repository.ShowRepo
import sidev.app.course.dicoding.moviecatalog1.ui.fragment.ShowListFragment
import sidev.app.course.dicoding.moviecatalog1.util.AppConfig
import sidev.app.course.dicoding.moviecatalog1.util.Const

class ShowListActivity: ShowListAbsActivity() {
    private lateinit var showRepo: ShowRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.show_list)
    }

    override fun onGetExtras(intent: Intent) {
        showRepo = intent.getSerializableExtra(Const.KEY_REPO) as? ShowRepo ?: AppConfig.defaultShowRepo
    }

    override fun createFragment(pos: Int): Fragment = ShowListFragment().apply {
        arguments = Bundle().apply {
            putSerializable(Const.KEY_REPO, showRepo)
        }
    }

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