package com.gabrielbmoro.programmingchallenge.ui.common.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MovieCardInformation(
    title: String,
    description: String,
    votes: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.Start),
            color = MaterialTheme.colors.onSurface,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        FiveStars(
            votes = votes,
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.body1.copy(
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.Start),
            color = MaterialTheme.colors.onSurface,
            maxLines = 6,
            overflow = TextOverflow.Ellipsis
        )
    }
}