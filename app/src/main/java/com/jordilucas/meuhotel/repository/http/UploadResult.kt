package com.jordilucas.meuhotel.repository.http

sealed class UploadExecution

data class UploadResult(var id: Long, var photoUrl: String): UploadExecution()
object NoUploadPerformed: UploadExecution()