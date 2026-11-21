package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.TallyActionType
import com.example.ui.TallyViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun readAppNameFromContext() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Quick Tally", appName)
  }

  @Test
  fun tallyViewModelTracksIncrementsDecrementsHistoryCapAndReset() {
    val vm = TallyViewModel()
    vm.updateNote("Laps")
    vm.selectStep(5)

    repeat(12) { vm.increment() }
    assertEquals(60, vm.count)
    assertEquals(TallyViewModel.MAX_HISTORY_ITEMS, vm.history.size)
    assertEquals(60, vm.history.first().resultingCount)
    assertEquals(TallyActionType.INCREMENT, vm.history.first().actionType)

    vm.decrement()
    assertEquals(55, vm.count)
    assertEquals(TallyActionType.DECREMENT, vm.history.first().actionType)

    vm.reset()
    assertEquals(0, vm.count)
    assertEquals(1, vm.history.size)
    assertEquals(TallyActionType.RESET, vm.history.first().actionType)

    vm.reset()
    assertTrue(vm.history.isEmpty())
  }
}
