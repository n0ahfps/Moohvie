package noah.moohvie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import noah.moohvie.models.Movie
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.LocalAccentColor
import noah.moohvie.ui.theme.MooTaupe

@Composable
fun SwipeCardView(movie: Movie, posterHeight: Int = 420, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(1.5.dp, MooBeige, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp)),
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(posterHeight.dp)
                .background(MooBeige),
        )

        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                movie.title,
                style = MaterialTheme.typography.titleMedium,
                color = MooDark,
                maxLines = 1,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = LocalAccentColor.current)
                Text(
                    String.format("%.1f", movie.voteAverage),
                    color = MooDark,
                    style = MaterialTheme.typography.bodyMedium,
                )
                movie.releaseDate?.take(4)?.let { year ->
                    Text(" • $year", color = MooTaupe, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Text(
                movie.overview,
                style = MaterialTheme.typography.bodySmall,
                color = MooTaupe,
                maxLines = 2,
            )
        }
    }
}
