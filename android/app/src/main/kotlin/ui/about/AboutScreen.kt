package weatherstations.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import weatherstations.BuildConfig
import weatherstations.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.about))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .then(Modifier.padding(15.dp, 0.dp))
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(56.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.weather_stations),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(text = stringResource(R.string.version, BuildConfig.VERSION_NAME))
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            val text = buildAnnotatedString {

                append(stringResource(id = R.string.app_description))

                appendLine()
                appendLine()

                append(stringResource(R.string.the_weather_data_used_in_the_app_comes_from))
                append(" ")

                pushStringAnnotation(tag = "link", annotation = "https://eismoinfo.lt")
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                )) {
                    append(stringResource(R.string.lithuanian_road_administration))
                }
                pop()

                append(".")

                appendLine()
                appendLine()

                append(stringResource(id = R.string.weather_stations))
                append(stringResource(R.string.is_free_and_open_source_application_the_source_code_can_be_found))
                append(" ")

                pushStringAnnotation(tag = "link", annotation = "https://github.com/tunaitis/weatherstations")
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                )) {
                    append(stringResource(R.string.here))
                }
                pop()

                append(". ")

                append(stringResource(R.string.if_you_want_to_report_a_bug_or_request_a_feature_please))

                append(" ")
                pushStringAnnotation(tag = "link", annotation = "https://github.com/tunaitis/weatherstations/issues")
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                )) {
                    append(stringResource(R.string.create_an_issue))
                }
                pop()
                append(" ")

                append(stringResource(R.string.on_the_project_s_github_page))

                appendLine()
                appendLine()

                append(stringResource(R.string.the_app_icon_is_from))
                append(" ")
                pushStringAnnotation(tag = "link", annotation = "https://www.flaticon.com")
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                )) {
                    append("flaticon.com")
                }
                pop()

                append(". ")
            }
            val uriHandler = LocalUriHandler.current

            ClickableText(text = text, style = TextStyle(
                color = LocalContentColor.current,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
            ), onClick = { offset ->
                text.getStringAnnotations(start = offset, end = offset).firstOrNull()?.let {
                    uriHandler.openUri(it.item)
                }
            })

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}