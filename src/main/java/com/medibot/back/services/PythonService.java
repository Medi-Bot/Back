package com.medibot.back.services;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PythonService {

    public String executePythonScript(String scriptPath, String... args) {
        try {
            List<String> command = new ArrayList<>();
            command.add("python");
            command.add(scriptPath);
            for (String arg : args) {
                command.add(arg);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            return output.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error executing script: " + e.getMessage();
        }
    }
}