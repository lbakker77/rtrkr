# Evaluation #

This document contains evaludation guidelines used to test and select an ai model for rtrkr's automatic category assignmemt

The output of the ai module does not need to be of very high quality since the categries can be changed by the user manually if required. Nevertheless the ai selection should be fitting in > 90% of the cases.
The most important aspect besides selection quality is latency, as the category need to be assigned when the user creates a new task. 

Due to the expectations only Small Language Models are evaluated for this feature. They are expected to be good enough for this use case and costs are much lower.

## Expectations ##

The AI module selects a category from a given list of categories based on the tasks title. 

## Following Instruction ##
The AI module must select one of the provided categories. 
1. Selection must fall within defined categories
2. No extra category must be generated / returned

## Category selection qualtity ##

The selection is is either 
1. wrong ("call mother" categorizes in category financials)
2. right ("call mother" in categories "private" or "family and friends")
3. ok ("call mother" in category "general")
4. no selection. non-existant selection

For all provided examples the selection should only be wrong in max 5 percent of the test cases. the general category should only be used rarly (max 10% of the test cases) 

Scoring: 

- wrong category / instructions not followed: -2
- right category: 1
- ok: 0.5
- failure: -2

## Evaulation set ##

Languages used in evaluation. 
1. German
2. English

Size of test set: 200 

The test set is partly ai generated but failures are reviewed manually.

## Execution
For evaluation a small python program is used that performs the categorization tasks for the same test set for all ai models in the long list. See sub folder evalcode.

The temperature of the models used is set to 0 since creativity is not desired for categorization.

The prompt is the same for all models. No model specific optimization is performed in this step.

## Evaluation Results

### Google Gemini 2.0 flash lite

**Pricing** 

Input per M tokens:  $ 0.075 

Output per M tokens: $ 0.30 €


 Metric                | Value       |
 |-----------------------|-------------|
 | **Model Name**        | Gemini2FlashLightModel |
 | **Total Tasks**       | 200         |
 | **Success Rate**      | 92.50%      |
 | **Score**             | 155.0       |
 | **Successful Tasks**  | 185         |
 | **Failed Tasks**      | 15          |
 | **Errors**            | 0           |
 | **General**           | 0           |
 | **Average Latency**   | 400.44 ms   |
 | **Maximum Latency**   | 755.04 ms   |
 | **Minimum Latency**   | 339.92 ms   |

### Mistral Small 3.1 (la platform)

**Pricing** 

Input per M tokens:  0.10 €
Output per M tokens: 0.30 €

 Metric                | Value       |
 |-----------------------|-------------|
 | **Model Name**        | MistralSmallLatestModel |
 | **Total Tasks**       | 200         |
 | **Success Rate**      | 92.50%      |
 | **Score**             | 157.5       |
 | **Successful Tasks**  | 185         |
 | **Failed Tasks**      | 13          |
 | **Errors**            | 1           |
 | **General**           | 1           |
 | **Average Latency**   | 486.10 ms   |
 | **Maximum Latency**   | 4814.99 ms  |
 | **Minimum Latency**   | 223.06 ms   |


### Mistral Small 3.1 Instruct via LM Studio

Kopieren
  Metric                | Value       |
 |-----------------------|-------------|
 | **Model Name**        | MistralSmallInstructModel |
 | **Total Tasks**       | 200         |
 | **Success Rate**      | 89.50%      |
 | **Score**             | 152.0       |
 | **Successful Tasks**  | 179         |
 | **Failed Tasks**      | 15          |
 | **Errors**            | 0           |
 | **General**           | 6           |

### Gemma 3 12B it via LM Studio

Kopieren
  Metric                | Value       |
 |-----------------------|-------------|
 | **Model Name**        | Gemma312BModel |
 | **Total Tasks**       | 200         |
 | **Success Rate**      | 95.00%      |
 | **Score**             | 170.0       |
 | **Successful Tasks**  | 190         |
 | **Failed Tasks**      | 10          |
 | **Errors**            | 0           |
 | **General**           | 0           |


### Phi4 via LM Studio

Kopieren
  Metric                | Value       |
 |-----------------------|-------------|
 | **Model Name**        | Phi4Model   |
 | **Total Tasks**       | 200         |
 | **Success Rate**      | 93.47%      |
 | **Score**             | 158.0       |
 | **Successful Tasks**  | 186         |
 | **Failed Tasks**      | 13          |
 | **Errors**            | 1           |
 | **General**           | 0           |


### Phi4 Mini Instruct via LM Studio

 Metric                | Value       |
 |-----------------------|-------------|
 | **Model Name**        | Phi4MiniInstructModel |
 | **Total Tasks**       | 192         |
 | **Success Rate**      | 73.44%      |
 | **Score**             | 25.5        |
 | **Successful Tasks**  | 141         |
 | **Failed Tasks**      | 50          |
 | **Errors**            | 8           |
 | **General**           | 1           |

### LLAMA 8B Instruct via LM Studio

 Metric                | Value       |
 |-----------------------|-------------|
 | **Model Name**        | Llama8BModel |
 | **Total Tasks**       | 200         |
 | **Success Rate**      | 84.00%      |
 | **Score**             | 104.0       |
 | **Successful Tasks**  | 168         |
 | **Failed Tasks**      | 32          |
 | **Errors**            | 0           |
 | **General**           | 0           |