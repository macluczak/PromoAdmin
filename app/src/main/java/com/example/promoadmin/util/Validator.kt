package com.example.promoadmin.util

class Validator<T>(private val validationObject: ValidationObject<T>) {

    private val rules: MutableList<(T) -> Boolean> = mutableListOf()

    fun addRule(rule: (T) -> Boolean): Validator<T> {
        rules.add(rule)
        return this
    }

    fun validate(): Boolean {
        val value = validationObject.value
        val isValid = rules.all { it(value) }

        if (!isValid) {
            validationObject.setError("Invalid value")
        }

        return isValid
    }
}

class ValidationObject<T>(var value: T) {
    var error: String? = null

    @JvmName("setValidationError")
    fun setError(errorMessage: String) {
        error = errorMessage
    }

    fun clearError() {
        error = null
    }
}
