package com.techyourchance.multithreading.home;

public enum ScreenReachableFromHome {
    EXERCISE_1("Exercise 1"),
    EXERCISE_2("Exercise 2"),
    UI_THREAD_DEMONSTRATION("UI Thread Demo"),
    UI_HANDLER_DEMONSTRATION("UI Handler Demo"),
    CUSTOM_HANDLER_DEMONSTRATION("Custom Handler Demo"),
    EXERCISE_3("Exercise 3"),
    ATOMICITY_DEMONSTRATION("Atomicity Demo"),
    EXERCISE_4("Exercise 4"),
    THREAD_WAIT_DEMONSTRATION("Thread Wait Demo"),
    EXERCISE_5("Exercise 5"),
    DESIGN_WITH_THREADS_DEMONSTRATION("Design Demo: Threads"),
    EXERCISE_6("Exercise 6"),
    DESIGN_WITH_THREAD_POOL_DEMONSTRATION("Design Demo: Thread Pool"),
    EXERCISE_7("Exercise 7"),
    DESIGN_WITH_ASYNCTASK_DEMONSTRATION("Design Demo: AsyncTask"),
    DESIGN_WITH_THREAD_POSTER_DEMONSTRATION("Design Demo: ThreadPoster"),
    EXERCISE_8("Exercise 8"),
    DESIGN_WITH_RX_JAVA_DEMONSTRATION("Design Demo: RxJava"),
    EXERCISE_9("Exercise 9"),
    DESIGN_WITH_COROUTINES_DEMONSTRATION("Design Demo: Coroutines"),
    EXERCISE_10("Exercise 10"),
    ;

    private String mName;

    ScreenReachableFromHome(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
