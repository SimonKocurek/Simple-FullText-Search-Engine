package io.search.engine

import java.nio.CharBuffer

/**
 * Utility function, as `get()` moves position and `get(index)` ignores subsequence position.
 */
private inline fun CharBuffer.at(index: Int) = get(index + position())
private inline fun CharBuffer.putAt(index: Int, c: Char) = put(index + position(), c)


/**
 * Using "The English (Porter2) stemming algorithm"
 *
 * Source: https://snowball.tartarus.org/algorithms/english/stemmer.html
 */
internal inline fun Char.isVowel() = when (this) {
    'a', 'e', 'i', 'o', 'u', 'y' -> true
    else -> false
}

internal inline fun CharBuffer.isDouble() = when (at(0)) {
    'b' -> at(1) == 'b'
    'd' -> at(1) == 'd'
    'f' -> at(1) == 'f'
    'g' -> at(1) == 'g'
    'm' -> at(1) == 'm'
    'n' -> at(1) == 'n'
    'p' -> at(1) == 'p'
    'r' -> at(1) == 'r'
    't' -> at(1) == 't'
    else -> false
}

internal inline fun Char.isLiEnding() = when (this) {
    'c', 'd', 'e', 'g', 'h', 'k', 'm', 'n', 'r', 't' -> true
    else -> false
}

internal inline fun CharBuffer.getRRegion(): CharBuffer {
    for (i in 0 until length - 1) {
        if (at(i).isVowel() && !at(i + 1).isVowel()) {
            return subSequence(i + 2, length)
        }
    }

    return subSequence(0, 0) // Empty
}

internal inline fun CharBuffer.getLastShortSyllable(): CharBuffer {
    for (i in length - 1 downTo 1) {
        if (at(i).isVowel() && !at(i + 1).isVowel() &&
            // These non-vowels can't form a short syllable
            at(i + 1) != 'w' && at(i + 1) != 'x' && at(i + 1) != 'Y' && // Upper case acts as a flag indicating a consonant
            // Must be preceded by non-vowel
            !at(i - 1).isVowel()
        ) {
            return subSequence(i, i + 2)
        }
    }

    // At the beginning of word, the rules are simpler:
    if (length >= 2 && at(0).isVowel() && !at(1).isVowel()) {
        return subSequence(0, 2)
    }

    return subSequence(0, 0) // Empty
}

internal inline fun CharBuffer.endsWithShortSyllable(): Boolean = getLastShortSyllable().let {
    it.position() + it.length == this.length
}

internal inline fun CharBuffer.isShortWord() = endsWithShortSyllable() && getRRegion().isEmpty()

/**
 * Converts to CharSequence so that substring operations are not creating new strings and flags certain vowels as consonants
 */
internal fun cleanWord(word: String): CharBuffer {
    var wordBuffer = CharBuffer.wrap(word.toCharArray())

    if (wordBuffer.length <= 2) {
        return wordBuffer
    }

    if (wordBuffer.startsWith('\'')) {
        wordBuffer = wordBuffer.subSequence(1, wordBuffer.length)
    }

    // Upper case acts as a flag indicating a consonant
    if (wordBuffer.startsWith('y')) {
        wordBuffer.putAt(0, 'Y')
    }

    for (i in 1 until wordBuffer.length) {
        if (wordBuffer.at(i) == 'y' && wordBuffer.at(i - 1).isVowel()) {
            wordBuffer.putAt(i, 'Y')
        }
    }

    return wordBuffer
}

/**
 * Converts an english word to its word stem: kneeling -> kneel
 *
 * ! Lowercase words must be provided. !
 */
fun getWordStem(word: String): String {
    return cleanWord(word).toString()
}
