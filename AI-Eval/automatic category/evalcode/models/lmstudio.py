from openai import OpenAI
from .base_model import BaseModel

class Phi4MiniInstructModel(BaseModel):
    def __init__(self):
        self.delay_in_ms = 0
        self.client = OpenAI(base_url="http://localhost:1234/v1", api_key="lm-studio")
        self.model = "phi-4-mini-instruct"

    def prompt(self, prompt_text: str) -> str:
        completion = self.client.chat.completions.create(
            model=self.model,
             messages=[
                {"role": "user", "content": prompt_text}
            ],
            temperature=0,
            max_tokens=2
        )
        return completion.choices[0].message.content

class Phi4Model(Phi4MiniInstructModel):
    def __init__(self):
        super().__init__()
        self.model = "phi-4"

class MistralSmallInstructModel(Phi4MiniInstructModel):
    def __init__(self):
        super().__init__()
        self.model = "mistralai_mistral-small-3.1-24b-instruct-2503"

class Llama8BModel(Phi4MiniInstructModel):
    def __init__(self):
        super().__init__()
        self.model = "meta-llama-3.1-8b-instruct"


class Gemma312BModel(Phi4MiniInstructModel):
    def __init__(self):
        super().__init__()
        self.model = "gemma-3-12b-it"