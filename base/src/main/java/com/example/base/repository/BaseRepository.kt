package com.example.base.repository

abstract class BaseRepository<Params, Result> {

    abstract suspend fun doWork(params: Params): Result

}