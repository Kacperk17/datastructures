package wordle;

import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;
import project20280.interfaces.Position;
import project20280.priorityqueue.HeapPriorityQueue;
import project20280.tree.LinkedBinaryTree;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Wordle {

    //String fileName = "wordle/resources/dictionary.txt";
    String fileName = "wordle/resources/extended-dictionary.txt";
    static List<String> dictionary = null;
    final int num_guesses = 5;
    final long seed = 42;
    //Random rand = new Random(seed);
    Random rand = new Random();

    static final String winMessage = "CONGRATULATIONS! YOU WON! :)";
    static final String lostMessage = "YOU LOST :( THE WORD CHOSEN BY THE GAME IS: ";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_GREY_BACKGROUND = "\u001B[100m";
    public static final String MISS = "⬛";
    public static final String MISPLACED = "🟨";
    public static final String EXACT= "🟩";

    public static final int WORD_LENGTH = 5;
    public static final int ASCII_LENGTH = 8;

    Wordle() {

        this.dictionary = readDictionary(fileName);

        //System.out.println("dict length: " + this.dictionary.size());
        //System.out.println("dict: " + dictionary);

    }

    public static void main(String[] args) {
        Wordle game = new Wordle();

        String target = game.getRandomTargetWord();
        int totalGuesses = 0;


        frequencySolver("irony", false);


        //System.out.println("target: " + target);

        //game.play(target);

        LinkedBinaryTree<Character> tree = game.huffman(dictionaryToString());

        System.out.println(tree.inorder().toString());

        HashMap<Character, String> encodings = getEncodings(tree);

        ArrayList<String> encodedList = encodeDictionary(encodings);

        float sumAsciiBits = getNumOfBitsAscii();
        float sumHuffmanBits = getNumOfBitsHuffman(encodedList);
        float compressionRatio = sumHuffmanBits/sumAsciiBits;

        System.out.println(tree.toBinaryTreeString());




        System.out.println("The shortest encoded word is: " + getShortestCode(encodedList));
        System.out.println("The longest encoded word is: " + getLongestCode(encodedList));
        System.out.println("The compression ratio is: " + compressionRatio*100 + "%"); // compression ratio




    }

    public void play(String target) {
        // TODO
        // TODO: You have to fill in the code
        for(int i = 0; i < num_guesses; ++i) {
            String guess = getGuess();

            if(guess == target) { // you won!
                win(target);
                return;
            }

            // the hint is a string where green="+", yellow="o", grey="_"
            // didn't win ;(
            String [] hint = {"_", "_", "_", "_", "_"};


            hint = getHints(target, guess);

            // after setting the yellow and green positions, the remaining hint positions must be "not present" or "_"
            System.out.println("hint: " + Arrays.toString(hint));


            // check for a win
            int num_green = 0;
            for(int k = 0; k < 5; ++k) {
                if(hint[k] == EXACT) num_green += 1;
            }
            if(num_green == 5) {
                 win(target);
                 return;
            }
        }

        lost(target);
    }

    public void lost(String target) {
        System.out.println();
        System.out.println(lostMessage + target.toUpperCase() + ".");
        System.out.println();

    }
    public void win(String target) {
        System.out.println(ANSI_GREEN_BACKGROUND + target.toUpperCase() + ANSI_RESET);
        System.out.println();
        System.out.println(winMessage);
        System.out.println();
    }

    public String getGuess() {
        Scanner myScanner = new Scanner(System.in, StandardCharsets.UTF_8.displayName());  // Create a Scanner object
        System.out.println("Guess:");

        String userWord = myScanner.nextLine();  // Read user input
        userWord = userWord.toLowerCase(); // covert to lowercase

        // check the length of the word and if it exists
        while ((userWord.length() != 5) || !(dictionary.contains(userWord))) {
            if ((userWord.length() != 5)) {
                System.out.println("The word " + userWord + " does not have 5 letters.");
            } else {
                System.out.println("The word " + userWord + " is not in the word list.");
            }
            // Ask for a new word
            System.out.println("Please enter a new 5-letter word.");
            userWord = myScanner.nextLine();
        }
        return userWord;
    }

    public String getRandomTargetWord() {
        // generate random values from 0 to dictionary size
        return dictionary.get(rand.nextInt(dictionary.size()));
    }
    public List<String> readDictionary(String fileName) {
        List<String> wordList = new ArrayList<>();

        try {
            // Open and read the dictionary file
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            assert in != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strLine;

            //Read file line By line
            while ((strLine = reader.readLine()) != null) {
                wordList.add(strLine.toLowerCase());
            }
            //Close the input stream
            in.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return wordList;
    }

    public LinkedBinaryTree<Character> huffman(String x) {


        int length = x.length();

        //Comparator<Integer> integerComparator = Comparator.comparing(Integer::byteValue);

        HashMap<Character, Integer> charFrequency = getFrequency(x);
        HeapPriorityQueue<Integer, LinkedBinaryTree> queue = new HeapPriorityQueue<Integer, LinkedBinaryTree>();
        ArrayList<Character> charsInserted = new ArrayList<Character>();

        // fill up queue with nodes
        for(int i = 0; i < length; i++) {
            char c = x.charAt(i);

            // create single node binary tree
            LinkedBinaryTree<Character> singleNodeTree = new LinkedBinaryTree<Character>();
            singleNodeTree.addRoot(c);

            if(!charsInserted.contains(c)) {
                queue.insert(charFrequency.get(c), singleNodeTree);
            }

            charsInserted.add(c);
        }


        // iterate through queue

        while (queue.size() > 1) {

            Entry<Integer, LinkedBinaryTree> e1 = queue.removeMin();
            Entry<Integer, LinkedBinaryTree> e2 = queue.removeMin();
            Integer newKey = e1.getKey() + e2.getKey();
            LinkedBinaryTree node1 = e1.getValue();
            LinkedBinaryTree node2 = e2.getValue();

            LinkedBinaryTree<Character> characterTree = new LinkedBinaryTree<Character>();

            // root of tree (placeholder character)
            characterTree.addRoot(null);

            // add entry 1 to the left of this tree and entry 2 to right of tree.
            characterTree.attach(characterTree.root(), node1, node2);

            // add into final  character tree
            queue.insert(newKey, characterTree);


        }
        Entry<Integer, LinkedBinaryTree> finalEntry = queue.removeMin();

        return finalEntry.getValue();



    }

    // method that returns a HashMap of frequencies of each character in a string
    public static HashMap<Character, Integer> getFrequency(String string) {
        HashMap<Character, Integer> charFrequency = new HashMap<Character, Integer>();

        int len = string.length();

        // iterate through string to count characters and insert them into map as needed
        for(int i = 0; i < len; i++) {

            char c = string.charAt(i);
            Integer count = charFrequency.get(c);

            if(count == null) {
                charFrequency.put(c, 1);
            }
            else {
                charFrequency.put(c, count + 1);
            }


        }

        return charFrequency;
    }

    // method that returns a HashMap of frequencies of each character in a string using my chainhashmap
    public static ChainHashMap<Character, Integer> getFrequencyChain(String string) {
        ChainHashMap<Character, Integer> charFrequency = new ChainHashMap<Character, Integer>();

        int len = string.length();

        // iterate through string to count characters and insert them into map as needed
        for(int i = 0; i < len; i++) {

            char c = string.charAt(i);
            Integer count = charFrequency.get(c);

            if(count == null) {
                charFrequency.put(c, 1);
            }
            else {
                charFrequency.put(c, count + 1);
            }


        }

        return charFrequency;
    }

    // method which turns the dictionary and returns it as one long string
    public static String dictionaryToString() {
        StringBuilder sb = new StringBuilder();

        for(String string : dictionary) {
            sb.append(string);
        }

        return sb.toString();
    }

    // method which takes in a character and binary tree and traverses the tree until it finds the character



    // method that takes a huffman tree  and returns a HashMap of its encodings

    public static HashMap<Character, String> getEncodings(LinkedBinaryTree<Character> tree) {

        HashMap<Character, String> encodings = new HashMap<>();
        ArrayList<Character> distinctCharacters = new ArrayList<>();

        // get an array of distinct characters
        for(Character c : dictionaryToString().toCharArray()) {
            // if array doesnt contain c
            if(!distinctCharacters.contains(c)) {
                distinctCharacters.add(c);
            }
        }

        // iterate over distinct characters
        for(Character c : distinctCharacters) {
            encodings.put(c, tree.pathToNode(c));
        }

        return encodings;
    }

    // method that takes in the huffman encodings and returns an array of strings with the words encoded
    public static ArrayList<String> encodeDictionary(HashMap<Character, String> encodings) {
        ArrayList<String> encodedList = new ArrayList<>();

        // for each word in the dictionary
        for(String word : dictionary) {

            // for each character in the word
            StringBuilder sb = new StringBuilder();
            for (Character c : word.toCharArray()) {
                sb.append(encodings.get(c));
            }

            encodedList.add(sb.toString());

        }

        return encodedList;
    }

    // method that returns word with longest bits
    public static ArrayList<String> getLongestCode(ArrayList<String> huffmanEncodedWords) {

        ArrayList<String> wordAndEncoding = new ArrayList<>();
        int indexOfLargest = 0;
        int longest = 0;

        for(String word : huffmanEncodedWords) {

            if(word.length() > longest) {
                longest = word.length();
                indexOfLargest = huffmanEncodedWords.indexOf(word);
            }
        }

        wordAndEncoding.add(dictionary.get(indexOfLargest));
        wordAndEncoding.add(huffmanEncodedWords.get(indexOfLargest));

        return wordAndEncoding;

    }

    // method that returns word with shortest bits
    public static ArrayList<String> getShortestCode(ArrayList<String> huffmanEncodedWords) {

        ArrayList<String> wordAndEncoding = new ArrayList<>();
        int indexOfShortest = 0;
        int shortest = huffmanEncodedWords.get(0).length();

        for(String word : huffmanEncodedWords) {

            if(word.length() < shortest) {
                shortest = word.length();
                indexOfShortest = huffmanEncodedWords.indexOf(word);
            }
        }

        wordAndEncoding.add(dictionary.get(indexOfShortest));
        wordAndEncoding.add(huffmanEncodedWords.get(indexOfShortest));

        return wordAndEncoding;

    }

    // method that returns amount of bits required to encode using ascii coding (number of characters * 8)
    public static int getNumOfBitsAscii() {
        return (dictionary.size()  * WORD_LENGTH * ASCII_LENGTH);
    }

    // method that returns amount of bits required to encoding using huffman
    public static int getNumOfBitsHuffman(ArrayList<String> huffmanEncodedWords) {

        int sum = 0;

        for(String word : huffmanEncodedWords) {

            // for each char (bit) in the word add to sum
            for(Character c : word.toCharArray()) {
                sum++;
            }

        }

        return sum;
    }


    public static String [] getHints(String target, String guess) {

        // HashMap which has each char and its frequency in the target string
        HashMap<Character, Integer> charFrequencyOfTarget = getFrequency(target);
        HashMap<Character, Integer> charFrequencyOfMatches = new HashMap<>(); // stores a count of how many times each char was matched
        HashMap<Character, Integer> charFrequencyOfMisplaced = new HashMap<>();

        // initialize frequency match and misplaced map

        for(char c : target.toCharArray()) {
            charFrequencyOfMatches.put(c, 0);
            charFrequencyOfMisplaced.put(c, 0);
        }

        String [] hint = new String[5];

        // first check for exact matches
        for(int k = 0; k < 5; ++k) {
            if(guess.charAt(k) == target.charAt(k)) {
                hint[k] = EXACT;

                // update HashMap
                int currentCount = charFrequencyOfMatches.get(target.charAt(k));
                charFrequencyOfMatches.put(target.charAt(k), currentCount + 1);
            }
            else {
                hint[k] = MISS;
            }
        }

        // next check for misplaced guesses
        for(int k = 0; k < 5; ++k) {

            char currentChar = guess.charAt(k);
            int numOfMatchesOfLetter = 0;
            int currentMisplacedCount = 0;
            try {
                numOfMatchesOfLetter = charFrequencyOfMatches.get(currentChar);
            } catch (Exception ex) {}

            if(!(currentChar == target.charAt(k)) && numOfMatchesOfLetter == 0 && target.indexOf(currentChar) > -1 && charFrequencyOfMisplaced.get(currentChar) < charFrequencyOfTarget.get(currentChar)) {
                hint[k] = MISPLACED;

                try {
                    currentMisplacedCount = charFrequencyOfMisplaced.get(guess.charAt(k));
                    charFrequencyOfMisplaced.put(guess.charAt(k), currentMisplacedCount + 1);
                } catch (Exception ex) {}

            }

        }


        return hint;
    }

    public static HashMap<String, Integer> getSumOfFrequencyOfLettersInWord(HashMap<Character, Integer> frequencyOfLetters) {
        HashMap<String, Integer> result = new HashMap<>();

        // iterate through each word in the dictionary
        for(String word : dictionary) {

            int sumOfFrequencies = 0;

            // iterate through each character in the word
            for(Character c : word.toCharArray()) {
                sumOfFrequencies += frequencyOfLetters.get(c);
            }

            result.put(word, sumOfFrequencies);

        }


        return result;
    }

    public static ChainHashMap<String, Integer> getSumOfFrequencyOfLettersInWordChain(ChainHashMap<Character, Integer> frequencyOfLetters) {
        ChainHashMap<String, Integer> result = new ChainHashMap<>();

        // iterate through each word in the dictionary
        for(String word : dictionary) {

            int sumOfFrequencies = 0;

            // iterate through each character in the word
            for(Character c : word.toCharArray()) {
                sumOfFrequencies += frequencyOfLetters.get(c);
            }

            result.put(word, sumOfFrequencies);

        }


        return result;
    }

    public static int frequencySolver(String target, boolean utilHashMap) {


        // java.util hashmap
        HashMap<Character, Integer> frequencyOfEachLetter = getFrequency(dictionaryToString());
        HashMap<String, Integer> possible_words = getSumOfFrequencyOfLettersInWord(frequencyOfEachLetter);



        // chainhashmap
        ChainHashMap<Character, Integer> frequencyOfEachLetterChain = getFrequencyChain(dictionaryToString());
        ChainHashMap<String, Integer> possible_wordsChain = getSumOfFrequencyOfLettersInWordChain(frequencyOfEachLetterChain);

        System.out.println("Number of collisions in the frequency table: " + possible_wordsChain.collisions());


        String guess = "adieu";
        String guessChain = "adieu";
        int numOfGuesses = 0;

        String [] hint = getHints(target, guess);
        String [] hintChain = getHints(target, guessChain);



        long before = System.currentTimeMillis();
        while(utilHashMap ? (possible_words.size() > 1) : (possible_wordsChain.size() > 1)) {

            if(utilHashMap) {
                possible_words = filterPossibleWords(possible_words, hint, guess);
            } else {
                possible_wordsChain = filterPossibleWordsChain(possible_wordsChain, hintChain, guessChain);
            }


            // get entry with highest value in the map
            if(utilHashMap) {
                guess = getEntryWithHighestValue(possible_words);
            } else {
                guessChain = getEntryWithHighestValueChain(possible_wordsChain);
            }

            if(utilHashMap) {
                System.out.println("Guess: " + guess);
            }else {
                System.out.println("Guess: " + guessChain);
            }

            numOfGuesses++;

            if(utilHashMap) {
                hint = getHints(target, guess);
            } else {
                hintChain = getHints(target, guessChain);
            }
        }
        long after = System.currentTimeMillis();


        System.out.println("Guesses used: " + (numOfGuesses-1));
        if(utilHashMap) {
            System.out.println("Frequency Solver Solution: " + guess);
        }else {
            System.out.println("Frequency Solver Solution: " + guessChain);
        }
        System.out.println("Time taken: " + (after - before) + "ms");
        return numOfGuesses;

    }

    // returns possible words filtered out based on a hint
    public static HashMap<String, Integer> filterPossibleWords(HashMap<String, Integer> possible_words, String [] hint, String guess) {

        // if hint is a winner, return "possible words" as just the guess
        if(isExact(hint)) {

            HashMap<String, Integer> winner = new HashMap<>();

            winner.put(guess, possible_words.get(guess));

            return winner;
        }

        char [] firstGuessArray = guess.toCharArray();

        ArrayList<Character> exactAndMisplacedLetters = new ArrayList<>();

        // first, go through each entry and remove any that dont have exact match
        Iterator<Map.Entry<String, Integer>> iterator = possible_words.entrySet().iterator();
        while(iterator.hasNext()) {

            boolean remove = false;
            Map.Entry<String, Integer> entry = iterator.next();

            String currentWord = entry.getKey();

            // go through hint and determine whether word matches hint for exact positions
            for(int i = 0; i < 5; i++) {


                // if the hint at this position is exact
                if(hint[i].equals(EXACT)) {

                    exactAndMisplacedLetters.add(guess.charAt(i));

                    // check if word at this position doesn't have the correct letter
                    if(currentWord.charAt(i) != firstGuessArray[i]) {

                        // remove this word
                        remove = true;
                    }
                }

                // check for misplaced (i.e. if the word has occurances of the letter)
                if(hint[i].equals(MISPLACED)) {
                    exactAndMisplacedLetters.add(guess.charAt(i));
                    // if this word doesnt have that letter in it anywhere
                    if(!hasOccurance(currentWord, guess.charAt(i))) {

                        // remove this word
                        remove = true;
                    }

                }

                // check for no occurances of the letter
                if(hint[i].equals(MISS)) {

                    // if this letter hasnt been in the hint and the current word has occurances of the letter
                    if(guess.charAt(i) == currentWord.charAt(i)) {

                        remove = true;
                    }

                }

            }

            // if we should remove
            if(remove) {
                iterator.remove();
            }

        }

        // remove guess from the possible word list
        possible_words.remove(guess);


        return possible_words;
    }

    // returns possible words filtered out based on a hint but with my chainhashmap
    public static ChainHashMap<String, Integer> filterPossibleWordsChain(ChainHashMap<String, Integer> possible_words, String [] hint, String guess) {

        // if hint is a winner, return "possible words" as just the guess
        if(isExact(hint)) {

            ChainHashMap<String, Integer> winner = new ChainHashMap<>();

            winner.put(guess, possible_words.get(guess));

            return winner;
        }

        ArrayList<String> wordsToDelete = new ArrayList<>();

        for(String currWord : possible_words.keySet()) {

            if(shouldDeleteExact(currWord, hint, guess) || shouldDeleteMisplaced(currWord, hint, guess) || shouldDeleteMiss(currWord, hint, guess)) {
                wordsToDelete.add(currWord);
            }
        }

        for(String word : wordsToDelete) {
            possible_words.remove(word);
        }

        possible_words.remove(guess);
        return possible_words;
    }


    // method that returns entry with highest value in a java util hashmap
    public static String getEntryWithHighestValue(HashMap<String, Integer> possible_words) {
        int max = 0;
        String word = "";

        for(Map.Entry<String, Integer> entry : possible_words.entrySet()) {

            if(entry.getValue() > max) {
                word = entry.getKey();
                max = entry.getValue();
            }

        }

        return word;
    }

    // method that returns entry with highest value in my chainhashmap
    public static String getEntryWithHighestValueChain(ChainHashMap<String, Integer> possible_words) {
        int max = 0;
        String word = "";

        for(Entry<String, Integer> entry : possible_words.entrySet()) {

            if(entry.getValue() > max) {
                word = entry.getKey();
                max = entry.getValue();
            }

        }

        return word;
    }


    // method that determines if a hint is exact
    public static boolean isExact(String [] hint) {

        int num_green = 0;
        for(int k = 0; k < 5; ++k) {
            if(hint[k] == EXACT) num_green += 1;
        }

        return num_green == 5;

    }

    // method that determines if a string has an occurance of a given character
    public static boolean hasOccurance(String word, char c) {

        for(Character letter : word.toCharArray()) {

            if(letter.equals(c)) {
                return true;
            }

        }

        return false;

    }

    // method that returns true if a word should be deleted based on the guess and hint (only checks for exact)
    public static boolean shouldDeleteExact(String wordToCheck, String [] hint, String guess) {
        boolean shouldDelete = false;

        // check through the hint
        for(int i = 0; i < 5; i++) {

            // if hint at i is exact (green)
            if(hint[i].equals(EXACT)) {

                // if the word to check has a different char to the guess
                if(wordToCheck.charAt(i) != guess.charAt(i)) {
                    shouldDelete = true;
                }

            }

        }


        return shouldDelete;
    }

    // method that returns whether a word should be deleted for misplaced
    public static boolean shouldDeleteMisplaced(String wordToCheck, String [] hint, String guess) {
        boolean shouldDelete = false;

        // check through hint
        for(int i = 0; i < 5; i++) {

            // if the hint here is misplaced
            if(hint[i].equals(MISPLACED)) {

                // if the word to check has no occurance of this letter
                if(!hasOccurance(wordToCheck, guess.charAt(i))) {
                    shouldDelete = true;
                }

            }

        }


        return shouldDelete;
    }

    // method that returns whether a word should be deleted for miss
    public static boolean shouldDeleteMiss(String wordToCheck, String [] hint, String guess) {
        boolean shouldDelete = false;

        for(int i = 0; i < 5; i++) {

            // if hint at i is MISS
            if(hint[i].equals(MISS)) {

                if(wordToCheck.charAt(i) == guess.charAt(i)) {

                    shouldDelete = true;

                }
            }
        }
        return shouldDelete;
    }






}
