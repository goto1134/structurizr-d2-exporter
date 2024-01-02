package io.github.goto1134.structurizr.export.d2

enum class AnimationType {
    NO,
    D2,
    FRAMES;

    companion object {
        fun get(value: String?) = entries.firstOrNull {
            it.name.equals(value, ignoreCase = true)
        }

        fun getOrDefault(value: String?, default: AnimationType) = get(value) ?: default
    }
}
