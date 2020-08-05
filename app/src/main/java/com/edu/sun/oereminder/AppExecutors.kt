package com.edu.sun.oereminder

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

class AppExecutors private constructor(
    val diskIO: Executor,
    val networkIO: Executor,
    val mainThread: Executor
) {

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {

        @JvmStatic
        fun getInstance() = Holder.INSTANCE

        private const val KEEP_ALIVE_TIME = 1L
        private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()

        private object Holder {
            val INSTANCE = AppExecutors(
                Executors.newSingleThreadExecutor(),
                ThreadPoolExecutor(
                    NUMBER_OF_CORES,
                    NUMBER_OF_CORES * 2,
                    KEEP_ALIVE_TIME,
                    TimeUnit.SECONDS,
                    LinkedBlockingQueue<Runnable>()
                ),
                MainThreadExecutor()
            )
        }
    }
}
