import os
import time
from test_model import get_test_cases, TestResult
from models import get_model_instance, Models
import logging
import argparse

class PromptTemplate: 
    def __init__(self):
        this_directory = os.path.dirname(os.path.realpath(__file__))
        template_file_path = os.path.join(this_directory, "prompt-template.txt")     
        with open(template_file_path, "r", encoding="utf-8") as f:
            self.template = f.read()

    def create(self, task_title: str) -> str:
        return self.template.format(task_title)


def run_simulation(model_name: str):
    testcases = get_test_cases()
    prompt_template = PromptTemplate()
    model = get_model_instance(model_name)
    test_result = TestResult(model_name)

    for testcase in testcases:
        prompt = prompt_template.create(testcase.taskName)
        try:
            start_time = time.time()
            response_text = model.prompt(prompt)
            stop_time = time.time()
            latencyMs = (stop_time - start_time) * 1000
            test_result.add_test_result(testcase, response_text, latencyMs)
        except Exception as e:
            test_result.register_failure(testcase, str(e))
        if (model.delay_in_ms > 0):
            time.sleep(model.delay_in_ms / 1000)  

    test_result.print_summary()    


def main():
    model_choices = [model.value for model in Models]
    parser = argparse.ArgumentParser(description="Run AI model evaluation")
    parser.add_argument("--model", default=Models.MISTRALSMALL.value, help=f"Name of the model to evaluate", choices=model_choices)
    args = parser.parse_args()
    logging.basicConfig(level=logging.INFO)
    run_simulation(args.model)


if __name__ == "__main__":
    main()

