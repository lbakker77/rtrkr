package de.lbakker77.retracker.main.ai;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.ai.mistralai.MistralAiChatOptions;
import org.springframework.ai.mistralai.api.MistralAiApi;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AutoCategoryServiceEvalTests {
    MistralAiChatModel getMistralChatModel() {
        var mistralAiApi = new MistralAiApi(System.getenv("MISTRAL_API_KEY"));
        var mistralOptions = MistralAiChatOptions.builder()
                .model(MistralAiApi.ChatModel.SMALL.getValue())
                .temperature(0.1)
                .maxTokens(2)
                .build();

        return MistralAiChatModel.builder().mistralAiApi(mistralAiApi).defaultOptions(mistralOptions).build();
    }

    @Disabled
    @Test
    void mistralComplectionWorks()  {
        var autoCategoryService = new AutoCategoryService(getMistralChatModel());
        var category = autoCategoryService.getAutoCategory("Kontaktlinsen wechseln");
        assertNotNull(category);
    }
}
