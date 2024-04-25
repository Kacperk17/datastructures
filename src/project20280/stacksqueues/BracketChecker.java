package project20280.stacksqueues;

class BracketChecker {
    private final String input;

    public BracketChecker(String in) {
        input = in;
    }

    public void check() {
        // TODO

        // sums for each bracket
        int squareSum = 0;
        int curlSum = 0;
        int swishSum = 0;

        // go through String
        for(char c : input.toCharArray()) {
            if(c == '[') squareSum++;
            if(c == '(') curlSum++;
            if(c == '{') swishSum++;

            if(c == ']') squareSum--;
            if(c == ')') curlSum--;
            if(c == '}') swishSum--;
        }

        if(squareSum != 0 || curlSum != 0 || swishSum != 0) {
            System.out.println("incorrect brackets detected");
        }


    }

    public static void main(String[] args) {
        String[] inputs = {
                "[]]()()", // not correct
                "c[d]", // correct\n" +
                "a{b[c]d}e", // correct\n" +
                "a{b(c]d}e", // not correct; ] doesn't match (\n" +
                "a[b{c}d]e}", // not correct; nothing matches final }\n" +
                "a{b(c) ", // // not correct; Nothing matches opening {
        };

        for (String input : inputs) {
            BracketChecker checker = new BracketChecker(input);
            System.out.println("checking: " + input);
            checker.check();
        }
    }
}