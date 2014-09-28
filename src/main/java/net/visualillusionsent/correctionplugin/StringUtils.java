package net.visualillusionsent.correctionplugin;

import net.visualillusionsent.utils.ArrayUtils;

import java.util.LinkedList;

/**
 * Created by Aaron on 9/27/2014.
 */
public class StringUtils {

    /**
     * Copies and Replaces the first instance of substring in string with replacer.
     *
     * @param string The base string.
     * @param substring string to replace
     * @param replacer string to replace with
     * @param ignoreCase ignore case sensitivity?
     * @return a new string as described above.
     */
    public static String replaceFirst(String string, String substring, String replacer, boolean ignoreCase) {
        return replaceAtIndex(string, substring, replacer, 1, ignoreCase);
    }

    /**
     * Copies and Replaces the last instance of substring in string with replacer.
     *
     * @param string The base string.
     * @param substring string to replace
     * @param replacer string to replace with
     * @param ignoreCase ignore case sensitivity?
     * @return a new string as described above.
     */
    public static String replaceLast(String string, String substring, String replacer, boolean ignoreCase) {
        int[] indices = containsStringIndices(string, substring, ignoreCase);
        return replaceAtIndex(string, substring, replacer, indices.length, ignoreCase);
    }

    /**
     * Copies and Replaces all instances of substring in string with replacer.
     *
     * @param string The base string.
     * @param substring string to replace
     * @param replacer string to replace with
     * @param ignoreCase ignore case sensitivity?
     * @return a new string as described above.
     */
    public static String replaceAll(String string, String substring, String replacer, boolean ignoreCase) {
        int[] indices = containsStringIndices(string, substring, ignoreCase);
        //String toRet = new String(string);
        /*while (indices.length > 0) {
            toRet = replaceFirst(toRet, substring, replacer, ignoreCase);
            indices = containsStringIndices(toRet, substring, ignoreCase);
        }*/
        StringBuilder sb = new StringBuilder();
        mainLoop:
        for (int i = 0; i < string.length(); i++) {
            for (int index : indices) {
                if (i == index) {
                    i = (index + substring.length() - 1);
                    sb.append(replacer);
                    continue mainLoop;
                }
            }
            sb.append(string.charAt(i));
        }
        return sb.toString();
    }

    /**
     * Copies and Replaces the given instance of substring in string with replacer.
     *
     * @param string The base string.
     * @param substring string to replace
     * @param replacer string to replace with
     * @param occurance the occurance to replace
     * @param ignoreCase ignore case sensitivity?
     * @return a new string as described above.
     */
    public static String replaceAtIndex(String string, String substring, String replacer, int occurance, boolean ignoreCase) {
        int[] indices = containsStringIndices(string, substring, ignoreCase);
        StringBuilder sb = new StringBuilder();
        int index = occurance - 1;
        for (int i = 0; i < string.length(); i++) {
            if (i == indices[index]) {
                i = (indices[index] + substring.length() - 1);
                sb.append(replacer);
                continue;
            }
            sb.append(string.charAt(i));
        }
        return sb.toString();
    }

    /**
     * Checks if substring is inside of string.
     *
     * @param string The base string.
     * @param substring The string to check for.
     * @param ignoreCase ignore case sensitivity?
     * @return true if substring is inside of string, false otherwise
     */
    public static boolean containsString(String string, String substring, boolean ignoreCase) {
        return containsStringCount(string, substring, ignoreCase) > 0;
    }

    /**
     * Gets the number of times substring is inside of string.
     *
     * @param string The base string.
     * @param substring The string to check for.
     * @param ignoreCase ignore case sensitivity?
     * @return number of times substring occurs
     */
    public static int containsStringCount(String string, String substring, boolean ignoreCase) {
        return containsStringIndices(string, substring, ignoreCase).length;
    }

    /**
     * Gets the all the indices for the first letter of substring for all the times it occurs inside of string.
     *
     * @param string The base string.
     * @param substring The string to check for.
     * @param ignoreCase ignore case sensitivity?
     * @return array of indices
     */
    public static int[] containsStringIndices(String string, String substring, boolean ignoreCase) {
        LinkedList<Integer> matches = new LinkedList<Integer>();
        /* make sure we have good strings */
        if (string == null) throw new IllegalArgumentException("String cannot be null.");
        if (substring == null) throw new IllegalArgumentException("Substring cannot be null.");
        if (substring.isEmpty()) throw new IllegalArgumentException("Substring cannot be empty.");
        if (string.isEmpty())  throw new IllegalArgumentException("String cannot be empty.");

        /* Convert out strings to iterators */
        LinkedList<Character> chList = getCharList(string);
        LinkedList<Character> subchList = getCharList(new String(substring));
        /* Declare some variables, get our first substring character */
        int chIndex = 0;
        int subchIndex = 0;
        /* marker points for indices; denotes the starting index of our current check */
        int chIndexMarker = 0;
        /* current chars */
        char ch = chList.get(chIndex);
        char subch = subchList.get(subchIndex);
        int matching = 0;
        do {
            boolean equals = false;
            /* check if our characters are equal */
            if (ignoreCase) {
                if (subch == ch) {
                    equals = true;
                } else if (subch >= 64 && subch <= 90) {
                    /* convert upper case to lower case */
                    if (subch + 32 == ch) {
                        equals = true;
                    }
                } else if (subch >= 96 && subch <= 122) {
                    /* convert lower case to upper case */
                    if (subch - 32 == ch) {
                        equals = true;
                    }
                }
            } else {
                if (subch == ch) {
                    equals = true;
                }
            }
            /* Add to matching characters */
            if (equals) {
                /* we have a matching char lets continue */
                matching++;
                if (substring.length() == matching) {
                    /* We have a matchin string! Reset our values! */
                    subchIndex = 0;
                    matching = 0;
                    /* Add it to our list of indices */
                    matches.add(chIndexMarker);
                    chIndexMarker = ++chIndex;
                } else {
                    chIndex++;
                    subchIndex++;
                }
                /* set our char variables for next iteration */
                if (chList.size() <= chIndex) {
                    ch = 0;
                } else {
                    ch = chList.get(chIndex);
                }
                subch = subchList.get(subchIndex);
            } else {
                /* no match, reset substring */
                matching = 0;
                chIndex = chIndexMarker += 1;
                /* check if we have any more characters to compare */
                if (chList.size() <= chIndex) {
                    ch = 0;
                } else {
                    ch = chList.get(chIndex);
                }
                subchIndex = 0;
                subch = subchList.get(subchIndex);
            }


        } while (ch != 0);
        return ArrayUtils.toPrimative(matches.toArray(new Integer[matches.size()]));
    }

    /**
     * Converts the string to an array of characters.
     *
     * @param string
     * @return
     */
    private static LinkedList<Character> getCharList(String string) {
        LinkedList<Character> it = new LinkedList<Character>();
        for (char c : string.toCharArray()) {
            it.addLast(c);
        }
        return it;
    }
}
