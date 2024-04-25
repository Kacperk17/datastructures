package wordle;

import org.junit.jupiter.api.Test;
import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;
import project20280.priorityqueue.DefaultComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WordleTest {

    public static final String MISS = "⬛";
    public static final String MISPLACED = "🟨";
    public static final String EXACT= "🟩";

    @Test
    void testHint() {

        String target1 = "olive";
        String target2 = "ghost";

        String target3 = "voila";


        String guess1 = "movie";
        String guess2 = "crane";
        String guess3 = "pious";
        String guess4 = "slosh";

        String guess5 = "lords";
        String guess6 = "bowls";

        String [] expectedHint1 = {MISS, MISPLACED, MISPLACED, MISPLACED, EXACT};
        String [] actualHint1 = Wordle.getHints(target1, guess1);

        String [] expectedHint2 = {MISS, MISS, MISS, MISS, EXACT};
        String [] actualHint2 = Wordle.getHints(target1, guess2);

        String [] expectedHint3 = {MISS, MISS, EXACT, MISS, MISPLACED};
        String [] actualHint3 = Wordle.getHints(target2, guess3);

        String [] expectedHint4 = {MISS, MISS, EXACT, EXACT, MISPLACED};
        String [] actualHint4 = Wordle.getHints(target2, guess4);

        String [] expectedHint5 = {MISPLACED, EXACT, MISS, MISS, MISS};
        String [] actualHint5 = Wordle.getHints(target3, guess5);

        String [] expectedHint6 = {MISS, EXACT, MISS, EXACT, MISS};
        String [] actualHint6 = Wordle.getHints(target3, guess6);

        assertArrayEquals(expectedHint1, actualHint1);
        assertArrayEquals(expectedHint2, actualHint2);
        assertArrayEquals(expectedHint3, actualHint3);
        assertArrayEquals(expectedHint4, actualHint4);
        assertArrayEquals(expectedHint5, actualHint5);
        assertArrayEquals(expectedHint6, actualHint6);

    }

    // method to test whether my chainhashmap gives the same results as java util hashmap
    @Test
    void testChainHashmap() {

        Wordle wordle = new Wordle();

        // java util
        HashMap<Character, Integer> frequencyOfEachLetter = Wordle.getFrequency(Wordle.dictionaryToString());
        HashMap<String, Integer> possible_words = Wordle.getSumOfFrequencyOfLettersInWord(frequencyOfEachLetter);

        // chainhashmap
        ChainHashMap<Character, Integer> frequencyOfEachLetterChain = Wordle.getFrequencyChain(Wordle.dictionaryToString());
        ChainHashMap<String, Integer> possible_wordsChain = Wordle.getSumOfFrequencyOfLettersInWordChain(frequencyOfEachLetterChain);


        boolean equal = true;

        // check if each entry in util hashmap has the same value as the equivilant entry in chainhashmap (Possible Words)
        for(Map.Entry<String, Integer> utilEntry : possible_words.entrySet()) {

            Integer chainValue = possible_wordsChain.get(utilEntry.getKey());
            Integer utilValue = utilEntry.getValue();

            // check if chain value is the same as the util value
            if(!chainValue.equals(utilValue)) {
                equal = false;
            }

        }

        // check if frequency of letters is the same in util and chainhashmap
        for(Map.Entry<Character, Integer> utilEntry : frequencyOfEachLetter.entrySet()) {

            Integer chainValue = frequencyOfEachLetterChain.get(utilEntry.getKey());
            Integer utilValue = utilEntry.getValue();

            // check if chain value is the same as the util value
            if(!chainValue.equals(utilValue)) {
                equal = false;
            }

        }


        assertTrue(equal);


    }




}
