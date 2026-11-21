package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.BuildConfig
import com.example.R
import com.example.ui.theme.CounterDisplayTextStyle
import com.example.ui.theme.QuickTallyTheme
import com.example.ui.theme.TabularLabelTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickTallyScreen(
  viewModel: TallyViewModel = viewModel(),
  versionName: String = BuildConfig.VERSION_NAME,
) {
  val haptics = LocalHapticFeedback.current

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.background,
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = { QuickTallyTopBar(versionName = versionName) },
  ) { innerPadding ->
    BoxWithConstraints(
      modifier =
        Modifier.fillMaxSize()
          .padding(innerPadding)
          .padding(horizontal = 16.dp, vertical = 12.dp),
      contentAlignment = Alignment.TopCenter,
    ) {
      val isExpandedLayout = maxWidth >= 700.dp

      if (isExpandedLayout) {
        Row(
          modifier = Modifier.fillMaxSize().widthIn(max = 1040.dp),
          horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
          Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            PrimaryCounterCard(
              count = viewModel.count,
              selectedStep = viewModel.selectedStep,
              note = viewModel.note,
              lastDirection = viewModel.lastDirection,
              sessionActionCount = viewModel.sessionActionCount,
              onNoteChange = viewModel::updateNote,
              onClearNote = viewModel::clearNote,
            )
            StepSelectorRow(
              steps = viewModel.availableSteps,
              selectedStep = viewModel.selectedStep,
              onSelectStep = { step ->
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                viewModel.selectStep(step)
              },
            )
            CounterControls(
              selectedStep = viewModel.selectedStep,
              canReset = viewModel.count != 0 || viewModel.history.isNotEmpty(),
              onIncrement = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                viewModel.increment()
              },
              onDecrement = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                viewModel.decrement()
              },
              onReset = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.reset()
              },
            )
          }

          RecentHistorySection(
            history = viewModel.history,
            onClearHistory = viewModel::clearHistory,
            modifier = Modifier.weight(0.9f).fillMaxHeight(),
          )
        }
      } else {
        Column(
          modifier = Modifier.fillMaxSize().widthIn(max = 600.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          PrimaryCounterCard(
            count = viewModel.count,
            selectedStep = viewModel.selectedStep,
            note = viewModel.note,
            lastDirection = viewModel.lastDirection,
            sessionActionCount = viewModel.sessionActionCount,
            onNoteChange = viewModel::updateNote,
            onClearNote = viewModel::clearNote,
          )

          StepSelectorRow(
            steps = viewModel.availableSteps,
            selectedStep = viewModel.selectedStep,
            onSelectStep = { step ->
              haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
              viewModel.selectStep(step)
            },
          )

          CounterControls(
            selectedStep = viewModel.selectedStep,
            canReset = viewModel.count != 0 || viewModel.history.isNotEmpty(),
            onIncrement = {
              haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
              viewModel.increment()
            },
            onDecrement = {
              haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
              viewModel.decrement()
            },
            onReset = {
              haptics.performHapticFeedback(HapticFeedbackType.LongPress)
              viewModel.reset()
            },
          )

          RecentHistorySection(
            history = viewModel.history,
            onClearHistory = viewModel::clearHistory,
            modifier = Modifier.fillMaxWidth().weight(1f),
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickTallyTopBar(versionName: String) {
  TopAppBar(
    colors =
      TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
      ),
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Box(
          modifier =
            Modifier.size(30.dp)
              .clip(RoundedCornerShape(9.dp))
              .background(MaterialTheme.colorScheme.surface)
              .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.65f),
                shape = RoundedCornerShape(9.dp),
              ),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = stringResource(R.string.app_logo_desc),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp),
          )
        }

        Text(
          text = stringResource(R.string.app_name),
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onBackground,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    },
    actions = {
      Surface(
        modifier = Modifier.padding(end = 16.dp).testTag("version_badge"),
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
      ) {
        Text(
          text = stringResource(R.string.version_badge_format, versionName),
          style =
            MaterialTheme.typography.labelMedium.copy(
              fontFeatureSettings = "tnum",
              fontWeight = FontWeight.SemiBold,
            ),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        )
      }
    },
  )
}

@Composable
private fun PrimaryCounterCard(
  count: Int,
  selectedStep: Int,
  note: String,
  lastDirection: Int,
  sessionActionCount: Int,
  onNoteChange: (String) -> Unit,
  onClearNote: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val glowTargetColor =
    when {
      lastDirection > 0 -> MaterialTheme.colorScheme.primary
      lastDirection < 0 -> MaterialTheme.colorScheme.error
      else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
    }

  val animatedGlowColor by
    animateColorAsState(
      targetValue = glowTargetColor,
      animationSpec = tween(durationMillis = 280),
      label = "counterGlowColor",
    )

  Card(
    modifier = modifier.fillMaxWidth().testTag("primary_counter_card"),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  ) {
    Box(
      modifier =
        Modifier.fillMaxWidth().drawBehind {
          val glowRadius = size.maxDimension * 0.72f
          drawCircle(
            brush =
              Brush.radialGradient(
                colors = listOf(animatedGlowColor.copy(alpha = 0.14f), Color.Transparent),
                center = Offset(size.width * 0.5f, size.height * 0.62f),
                radius = glowRadius,
              ),
            radius = glowRadius,
            center = Offset(size.width * 0.5f, size.height * 0.62f),
          )
        }
    ) {
      Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        OutlinedTextField(
          value = note,
          onValueChange = onNoteChange,
          modifier = Modifier.fillMaxWidth().testTag("counter_note_input"),
          singleLine = true,
          placeholder = {
            Text(
              text = stringResource(R.string.counter_label_hint),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Outlined.EditNote,
              contentDescription = stringResource(R.string.counter_label_icon_desc),
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp),
            )
          },
          trailingIcon =
            if (note.isNotEmpty()) {
              {
                IconButton(
                  onClick = onClearNote,
                  modifier = Modifier.testTag("clear_note_button"),
                ) {
                  Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(R.string.counter_label_clear),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                  )
                }
              }
            } else {
              null
            },
          textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
          shape = RoundedCornerShape(14.dp),
          colors =
            OutlinedTextFieldDefaults.colors(
              focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
              unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
              focusedBorderColor = MaterialTheme.colorScheme.primary,
              unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
              focusedTextColor = MaterialTheme.colorScheme.onSurface,
              unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
              cursorColor = MaterialTheme.colorScheme.primary,
            ),
          keyboardOptions =
            KeyboardOptions(
              capitalization = KeyboardCapitalization.Sentences,
              imeAction = ImeAction.Done,
            ),
        )

        Spacer(modifier = Modifier.height(2.dp))

        AnimatedContent(
          targetState = count,
          transitionSpec = {
            val movingUp = targetState >= initialState
            val slideIn =
              slideInVertically(
                animationSpec =
                  spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow,
                  ),
                initialOffsetY = { height -> if (movingUp) height / 3 else -height / 3 },
              )
            val slideOut =
              slideOutVertically(
                animationSpec = tween(durationMillis = 120),
                targetOffsetY = { height -> if (movingUp) -height / 3 else height / 3 },
              )
            (slideIn + fadeIn(tween(140)) + scaleIn(initialScale = 0.94f, animationSpec = tween(140)))
              .togetherWith(slideOut + fadeOut(tween(100)))
          },
          label = "counterValueTransition",
        ) { targetCount ->
          Text(
            text = targetCount.toString(),
            style = CounterDisplayTextStyle,
            color =
              when {
                targetCount < 0 -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurface
              },
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth().testTag("counter_value_text"),
          )
        }

        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Surface(
            shape = RoundedCornerShape(999.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            border =
              BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              Box(
                modifier =
                  Modifier.size(7.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
              )
              Text(
                text = stringResource(R.string.active_step_badge, selectedStep),
                style = TabularLabelTextStyle,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.testTag("active_step_badge"),
              )
            }
          }

          if (sessionActionCount > 0) {
            Surface(
              shape = RoundedCornerShape(999.dp),
              color = MaterialTheme.colorScheme.surfaceVariant,
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            ) {
              val actionLabel =
                if (sessionActionCount == 1) {
                  stringResource(R.string.session_tap_single)
                } else {
                  stringResource(R.string.session_taps_format, sessionActionCount)
                }
              Text(
                text = actionLabel,
                style =
                  MaterialTheme.typography.labelMedium.copy(fontFeatureSettings = "tnum"),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun StepSelectorRow(
  steps: List<Int>,
  selectedStep: Int,
  onSelectStep: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(
      text = stringResource(R.string.step_selector_title),
      style = MaterialTheme.typography.titleSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(horizontal = 2.dp),
    ) {
      items(items = steps, key = { it }) { step ->
        val isSelected = step == selectedStep
        FilterChip(
          selected = isSelected,
          onClick = { onSelectStep(step) },
          modifier =
            Modifier.minimumInteractiveComponentSize().testTag("step_chip_$step"),
          label = {
            Text(
              text = stringResource(R.string.step_chip_format, step),
              style = TabularLabelTextStyle,
            )
          },
          leadingIcon =
            if (isSelected) {
              {
                Icon(
                  imageVector = Icons.Rounded.Check,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                )
              }
            } else {
              null
            },
          shape = RoundedCornerShape(12.dp),
          colors =
            FilterChipDefaults.filterChipColors(
              containerColor = MaterialTheme.colorScheme.surface,
              labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
              selectedLeadingIconColor = MaterialTheme.colorScheme.primary,
            ),
          border =
            FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = isSelected,
              borderColor = MaterialTheme.colorScheme.outlineVariant,
              selectedBorderColor = MaterialTheme.colorScheme.primary,
              borderWidth = 1.dp,
              selectedBorderWidth = 1.5.dp,
            ),
        )
      }
    }
  }
}

@Composable
private fun CounterControls(
  selectedStep: Int,
  canReset: Boolean,
  onIncrement: () -> Unit,
  onDecrement: () -> Unit,
  onReset: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Button(
      onClick = onIncrement,
      modifier = Modifier.fillMaxWidth().height(64.dp).testTag("increment_button"),
      shape = RoundedCornerShape(18.dp),
      colors =
        ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
      elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
    ) {
      Icon(
        imageVector = Icons.Rounded.Add,
        contentDescription = stringResource(R.string.increment_button_desc, selectedStep),
        modifier = Modifier.size(26.dp),
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = stringResource(R.string.increment_button_format, selectedStep),
        style =
          MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontFeatureSettings = "tnum",
          ),
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      OutlinedButton(
        onClick = onDecrement,
        modifier = Modifier.weight(1f).height(56.dp).testTag("decrement_button"),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors =
          ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
          ),
      ) {
        Icon(
          imageVector = Icons.Rounded.Remove,
          contentDescription = stringResource(R.string.decrement_button_desc, selectedStep),
          modifier = Modifier.size(22.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = stringResource(R.string.decrement_button_format, selectedStep),
          style =
            MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold,
              fontFeatureSettings = "tnum",
            ),
        )
      }

      TextButton(
        onClick = onReset,
        enabled = canReset,
        modifier = Modifier.height(56.dp).testTag("reset_button"),
        shape = RoundedCornerShape(16.dp),
        colors =
          ButtonDefaults.textButtonColors(
            containerColor =
              if (canReset) {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.72f)
              } else {
                MaterialTheme.colorScheme.surface
              },
            contentColor = MaterialTheme.colorScheme.error,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
          ),
        contentPadding = PaddingValues(horizontal = 20.dp),
      ) {
        Icon(
          imageVector = Icons.Rounded.Refresh,
          contentDescription = stringResource(R.string.reset_button_desc),
          modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = stringResource(R.string.reset_button_label),
          style = MaterialTheme.typography.labelLarge,
        )
      }
    }
  }
}

@Composable
private fun RecentHistorySection(
  history: List<TallyLogEntry>,
  onClearHistory: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Text(
          text = stringResource(R.string.recent_history_title),
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
          shape = RoundedCornerShape(999.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
          Text(
            text =
              stringResource(
                R.string.history_count_format,
                history.size,
                TallyViewModel.MAX_HISTORY_ITEMS,
              ),
            style =
              MaterialTheme.typography.labelSmall.copy(fontFeatureSettings = "tnum"),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
          )
        }
      }

      if (history.isNotEmpty()) {
        TextButton(
          onClick = onClearHistory,
          modifier = Modifier.minimumInteractiveComponentSize().testTag("clear_history_button"),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        ) {
          Text(
            text = stringResource(R.string.clear_history_label),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }

    if (history.isEmpty()) {
      EmptyHistoryCard(modifier = Modifier.fillMaxWidth())
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxWidth().weight(1f).testTag("history_list"),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 8.dp),
      ) {
        items(items = history, key = { it.id }) { entry ->
          HistoryLogCard(entry = entry)
        }
      }
    }
  }
}

@Composable
private fun HistoryLogCard(entry: TallyLogEntry) {
  val (icon, iconTint, badgeContainer, iconDescription) =
    when (entry.actionType) {
      TallyActionType.INCREMENT ->
        LogBadgeVisuals(
          icon = Icons.Rounded.Add,
          iconTint = MaterialTheme.colorScheme.secondary,
          containerColor = MaterialTheme.colorScheme.secondaryContainer,
          description = stringResource(R.string.history_increment_desc),
        )
      TallyActionType.DECREMENT ->
        LogBadgeVisuals(
          icon = Icons.Rounded.Remove,
          iconTint = MaterialTheme.colorScheme.error,
          containerColor = MaterialTheme.colorScheme.errorContainer,
          description = stringResource(R.string.history_decrement_desc),
        )
      TallyActionType.RESET ->
        LogBadgeVisuals(
          icon = Icons.Rounded.Refresh,
          iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
          containerColor = MaterialTheme.colorScheme.surfaceVariant,
          description = stringResource(R.string.history_reset_desc),
        )
    }

  val actionText =
    when (entry.actionType) {
      TallyActionType.INCREMENT ->
        stringResource(R.string.log_increment_format, entry.stepDelta, entry.resultingCount)
      TallyActionType.DECREMENT ->
        stringResource(R.string.log_decrement_format, entry.stepDelta, entry.resultingCount)
      TallyActionType.RESET -> stringResource(R.string.log_reset_text)
    }

  Card(
    modifier = Modifier.fillMaxWidth().testTag("history_item_${entry.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Box(
        modifier = Modifier.size(32.dp).clip(CircleShape).background(badgeContainer),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = icon,
          contentDescription = iconDescription,
          tint = iconTint,
          modifier = Modifier.size(18.dp),
        )
      }

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp),
      ) {
        Text(
          text = actionText,
          style = TabularLabelTextStyle,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        if (entry.noteSnapshot.isNotEmpty()) {
          Text(
            text = entry.noteSnapshot,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }

      Text(
        text = entry.timestamp,
        style = MaterialTheme.typography.labelSmall.copy(fontFeatureSettings = "tnum"),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@Composable
private fun EmptyHistoryCard(modifier: Modifier = Modifier) {
  Card(
    modifier = modifier.testTag("empty_history_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 18.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Box(
        modifier =
          Modifier.size(38.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Outlined.History,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(20.dp),
        )
      }
      Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
          text = stringResource(R.string.empty_history_title),
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = stringResource(R.string.empty_history_subtitle),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

private data class LogBadgeVisuals(
  val icon: androidx.compose.ui.graphics.vector.ImageVector,
  val iconTint: Color,
  val containerColor: Color,
  val description: String,
)

@Preview(showBackground = true)
@Composable
private fun QuickTallyScreenPreview() {
  QuickTallyTheme {
    val previewVm =
      TallyViewModel().apply {
        updateNote("Laps")
        selectStep(5)
        increment()
        increment()
        increment()
      }
    QuickTallyScreen(viewModel = previewVm, versionName = "1.0.0")
  }
}
