package sidev.app.course.dicoding.moviecatalog1.util

import androidx.recyclerview.widget.DiffUtil
import sidev.app.course.dicoding.moviecatalog1.data.model.Show

object ShowDiffUtil: DiffUtil.ItemCallback<Show>() {
    override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean = oldItem.id == newItem.id && oldItem.type == newItem.type
    override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean = oldItem == newItem
}