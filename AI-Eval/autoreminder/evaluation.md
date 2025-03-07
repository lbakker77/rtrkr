# Evaluation #

This document contains the evaludation guidelines used to test and select an ai model for re-tracker automatic reminders.

The output of the ai should be high quality as we don't want to bother the users with unwanted reminders that do not help. 

Also the automatic reminders may not require an update

## Expectations ##

The AI generates a suggestion for an automatic reminder if this makes sense for a certain task. The reminder is shown at least 2 days before the due date for the task. 

The AI module uses the following information a input
- title of the task
- due date
- last 3 execution dates with information of postponed days and overdue execution
- last reminder date + user feedback on last reminder if any (too early, too late)

The following output is expected:
- reminder yes / no
- reminder date as days before duedate
- explaination text (max 40 words)

If it does not make sense to show a reminder for a specific task, the ai module shouldn't return a reminder date. 

## Reminder yes / no ##

Here are a couple of examples with expectations on whether the ai module should generate reminders yes or no.

**no reminder**
1. For a task with the title "meditation session" with a recurrance interval of one week, a reminder is not expected
2. Task "chance contact lenses" every 4 weeks should not have a reminder.
3. Task "test" should not have a reminder (too generic)
4. Task Susi should not have a reminder

**reminder**
1. A yearly task "Weekend with friends" should have a reminder. E.g. 4 weeks ahead of the duedate. 
2. A task "Ina vaccine" every year should have a reminder of e.g. 10 days before duedate since it may require an appointment at the vet / doctor. We don't know if Ina is a person or an animal.


## Reminder Dates ## 
The suggested date to show a reminder for a certain task 
1. must be at least 2 days before the configured due date
2. must be after the current date

## Text response ##
The text should be directly related to the tasks title
1. The text must not be generic 
2. The text should contain the tasks title
3. The text must be in the users language (de, en, future: more languages)
4. The text must only be related to the task execution. no other topics should appear. E.g. task "new video on politics) should not discuss political issures. Or task "sex" should not contain more advice.
5. the response is save / not toxic / friendly.



