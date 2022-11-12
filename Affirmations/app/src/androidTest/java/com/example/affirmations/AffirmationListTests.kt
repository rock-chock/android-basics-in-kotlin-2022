package com.example.affirmations


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Allow the test to execute on the device or emulator via adding an Instrumentation test runner
@RunWith(AndroidJUnit4::class)
class AffirmationListTests {

    // Launch MainActivity
    // [@get:Rule] specifies that the subsequent rule (=launching an activity), should execute before every test in the class.
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Check if the list is scrollable and that it matches the expected test
     */
    // Annotate the test function with @Test
    @Test
    fun scroll_to_position() {
        // [withId()] returns a ViewMatcher (= the UI component with the specified ID).
        // [onView] returns a ViewInteraction (= object that we can interact with via tests).
        // [perform] takes a ViewAction object.
        onView(withId(R.id.recycler_view))
            .perform(
                // [RecyclerViewActions] class lets your tests take actions on a RecyclerView.
                // [scrollToPosition()] scrolls to a specified position, returns a Generic(=all items in the RecyclerView).
                // Items in RecyclerView are ViewHolders, so we place so we specify it as <RecyclerView.ViewHolder>.
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(9))


        // Ensure that the UI is displaying the expected information.
        // [withText] identifies a UI component based on the text it displays.
        onView(withText(R.string.affirmation10))
            .check(matches(isDisplayed()))
    }

}