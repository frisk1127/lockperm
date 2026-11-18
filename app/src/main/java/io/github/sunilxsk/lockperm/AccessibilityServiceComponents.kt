package io.github.sunilxsk.lockperm

internal object AccessibilityServiceComponents {
    fun merge(current: String?, packageName: String, serviceNames: List<String>): String {
        val components = current.orEmpty().split(':').filter { it.isNotBlank() }.toMutableSet()
        for (name in serviceNames) {
            if (name.isBlank()) continue
            val className = when {
                name.startsWith('.') -> packageName + name
                '.' !in name -> "$packageName.$name"
                else -> name
            }
            components.add("$packageName/$className")
        }
        return components.joinToString(":")
    }
}
