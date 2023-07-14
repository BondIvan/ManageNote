package Tools.Converting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConvertCharArrayToLists {

    // Конвертация двумерного массива из Alphabet в список characters
    public static List<List<Character>> convert(char[][] alpha) {

        List<List<Character>> resultCharList = new ArrayList<>();
        // Перевод char[] в список characters1
        for( char[] ch: alpha ) {
            Stream<Character> stream = IntStream
                    .range(0, ch.length) // Типо счётчик i: от 0 до 5
                    .mapToObj(i -> ch[i]); // Каждый ch[i] преобразовывается в Object -> Stream<Character>

            resultCharList.add( stream.collect(Collectors.toList()) );
        }

        return resultCharList;
    }

}
