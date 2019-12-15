
public class Q4 {

    /**
     * Amortized worst case running time: O(n)
     * Explanation: The worst case occurs when s and e are the start and end indexes of str respectively.
     *              The algorithm checks the index of each character in the string hence O(n).
     *
     * Amortized worst case memory requirement: O(1)
     * Explanation: The algorithm only saves a single character at a time to memory and replaces it afterwards.
     */
    public static boolean isPalindrome(String str, int s, int e){
        int lo = s;
        int hi = e;
        while (lo < hi){
            if (str.charAt(lo) != str.charAt(hi)){
                return false;
            }
            lo++;
            hi--;
        }
        return true;
    }

    public static int longestIterativePalindrome(String str){
        int longest = 0;
        for (int i = 0; i < str.length(); i++){
            for(int j = i; j < str.length(); j++){
                if (j - i > longest){
                    if (isPalindrome(str, i, j)){
                        longest = (j - i) + 1;
                    }
                }
            }
        }
        return longest;
    }

    /**
     * Amortized worst case running time: O(2^n)
     * Explanation: String substring method takes O(n) time, this is called 2^n times as the
     *
     * Amortized worst case memory requirement: O(1)
     * Explanation: The algorithm only saves a single character at a time to memory and replaces it afterwards.
     */
    public static int longestPalindrome(String str){
        if (isPalindrome(str, 0, str.length()-1)){
            return str.length();
        }
        else {
            int longest1 = longestPalindrome(str.substring(0, str.length()-1));
            int longest2 = longestPalindrome(str.substring(1));
            return (longest1>longest2?longest1:longest2);
        }

    }

//    public static int longestPalindromeST(String str){
//
//    }

    public static void main(String[] args){
        String test = "redrumsirismurder";
        //System.out.println(isPalindrome(test, 1, test.length() - 2));
        System.out.println(longestPalindrome(test));
    }
}
