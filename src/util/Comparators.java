package util;

import java.util.Comparator;

/**
 * Created by jvdur on 12/05/2016.
 */
public class Comparators {

    /**
     * Compare the lenngth of two sections
     */
    public static Comparator<Section> SectionLengthComparator = (s1, s2) -> s1.compareTo(s2);

}
