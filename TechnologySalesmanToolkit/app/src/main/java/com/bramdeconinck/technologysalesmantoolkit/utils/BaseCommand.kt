package com.bramdeconinck.technologysalesmantoolkit.utils

/**
 * [BaseCommand] is a sealed class used as an inferred type for SingleLiveEvent.
 * This is to ensure that a single SingleLiveEvent can emit multiple events happening in one ViewModel.
 * More info about sealed classes can be found here: https://kotlinlang.org/docs/reference/sealed-classes.html,
 * but in layman's terms, sealed classes are similar to enums and we use it to emit different events happening in one ViewModel.
 */
sealed class BaseCommand {

    class Error(val error: Int?): BaseCommand()

    class Success(val message: Int?): BaseCommand()

}