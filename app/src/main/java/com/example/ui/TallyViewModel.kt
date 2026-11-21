package com.example.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class TallyActionType {
  INCREMENT,
  DECREMENT,
  RESET,
}

data class TallyLogEntry(
  val id: Long,
  val actionType: TallyActionType,
  val stepDelta: Int,
  val resultingCount: Int,
  val noteSnapshot: String,
  val timestamp: String,
)

class TallyViewModel : ViewModel() {

  val availableSteps: List<Int> = listOf(1, 5, 10)

  var count: Int by mutableIntStateOf(0)
    private set

  var selectedStep: Int by mutableIntStateOf(1)
    private set

  var note: String by mutableStateOf("")
    private set

  // +1 for increment, -1 for decrement, 0 for reset/initial
  var lastDirection: Int by mutableIntStateOf(0)
    private set

  var sessionActionCount: Int by mutableIntStateOf(0)
    private set

  private val _history = mutableStateListOf<TallyLogEntry>()
  val history: SnapshotStateList<TallyLogEntry> = _history

  private var nextEntryId: Long = 1L

  fun increment() {
    val updated = count + selectedStep
    count = updated
    lastDirection = 1
    sessionActionCount += 1
    recordEntry(
      actionType = TallyActionType.INCREMENT,
      stepDelta = selectedStep,
      resultingCount = updated,
    )
  }

  fun decrement() {
    val updated = count - selectedStep
    count = updated
    lastDirection = -1
    sessionActionCount += 1
    recordEntry(
      actionType = TallyActionType.DECREMENT,
      stepDelta = selectedStep,
      resultingCount = updated,
    )
  }

  fun selectStep(step: Int) {
    if (step in availableSteps) {
      selectedStep = step
    }
  }

  fun updateNote(newNote: String) {
    note = newNote.take(MAX_NOTE_LENGTH)
  }

  fun clearNote() {
    note = ""
  }

  fun reset() {
    val hadNonZeroCount = count != 0
    val hadEntries = _history.isNotEmpty()
    val wasAlreadyResetMarker =
      !hadNonZeroCount &&
        _history.size == 1 &&
        _history.first().actionType == TallyActionType.RESET

    count = 0
    lastDirection = 0
    sessionActionCount = 0
    _history.clear()

    if (hadNonZeroCount || (hadEntries && !wasAlreadyResetMarker)) {
      recordEntry(
        actionType = TallyActionType.RESET,
        stepDelta = 0,
        resultingCount = 0,
      )
    }
  }

  fun clearHistory() {
    _history.clear()
  }

  private fun recordEntry(
    actionType: TallyActionType,
    stepDelta: Int,
    resultingCount: Int,
  ) {
    val entry =
      TallyLogEntry(
        id = nextEntryId++,
        actionType = actionType,
        stepDelta = stepDelta,
        resultingCount = resultingCount,
        noteSnapshot = note.trim(),
        timestamp = formatCurrentTime(),
      )
    _history.add(0, entry)
    if (_history.size > MAX_HISTORY_ITEMS) {
      _history.removeAt(_history.lastIndex)
    }
  }

  private fun formatCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.US)
    return formatter.format(Date())
  }

  companion object {
    const val MAX_HISTORY_ITEMS = 10
    const val MAX_NOTE_LENGTH = 40
  }
}
