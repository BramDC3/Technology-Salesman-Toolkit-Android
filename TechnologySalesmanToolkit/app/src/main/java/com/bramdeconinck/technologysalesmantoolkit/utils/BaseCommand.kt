package com.bramdeconinck.technologysalesmantoolkit.utils

sealed class BaseCommand {

    class Error(val error: Int?): BaseCommand()

    class Success(val message: Int?): BaseCommand()

}