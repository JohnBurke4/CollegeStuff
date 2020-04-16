
public class Q3 {

    Node root;

    private class Node{
        Node next;
        Word word;

        public Node(Word word){
            this.word = word;
        }

        public Word getWord(){
            return word;
        }
    }

    private class Word {
        String word;
        Definition definition;

        public Word(String word) {
            this.word = word;
            definition = new Definition();
        }

        public boolean is(String word){
            return this.word.equals(word);
        }

        public String getMeanings(){
            return word + "\n" + definition.getMeanings();
        }
    }

    private class Definition {
        Meaning meanings;

        public Definition() {
            meanings = null;
        }

        public void addMeaning(String meaning){
            String[] split = meaning.split(" ");
            Word[] words = new Word[split.length];

            for (int i = 0; i < split.length; i++){
                String word = split[i];
                Word current = getWord(word);
                if (current != null){
                    words[i] = current;
                }
                else {
                    addWord(word);
                    words[i] = root.getWord();
                }
            }

            Meaning lastMeaning = meanings;

            if (lastMeaning == null){
                meanings = new Meaning(words);
            }
            else {
                while (lastMeaning.next != null){
                    lastMeaning = lastMeaning.next;
                }

                lastMeaning.next = new Meaning(words);
            }
        }

        public String getMeanings(){
            String result = "";
            if (meanings == null){
                return "No meanings found for word\n";
            }
            else {
                for (Meaning meaning = meanings; meaning != null; meaning = meaning.next){
                    result += meaning.getWords() + "\n";
                }
                return result;
            }
        }
    }

    private class Meaning {
        Word[] words;
        Meaning next;

        public Meaning(Word[] words) {
            this.words = words;
            next = null;
        }

        public String getWords(){
            String result = "";
            for (Word word: words){
                result += word.word + " ";
            }
            return result;
        }
    }

    public Q3(){
        root = null;
    }


    public boolean containsWord(String word){
        String lowercase = word.toLowerCase();
        for (Node node = root; node != null; node = node.next){
            if (node.getWord().is(lowercase)){
                return true;
            }
        }
        return false;
    }

    private Word getWord(String word){
        String lowercase = word.toLowerCase();
        for (Node node = root; node != null; node = node.next){
            if (node.getWord().is(lowercase)){
                return node.getWord();
            }
        }
        return null;
    }

    public void addWord(String word) {
        String lowercase = word.toLowerCase();
        if (!containsWord(lowercase)){
            Node newNode = new Node(new Word(lowercase));
            newNode.next = root;
            root = newNode;
        }
    }

    public void addMeaning(String word, String meaning) {
        Word result = getWord(word);

        if (result != null){
            result.definition.addMeaning(meaning);
        }
    }

    public void removeWord(String word) {
    }

    public String getMeanings(String word){
        Word current = getWord(word);

        if (current == null){
            return word + " does not exist in dictionary\n";
        }
        else {
            return current.getMeanings();
        }
    }

    public void printWords(){
        for (Node node = root; node != null; node = node.next){
            System.out.println(getMeanings(node.getWord().word));
        }
    }

    public static void main(String[] args){
        Q3 dict = new Q3();

        dict.addWord("Hello");
        dict.addWord("Hi");
        dict.addWord("Test");

        String testMeaning = "a procedure intended to establish the quality, performance, or reliability of something, especially before it is taken into widespread use.";
        String testMeaning2 = "a short written or spoken examination of a person's proficiency or knowledge.";
        String helloMeaning = "used as a greeting or to begin a telephone conversation.";

        dict.addMeaning("Test", testMeaning);
        dict.addMeaning("Test", testMeaning2);
        dict.addMeaning("Hello", helloMeaning);

        //System.out.println(dict.getMeanings("Test"));
        //Sy/stem.out.println(dict.getMeanings("Hello"));
        //System.out.println(dict.getMeanings("Not"));
        dict.printWords();
    }


}
