package kmch.vaccine.form.presentation.screen.admin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kmch.vaccine.form.domain.model.RegistrationAnswerDetail
import kmch.vaccine.form.presentation.localization.LocalStrings

@Composable
fun QuestionAnswerItem(answer: RegistrationAnswerDetail) {
    val strings = LocalStrings.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        Text(
            text = answer.questionText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        val answerText = if (answer.answer) strings.answerYes else strings.answerNo
        val answerColor = if (answer.answer) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = answerText,
                color = answerColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            if (!answer.remark.isNullOrBlank()) {
                Text(
                    text = strings.answerRemark(answer.remark),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
