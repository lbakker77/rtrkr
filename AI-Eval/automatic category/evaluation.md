# Evaluation #

This document contains evaludation guidelines used to test and select an ai model for rtrkr's automatic category assignmemt

The output of the ai module does not need to be of very high quality since the categries can be changed by the user manually if required. Nevertheless the ai selection should be fitting in > 95% of the cases.

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

- wrong category: -2
- right category: 1
- ok: 0,5
- no selection: 0

## Evaulation set ##

2 sets are used for evaluation. 
1. German
2. English

Size: 400 each.

The training set is ai generated but reviewed manually.
