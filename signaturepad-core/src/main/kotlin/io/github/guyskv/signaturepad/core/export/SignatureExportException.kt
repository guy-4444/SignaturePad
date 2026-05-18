package io.github.guyskv.signaturepad.core.export

/**
 * Exception thrown when attempting to export an empty or invalid signature.
 *
 * @param message A descriptive error message.
 */
class SignatureExportException(message: String) : Exception(message)
