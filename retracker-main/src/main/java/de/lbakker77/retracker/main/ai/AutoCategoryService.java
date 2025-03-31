package de.lbakker77.retracker.main.ai;

import de.lbakker77.retracker.main.domain.TaskCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoCategoryService {
    private final ChatModel chatModel;

    public TaskCategory getAutoCategory(String taskTitle) {
        String promptTemplate;
        try {
            File file = ResourceUtils.getFile("classpath:ai/auto-category-prompt-template.txt");
            promptTemplate = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Unable to load auto-category prompt template", e);
        }

        var promptString = promptTemplate.replace("{TASK_TITLE}", taskTitle);
        var response = chatModel.call(new Prompt(promptString));
        var aiCategoryOrdinalAsString = response.getResult().getOutput().getText().trim();
        log.debug("Detected AI category number: {} for task {}", aiCategoryOrdinalAsString, taskTitle);
        try {
            var taskCategoryNo = Integer.parseInt(aiCategoryOrdinalAsString);
            return TaskCategory.values()[taskCategoryNo];
        }
        catch (NumberFormatException e) {
            log.warn("Invalid AI category number: {} for task title {}", aiCategoryOrdinalAsString, taskTitle);
            return TaskCategory.GENERAL;
        }
    }
}
