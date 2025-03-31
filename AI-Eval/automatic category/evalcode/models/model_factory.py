from .base_model import BaseModel
from .foomodel import FooModel
from .gemini import GeminiFlashLightModel, GeminiFlashModel, Gemma3Model
from .lmstudio import Phi4MiniInstructModel, Phi4Model,MistralSmallInstructModel, Llama8BModel
from .mistral import MistralSmallLatestModel

from enum import Enum

class Models(str, Enum):
    FOO = "FooModel"
    GEMINI2FLASHLIGHT = "Gemini2FlashLightModel"
    GEMINI2FLASH = "Gemini2FlashModel"
    GEMMA3 = "Gemma3Model"
    MISTRALSMALL = "MistralSmallLatestModel"
    PHI4MINI = "Phi4MiniInstructModel"
    PHI4 = "Phi4Model"
    MISTRALSMALLINSTRUCT = "MistralSmallInstructModel"
    LLAMA8B = "Llama8BModel"


def get_model_instance(model_name: str) -> BaseModel:
    match model_name:
        case Models.FOO:
            return FooModel()
        case Models.GEMINI2FLASHLIGHT:
            return GeminiFlashLightModel()
        case Models.GEMINI2FLASH: 
            return GeminiFlashModel()
        case Models.MISTRALSMALL:
            return MistralSmallLatestModel()
        case Models.GEMMA3:
            return Gemma3Model()
        case Models.PHI4MINI:
            return Phi4MiniInstructModel()
        case Models.PHI4:
            return Phi4Model()
        case Models.MISTRALSMALLINSTRUCT:
            return MistralSmallInstructModel()
        case Models.LLAMA8B:
            return Llama8BModel()
        case _:
            raise ValueError(f"Unsupported model: {model_name}")