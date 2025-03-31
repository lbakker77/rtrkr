# Evaluation #

This document contains evaludation guidelines used to test and select an ai model for rtrkr's automatic category assignmemt

The output of the ai module does not need to be of very high quality since the categries can be changed by the user manually if required. Nevertheless the ai selection should be fitting in > 95% of the cases.
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

## Result of mistral small latest model



## Google Gemini 2.0 flash lite

Pricing 
Input / M tokens:  $ 0.075 
Output / M tokens: $ 0.30 €


## Mistral Small 3.1 (la platform)

Pricing 
Input / M tokens:  0.10 €
Output / M tokens: 0.30 €

## Mistral Small 3.1 Instruct via LM Studio

Model:         MistralSmallInstructModel
Total tasks:   199
Success rate:  86.93%
Score:         136.5
Success:       173
Failures:      19
Errors:        1
General:       7

## Gemma 3 via LM Studio

Pricing 
? Not available as payed model on google ai ?



## Phi4 via LM Studio

Temperatur: 0

Model:         Phi4Model
Total tasks:   199
Success rate:  92.96%
Score:         155.0
Success:       185
Failures:      14
Errors:        1
General:       0

Latency:       ? local execution



# Phi4 Mini Instruct via LM Studio

Model:         Phi4MiniInstructModel
Total tasks:   192
Success rate:  73.44%
Score:         25.5
Success:       141
Failures:      50
Errors:        8
General:       1

## LLAMA 8B Instruct via LM Studio

Model:         Llama8BModel
Total tasks:   200
Success rate:  84.00%
Score:         104.0
Success:       168
Failures:      32
Errors:        0
General:       0
Latency (avg): 852.66 ms
Latency (max): 7819.67 ms
Latency (min): 615.14 ms
