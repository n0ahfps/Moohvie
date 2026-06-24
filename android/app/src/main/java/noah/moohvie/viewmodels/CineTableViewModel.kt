package noah.moohvie.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import noah.moohvie.models.CineTableDisplayMode
import noah.moohvie.models.CineTableFilterState
import noah.moohvie.models.WatchedMovie
import noah.moohvie.services.CineTableStore

class CineTableViewModel(application: Application) : AndroidViewModel(application) {
    val store = CineTableStore.getInstance(application)

    var displayMode: CineTableDisplayMode by mutableStateOf(CineTableDisplayMode.GRID)
    var filters: CineTableFilterState by mutableStateOf(CineTableFilterState())

    val displayedMovies: List<WatchedMovie>
        get() = filters.apply(to = store.watchedMovies)

    fun remove(movieId: Int) = store.remove(movieId)

    fun setRating(movieId: Int, rating: Int) = store.setRating(movieId, rating)
}
