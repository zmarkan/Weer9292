package nl.tcilegnar.weer9292

import org.junit.Assert.assertEquals

class BuildConfigTestAcceptance : BuildConfigTest() {

    override fun buildConfig_ApplicationId_SuffixDependsOnBuildType() {
        // Arrange

        // Act
        val actualApplicationId = BuildConfig.APPLICATION_ID

        // Assert
        assertEquals("$APPLICATION_ID_BASE.accp", actualApplicationId)
    }
}
