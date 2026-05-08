package dev.terfehr.gymtrackerapi.utils;

import java.util.Collection;
import java.util.HashSet;

public class AssertionUtils {

    public static boolean doesNotContainDuplicates(Collection<?> collection) {
        return new HashSet<>(collection).size() == collection.size();
    }
}
