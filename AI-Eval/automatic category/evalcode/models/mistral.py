import os
from mistralai import Mistral
from .base_model import BaseModel


class MistralSmallLatestModel(BaseModel):
    def __init__(self):
        self.delay_in_ms = 2000  # 2 seconds delay due to rate limiting in free tier
        self.client = Mistral(api_key=os.environ.get("MISTRAL_API_KEY"))
        self.model = "mistral-small-latest"

    def prompt(self, prompt_text: str) -> str:
        chat_response = self.client.chat.complete(
            model = self.model,
            messages = [
                {
                    "role": "user",
                    "content": prompt_text,
                },
            ],
            temperature=0.1,
            max_tokens=2,
        )
        return chat_response.choices[0].message.content



