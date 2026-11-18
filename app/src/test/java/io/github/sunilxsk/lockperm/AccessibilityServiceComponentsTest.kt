package io.github.sunilxsk.lockperm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AccessibilityServiceComponentsTest {
    private val pkg = "cn.unipus.itest.student"
    private val service = "cn.unipus.asd.service.ASDAccessibilityService"
    private val expected = "$pkg/$service"

    @Test
    fun matchesTheExactComponentCheckedByItest() {
        val value = AccessibilityServiceComponents.merge(null, pkg, listOf(service))
        assertTrue(value.split(':').any { it == expected })
        assertEquals(expected, value)
    }

    @Test
    fun existingEntryFromSamePackageDoesNotSuppressTheRequiredService() {
        val existing = "$pkg/$pkg.OtherService"
        val value = AccessibilityServiceComponents.merge(existing, pkg, listOf(service))
        assertEquals(listOf(existing, expected), value.split(':'))
    }

    @Test
    fun preservesOtherAppsAndDoesNotDuplicateRepeatedReads() {
        val existing = "other.app/other.app.Reader"
        val first = AccessibilityServiceComponents.merge(existing, pkg, listOf(service))
        assertEquals(first, AccessibilityServiceComponents.merge(first, pkg, listOf(service)))
        assertEquals(listOf(existing, expected), first.split(':'))
    }

    @Test
    fun resolvesRelativeAndUnqualifiedManifestNames() {
        val value = AccessibilityServiceComponents.merge(null, "example.app", listOf(".Reader", "Helper"))
        assertEquals("example.app/example.app.Reader:example.app/example.app.Helper", value)
    }

    @Test
    fun packagePrefixCollisionDoesNotCountAsTheRequestedService() {
        val existing = "$pkg.extra/$service"
        assertEquals("$existing:$expected", AccessibilityServiceComponents.merge(existing, pkg, listOf(service)))
    }

    @Test
    fun noDeclaredServiceDoesNotInventAComponent() {
        assertEquals("", AccessibilityServiceComponents.merge(null, pkg, emptyList()))
        assertEquals("other/other.Reader", AccessibilityServiceComponents.merge("other/other.Reader", pkg, emptyList()))
    }
}
