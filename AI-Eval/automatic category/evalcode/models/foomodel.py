
import random
from .base_model import BaseModel

class FooModel(BaseModel):
    valid_response_values = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"]

    def __init__(self):
        self.delay_in_ms = 100 # Simulate a delay of 100ms

    def prompt(self, prompt_text: str) -> str:
        return random.choice(self.valid_response_values)
