package pies.Reducer.api;

import com.google.common.collect.Sets;

import java.util.Locale;
import java.util.Set;

public class Filter {
    public static Set<String> ChatFilter = Sets.newHashSet(
            "nigga", "niga", "nigger", "kys", "kill your self", "porn", "retard", "faggot",
            "fuck", "shit", "gay", "bitch", "lesbic", "lgbtq"
    );
    public static boolean checkProfanity(String text) {
        String t = text.toLowerCase(Locale.ROOT);

        Boolean isProfrain = false;
        for (String s : ChatFilter) {
            if (t.contains(s)) isProfrain = true;
            else if (t.replace("@", "a").contains(s)) isProfrain = true;
            else if (t.replace("$", "s").contains(s)) isProfrain = true;
            else if (t.replace("()", "o").contains(s)) isProfrain = true;
            else if (t.replace("()", "0").contains(s)) isProfrain = true;
            else if (t.replace("()", ".").contains(s)) isProfrain = true;
            else if (t.replace("0", "o").contains(s)) isProfrain = true;
            else if (t.replace("3", "e").contains(s)) isProfrain = true;
            else if (t.replace("!", "i").contains(s)) isProfrain = true;
        }

        return isProfrain;
    }
}
