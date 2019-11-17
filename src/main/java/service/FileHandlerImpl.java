package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileHandlerImpl implements FileHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FileHandlerImpl.class);

    private static final String LOG_FOLDER_PATH = "logs";

    private StatService statService;
    private LineParser lineParser;

    public FileHandlerImpl(StatService statService, LineParser lineParser) {
        this.statService = statService;
        this.lineParser = lineParser;
    }

    public boolean handle() {
        var logFolderPath = getClass()
                .getClassLoader()
                .getResource(LOG_FOLDER_PATH)
                .getPath();
        LOG.info("Start handle log files from folder {}", logFolderPath);
        var logFolder = new File(logFolderPath);
        var logFiles = logFolder.listFiles();
        if (logFiles == null || logFiles.length == 0) {
            LOG.info("There are no files in folder {}", logFolderPath);
            return false;
        }
        for (var file : logFiles) {
            LOG.info("Get file {}", file.getName());
            try {
                handleFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        LOG.info("Finish handle log files");
        return true;
    }

    private void handleFile(File file) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()))) {
            stream.forEach(s ->
                    statService.addEvent(lineParser.parse(s))
            );
        }
    }
}
