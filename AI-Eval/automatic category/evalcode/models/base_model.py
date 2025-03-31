from abc import ABC, abstractmethod
class BaseModel(ABC):
    def __init__(self):
        self.delay_in_ms = 0

    @abstractmethod
    def prompt(self, prompt_text: str) -> str:
        pass