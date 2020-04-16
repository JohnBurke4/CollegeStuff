import java.util.TreeMap;

public class Q4{


    /**
     * Asymptotic worst case running time: O(n)
     * Worst case occurs if the substring str[s..e] is a palindrome as
     * the function iterates through every character in the substring.
     *
     * Asymptotic worst case memory requirement: O(1)
     * This is cause the program only checks and holds in memory 2 extra charaters at
     * a time.
     */
    public static boolean isPalindrome(String str, int s, int e){
        while (e >= s){
            if (str.charAt(s++) != str.charAt(e--)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Asymptotic worst case running time: O(2^n)
     * Worst case occurs if str is not a palindrome, it checks through every substring.
     * That way it calls isPalindrome for size N + 2(n-1) + 4(n-2)...2^n-1(1) ~ O(2^n)
     *
     * Asymptotic worst case memory requirement: O(n)
     * The function will keep at most n calls on the stack which is the height of the tree
     * Created by the 2^n recursive calls;
     */

    public static int longestPalindrome(String str){
        return longestPalindrome(str, 0, str.length());
    }

    private static int longestPalindrome(String str, int s, int e){
        if (isPalindrome(str, s, e)){
            return e - s;
        }
        else {
            int left = longestPalindrome(str, ++s, e);
            int right = longestPalindrome(str, s, --e);
            return (left>right?left:right);
        }
    }

    




    public static void main(String[] args){

    }
}