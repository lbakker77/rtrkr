package de.lbakker77.retracker.main.ai;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AiCategoryModelConfig.class)
@Tag("integration")
@TestPropertySource(properties = {
        "ai.category.base-url=http://127.0.0.1:1234",
        "ai.category.api-key=doesNotMatter"
})
class AutoCategoryServiceIntegrationTests {

    @Autowired
    private ChatModel chatModel;

    @Disabled("Can only run locally with lm studio running")
    @Test
    void expectAiCategorizationToWork()  {
        var autoCategoryService = new AutoCategoryService(chatModel);
        var category = autoCategoryService.getAutoCategory("Kontaktlinsen wechseln");
        assertNotNull(category);
    }
}
