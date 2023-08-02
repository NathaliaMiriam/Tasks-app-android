package com.devmasterteam.tasks.service.constants

/**
 * Constantes usadas na aplicação
 */
class TaskConstants private constructor() {

    // SharedPreferences
    object SHARED {
        const val TOKEN_KEY = "tokenkey"
        const val PERSON_KEY = "personkey"
        const val PERSON_NAME = "personname"
    }

    // requisições API
    object HEADER {
        const val TOKEN_KEY = "token"
        const val PERSON_KEY = "personkey"
    }

    object HTTP {
        const val SUCCESS = 200
    }

    object BUNDLE {
        const val TASKID = "taskid"
        const val TASKFILTER = "taskfilter" // p os argumentos de cada Fragment, colocados em: mobile_navigation -> design
    }

    // p os argumentos de cada Fragment, colocados em: mobile_navigation -> design
    // cada uma das Fragments, assim que clicada, traz uma info... Assim, é possível identificar o que a config. da Fragment foi criada p fazer
    object FILTER {
        const val ALL = 0
        const val NEXT = 1
        const val EXPIRED = 2
    }

}