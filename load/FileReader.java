package uk.gov.gchq.gaffer.utils.load;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileReader {

    public List<String> readFile(String fileToRead) {

        List<String> interactions = new ArrayList<>();

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileToRead))) {
            stream.forEach(line -> interactions.add(line));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return interactions;
    }
}
