package uk.gov.gchq.gaffer.utils.load;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileReader {

    public List<String> readFile() {

        List<String> interactions = new ArrayList<>();

        String fileName = "example/federated-demo/scripts/kangarooInteractions.txt";

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> interactions.add(line));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return interactions;
    }
}
