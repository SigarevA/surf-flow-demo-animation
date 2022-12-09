/**
 * Copyright © 2022 Surf. All rights reserved.
 */
package ru.surfstudio.android.demo.core.shared.result.provider

/** Сущность, которая предоставляет идентификатор результата для `SharedResult`. */
interface SharedResultIdProvider {

    /** Идентификатор результата работы. */
    val sharedResultId: String
}