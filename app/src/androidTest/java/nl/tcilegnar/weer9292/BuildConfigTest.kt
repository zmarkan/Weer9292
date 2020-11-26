package nl.tcilegnar.weer9292

import org.junit.Assert.assertTrue
import org.junit.Test

abstract class BuildConfigTest : BaseInstrumentedTest() {

    companion object {
        const val APPLICATION_ID_BASE = "nl.tcilegnar.weer9292"
    }

    @Test
    fun buildConfig_ApplicationId_ApplicationIdBaseShouldNeverChange() {
        // Arrange

        // Act
        val actualApplicationId = BuildConfig.APPLICATION_ID

        // Assert
        assertTrue(actualApplicationId.contains(APPLICATION_ID_BASE))
    }

    @Test
    abstract fun buildConfig_ApplicationId_SuffixDependsOnBuildType()

    @Test
    fun buildConfig_ApplicationId_ApplicationIdContainsAppContextPackageName() {
        // Arrange

        // Act
        val actualApplicationId = BuildConfig.APPLICATION_ID

        // Assert
        assertTrue(actualApplicationId.contains(getContext().packageName))
    }

    @Test
    fun failingTest_provesThatOurTestsAreRunning() {
        // Arrange

        // Act

        // Assert
        assertTrue(false)
    }
}
