package io.search.engine

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.nio.CharBuffer

class StemmerTest {

    @ParameterizedTest
    @CsvSource(
        "beautiful,iful,ul",
        "beauty,y,",
        "beau,,",
        "animadversion,imadversion,adversion",
        "eucharist,harist,ist",
        "sprinkled,kled,",
    )
    fun `can get r region`(word: String, expectedR1Region: String?, expectedR2Region: String?) {
        // Given
        val sequence = CharBuffer.wrap(word)

        // When
        val r1Region = sequence.getRRegion()
        val r2Region = r1Region.getRRegion()

        // Then
        Assertions.assertEquals(expectedR1Region ?: "", r1Region.toString())
        Assertions.assertEquals(expectedR2Region ?: "", r2Region.toString())
    }

    @ParameterizedTest
    @ValueSource(strings = ["rap", "trap", "entrap"])
    fun `word ends with short syllable`(word: String) {
        // Given
        val sequence = CharBuffer.wrap(word)

        // When
        val syllable = sequence.getLastShortSyllable()
        val endsWithShortSyllable = sequence.endsWithShortSyllable()

        // Then
        Assertions.assertEquals("ap", syllable.toString())
        Assertions.assertEquals(true, endsWithShortSyllable)
    }

    @ParameterizedTest
    @ValueSource(strings = ["ow", "on", "at"])
    fun `word is short syllable`(word: String) {
        // Given
        val sequence = CharBuffer.wrap(word)

        // When
        val syllable = sequence.getLastShortSyllable()
        val endsWithShortSyllable = sequence.endsWithShortSyllable()

        // Then
        Assertions.assertEquals(word, syllable.toString())
        Assertions.assertEquals(true, endsWithShortSyllable)
    }

    @ParameterizedTest
    @ValueSource(strings = ["uproot", "bestow", "disturb"])
    fun `word does not end with short syllable`(word: String) {
        // Given
        val sequence = CharBuffer.wrap(word)

        // When
        val syllable = sequence.getLastShortSyllable()
        val endsWithShortSyllable = sequence.endsWithShortSyllable()

        // Then
        Assertions.assertNotEquals(word.length, syllable.position() + syllable.length)
        Assertions.assertEquals(false, endsWithShortSyllable)
    }

    @ParameterizedTest
    @ValueSource(strings = ["bed", "shed", "shred"])
    fun `is short word`(word: String) {
        // Given
        val sequence = CharBuffer.wrap(word)

        // When
        val isShortWord = sequence.isShortWord()

        // Then
        Assertions.assertEquals(true, isShortWord)
    }

    @ParameterizedTest
    @ValueSource(strings = ["bead", "embed", "beds"])
    fun `is not short word`(word: String) {
        // Given
        val sequence = CharBuffer.wrap(word)

        // When
        val isShortWord = sequence.isShortWord()

        // Then
        Assertions.assertEquals(false, isShortWord)
    }

    @ParameterizedTest
    @CsvSource(
        "youth,Youth",
        "boy,boY",
        "boyish,boYish",
        "fly,fly",
        "flying,flying",
        "syzygy,syzygy",
        "ayyyyy,aYyYyY",
    )
    fun `cleans word`(word: String, expectedCleanedWord: String) {
        // Given

        // When
        val cleanedWord = cleanWord(word)

        // Then
        Assertions.assertEquals(expectedCleanedWord, cleanedWord.toString())
    }

    @ParameterizedTest
    @CsvSource(
        "consign,consign",
        "consigned,consign",
        "consigning,consign",
        "consignment,consign",
        "consist,consist",
        "consisted,consist",
        "consistency,consist",
        "consistent,consist",
        "consistently,consist",
        "consisting,consist",
        "consists,consist",
        "consolation,consol",
        "consolations,consol",
        "consolatory,consolatori",
        "console,consol",
        "consoled,consol",
        "consoles,consol",
        "consolidate,consolid",
        "consolidated,consolid",
        "consolidating,consolid",
        "consoling,consol",
        "consolingly,consol",
        "consols,consol",
        "consonant,conson",
        "consort,consort",
        "consorted,consort",
        "consorting,consort",
        "conspicuous,conspicu",
        "conspicuously,conspicu",
        "conspiracy,conspiraci",
        "conspirator,conspir",
        "conspirators,conspir",
        "conspire,conspir",
        "conspired,conspir",
        "conspiring,conspir",
        "constable,constabl",
        "constables,constabl",
        "constance,constanc",
        "constancy,constanc",
        "constant,constant",
        "knack,knack",
        "knackeries,knackeri",
        "knacks,knack",
        "knag,knag",
        "knave,knave",
        "knaves,knave",
        "knavish,knavish",
        "kneaded,knead",
        "kneading,knead",
        "knee,knee",
        "kneel,kneel",
        "kneeled,kneel",
        "kneeling,kneel",
        "kneels,kneel",
        "knees,knee",
        "knell,knell",
        "knelt,knelt",
        "knew,knew",
        "knick,knick",
        "knif,knif",
        "knife,knife",
        "knight,knight",
        "knightly,knight",
        "knights,knight",
        "knit,knit",
        "knits,knit",
        "knitted,knit",
        "knitting,knit",
        "knives,knive",
        "knob,knob",
        "knobs,knob",
        "knock,knock",
        "knocked,knock",
        "knocker,knocker",
        "knockers,knocker",
        "knocking,knock",
        "knocks,knock",
        "knopp,knopp",
        "knot,knot",
        "knots,knot",
    )
    fun `generates expected stem`(word: String, expectedStem: String) {
        // Given

        // When
        val returnedStem = getWordStem(word)

        // Then
        Assertions.assertEquals(expectedStem, returnedStem)
    }
}
