import os
from google import genai
from google.genai import types
from .base_model import BaseModel

class GeminiFlashLightModel(BaseModel):
    def __init__(self): 
        self.delay_in_ms = 2500 # Max RPM in free tier is 30 RPM 
        self.client = genai.Client(
            api_key=os.environ.get("GEMINI_API_KEY"),
        )
        self.model = "gemini-2.0-flash-lite"
        self.generate_content_config = types.GenerateContentConfig(
            temperature=0.1,
            max_output_tokens=2,
            response_mime_type="text/plain",
        )
        types.ModelDict

    def prompt(self, prompt_text: str) -> str:
        contents = [
            types.Content(
                role="user",
                parts=[
                    types.Part.from_text(text=prompt_text),
                ],
            )
        ]
        response = self.client.models.generate_content(
            model=self.model,
            contents=contents,
            config=self.generate_content_config,
        )
        return response.text
    
    
class GeminiFlashModel(GeminiFlashLightModel):
    def __init__(self):
        super().__init__()
        self.model = "gemini-2.0-flash"
        self.delay_in_ms = 4500 # Max RPM in free tier is 15 RPM 

class Gemma3Model(GeminiFlashLightModel):
    def __init__(self):
        super().__init__()
        self.model = "gemma-3-27b-it"
        self.delay_in_ms = 2500 # 