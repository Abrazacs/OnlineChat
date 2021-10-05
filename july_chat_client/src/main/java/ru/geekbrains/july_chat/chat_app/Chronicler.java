package ru.geekbrains.july_chat.chat_app;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chronicler {
    private final String HISTORY_PATH = "history/";
    private File historyFile;

    public Chronicler(String login){
        historyFile = new File(HISTORY_PATH+"history"+login+".txt");
    }

    public void writeHistory(String message){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile, true));){
            writer.write(message);
        }catch (IOException e){
        e.printStackTrace();
        }
    }

    public List<String> readHistory() {
        if(!historyFile.exists()) return Collections.singletonList("");
        List<String> latestHundredRecords = null;
        if (historyFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
                String record;
                List<String> chronicle = new ArrayList<>();
                while ((record = reader.readLine()) != null) {
                    chronicle.add(record);
                }
                if (chronicle.size() <= 100) {
                    return chronicle;
                } else {
                    for (int i = chronicle.size() - 101; i < chronicle.size(); i++) {
                        latestHundredRecords.add(chronicle.get(i));
                    }
                    return latestHundredRecords;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return latestHundredRecords;
    }

}

