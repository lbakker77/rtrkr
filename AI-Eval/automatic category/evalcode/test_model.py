from enum import Enum

class TaskCategory(Enum):
    GENERAL = 0
    ADMINISTRATIVE = 1
    PERSONAL = 2
    WORK_PROFESSIONAL = 3
    FINANCIAL = 4
    HEALTH_WELLNESS = 5
    HOME_MAINTENANCE = 6
    SOCIAL_RELATIONSHIPS = 7
    EDUCATION_LEARNING = 8
    CREATIVE = 9
    TRAVEL_LOGISTICS = 10
    ERRANDS_SHOPPING = 11

class TestCase:
    categories = []
    def __init__(self, taskName: str, *categories: TaskCategory):
        self.taskName = taskName
        self.categories = categories

class TestResult:
    def __init__(self, model_name: str):
        self.modelName = model_name
        self.successCount = 0
        self.overallCount = 0
        self.failures: list[tuple[TestCase,TaskCategory]] = []
        self.generalAssinments: list[TestCase] = []
        self.errors: list[tuple[TestCase,str]] = []
        self.latencyMsList: list[float] = []

    def add_test_result(self, test_case: TestCase, responseText: str, latencyMs: float):
        self.overallCount += 1
        try:
            retvalInt = int(responseText)
            category = TaskCategory(retvalInt)
            if category in test_case.categories:
                self.successCount += 1
            elif (category == TaskCategory.GENERAL):
                self.generalAssinments.append(test_case)
            else:
                self.failures.append((test_case, category))
            self.latencyMsList.append(latencyMs)
        except ValueError:
            self.errors.append((test_case, responseText))
    
    def register_failure(self, test_case: TestCase, error: str):
        self.overallCount += 1
        self.errors.append((test_case, error))
    

    def print_summary(self):
        print(f"Model:         {self.modelName}")
        print(f"Total tasks:   {self.overallCount}")
        print(f"Success rate:  {self.successCount / self.overallCount:.2%}")
        print(f"Score:         {self.successCount + len(self.generalAssinments) * 0.5 - (len(self.errors) + len(self.failures)) * 2 }")
        print(f"Success:       {self.successCount}")
        print(f"Failures:      {len(self.failures)}")
        print(f"Errors:        {len(self.errors)}")
        print(f"General:       {len(self.generalAssinments)}")
        if self.successCount > 0:
            print(f"Latency (avg): {sum(self.latencyMsList) / len(self.latencyMsList):.2f} ms")
            print(f"Latency (max): {max(self.latencyMsList):.2f} ms")
            print(f"Latency (min): {min(self.latencyMsList):.2f} ms")
        if self.failures:
            print()
            print("\nFailures:")
            for test_case, category in self.failures:
                print(f"- {test_case.taskName} (Expected: {test_case.categories}, Actual: {category.name})")
        if self.generalAssinments:
            print()
            print("\nGeneral assignments:")
            for test_case in self.generalAssinments:
                print(f"- {test_case.taskName} (Expected: {test_case.categories})")
        if self.errors:
            print()
            print("\nErrors:")
            for test_case, error in self.errors:
                print(f"- {test_case.taskName}: Returned: {error}")

def get_test_cases() -> list[TestCase]:
    # Define test cases here
    test_cases = [
        TestCase("Meditation practice", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),
        TestCase("Team meeting", TaskCategory.WORK_PROFESSIONAL),
        TestCase("Joggingrunde", TaskCategory.HEALTH_WELLNESS), 
        TestCase("Budget review", TaskCategory.FINANCIAL, TaskCategory.PERSONAL),
        TestCase("Water indoor plants", TaskCategory.HOME_MAINTENANCE),
        TestCase("Großeinkauf", TaskCategory.ERRANDS_SHOPPING), 
        TestCase("Language learning practice", TaskCategory.EDUCATION_LEARNING, TaskCategory.PERSONAL),
        TestCase("Meal prep", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL, TaskCategory.HOME_MAINTENANCE),
        TestCase("House cleaning", TaskCategory.HOME_MAINTENANCE),
        TestCase("Charity donation", TaskCategory.FINANCIAL, TaskCategory.PERSONAL),
        TestCase("Anruf bei den Eltern", TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Check work emails", TaskCategory.WORK_PROFESSIONAL),
        TestCase("Write blog post", TaskCategory.CREATIVE, TaskCategory.WORK_PROFESSIONAL),
        TestCase("Frisör", TaskCategory.PERSONAL), 
        TestCase("Car wash", TaskCategory.HOME_MAINTENANCE, TaskCategory.ERRANDS_SHOPPING),
        TestCase("Create social media content", TaskCategory.CREATIVE, TaskCategory.WORK_PROFESSIONAL),
        TestCase("Do laundry", TaskCategory.HOME_MAINTENANCE),
        TestCase("Vitamintabletten einnehmen", TaskCategory.HEALTH_WELLNESS),
        TestCase("Review credit card statement", TaskCategory.FINANCIAL),
        TestCase("Vacuum cleaning", TaskCategory.HOME_MAINTENANCE),
        TestCase("Feed pets", TaskCategory.HOME_MAINTENANCE, TaskCategory.PERSONAL),
        TestCase("Gießen der Gartenpflanzen", TaskCategory.HOME_MAINTENANCE),
        TestCase("Return library books", TaskCategory.EDUCATION_LEARNING, TaskCategory.ERRANDS_SHOPPING),
        TestCase("Attend networking event", TaskCategory.WORK_PROFESSIONAL, TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Take out trash and recycling", TaskCategory.HOME_MAINTENANCE),
        TestCase("Journal work progress", TaskCategory.WORK_PROFESSIONAL, TaskCategory.PERSONAL),
        TestCase("Besuch bei Großeltern", TaskCategory.SOCIAL_RELATIONSHIPS), 
        TestCase("Guitar practice", TaskCategory.CREATIVE, TaskCategory.PERSONAL),
        TestCase("Meditate", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),
        TestCase("Grocery shopping", TaskCategory.ERRANDS_SHOPPING),
        TestCase("Car maintenance check", TaskCategory.HOME_MAINTENANCE),
        TestCase("Yoga-Kurs", TaskCategory.HEALTH_WELLNESS), 
        TestCase("Prioritize work tasks", TaskCategory.WORK_PROFESSIONAL),
        TestCase("Plan meals", TaskCategory.ERRANDS_SHOPPING, TaskCategory.HOME_MAINTENANCE),
        TestCase("Review investment portfolio", TaskCategory.FINANCIAL),
        TestCase("Lesen vor dem Schlafengehen", TaskCategory.EDUCATION_LEARNING, TaskCategory.PERSONAL), 
        TestCase("Deep clean bathroom", TaskCategory.HOME_MAINTENANCE),
        TestCase("Do work break stretches", TaskCategory.HEALTH_WELLNESS, TaskCategory.WORK_PROFESSIONAL, TaskCategory.PERSONAL),
        TestCase("Organize digital files", TaskCategory.WORK_PROFESSIONAL, TaskCategory.PERSONAL),
        TestCase("Call friend to catch up", TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Autowäsche", TaskCategory.HOME_MAINTENANCE),
        TestCase("Gratitude journaling", TaskCategory.PERSONAL, TaskCategory.HEALTH_WELLNESS),
        TestCase("Home security check", TaskCategory.HOME_MAINTENANCE),
        TestCase("Clean out fridge", TaskCategory.HOME_MAINTENANCE),
        TestCase("Date night", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),
        TestCase("Spende an Wohltätigkeitsorganisation", TaskCategory.FINANCIAL, TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Update work project", TaskCategory.WORK_PROFESSIONAL),
        TestCase("Review personal finances", TaskCategory.FINANCIAL, TaskCategory.PERSONAL),
        TestCase("Set and review goals", TaskCategory.PERSONAL, TaskCategory.WORK_PROFESSIONAL, TaskCategory.EDUCATION_LEARNING),
        TestCase("Fenster putzen", TaskCategory.HOME_MAINTENANCE), 
        TestCase("Book flight tickets", TaskCategory.TRAVEL_LOGISTICS, TaskCategory.PERSONAL),
        TestCase("Reisepass erneuern", TaskCategory.TRAVEL_LOGISTICS, TaskCategory.ADMINISTRATIVE), 
        TestCase("Pack suitcase for business trip", TaskCategory.TRAVEL_LOGISTICS, TaskCategory.WORK_PROFESSIONAL),
        TestCase("Research hotel options", TaskCategory.TRAVEL_LOGISTICS, TaskCategory.PERSONAL),
        TestCase("Arrange airport transfer", TaskCategory.TRAVEL_LOGISTICS),
        TestCase("Dentist appt", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),
        TestCase("Buy birthday gift", TaskCategory.ERRANDS_SHOPPING, TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Prepare presentation", TaskCategory.WORK_PROFESSIONAL),
        TestCase("Mow lawn", TaskCategory.HOME_MAINTENANCE),
        TestCase("Meditate", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),
        TestCase("File taxes", TaskCategory.FINANCIAL, TaskCategory.ADMINISTRATIVE),
        TestCase("Learn new recipe", TaskCategory.EDUCATION_LEARNING, TaskCategory.CREATIVE),
        TestCase("Attend parent-teacher conference", TaskCategory.EDUCATION_LEARNING, TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Update resume", TaskCategory.WORK_PROFESSIONAL, TaskCategory.PERSONAL),
        TestCase("Plan vacation", TaskCategory.TRAVEL_LOGISTICS, TaskCategory.PERSONAL),
        TestCase("Backup computer files", TaskCategory.WORK_PROFESSIONAL, TaskCategory.PERSONAL),
        TestCase("Schedule car inspection", TaskCategory.HOME_MAINTENANCE, TaskCategory.ERRANDS_SHOPPING),
        TestCase("Write thank-you notes", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL, TaskCategory.CREATIVE),
        TestCase("Attend yoga class", TaskCategory.HEALTH_WELLNESS),
        TestCase("Research investment options", TaskCategory.FINANCIAL),
        TestCase("Clean gutters", TaskCategory.HOME_MAINTENANCE),
        TestCase("Volunteer at food bank", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),
        TestCase("Create weekly meal plan", TaskCategory.HEALTH_WELLNESS, TaskCategory.HOME_MAINTENANCE),
        TestCase("Attend networking event", TaskCategory.WORK_PROFESSIONAL, TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Organize family photo albums", TaskCategory.PERSONAL, TaskCategory.CREATIVE, TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Schedule annual physical", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),
        TestCase("Learn new language", TaskCategory.EDUCATION_LEARNING, TaskCategory.PERSONAL),
        TestCase("Fix leaky faucet", TaskCategory.HOME_MAINTENANCE),
        TestCase("Plan retirement party", TaskCategory.WORK_PROFESSIONAL, TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Update emergency contact list", TaskCategory.PERSONAL, TaskCategory.ADMINISTRATIVE),
        TestCase("Attend art exhibition", TaskCategory.CREATIVE, TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Renew driver's license", TaskCategory.ADMINISTRATIVE, TaskCategory.PERSONAL, TaskCategory.ERRANDS_SHOPPING),
        TestCase("Plant vegetable garden", TaskCategory.HOME_MAINTENANCE, TaskCategory.CREATIVE),
        TestCase("Schedule dentist appointment", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),
        TestCase("Research new phone plans", TaskCategory.ERRANDS_SHOPPING, TaskCategory.FINANCIAL),
        TestCase("Organize garage sale", TaskCategory.HOME_MAINTENANCE, TaskCategory.FINANCIAL),
        TestCase("Take CPR class", TaskCategory.EDUCATION_LEARNING, TaskCategory.HEALTH_WELLNESS),
        TestCase("Plan family reunion", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),
        TestCase("Create budget spreadsheet", TaskCategory.FINANCIAL, TaskCategory.WORK_PROFESSIONAL),
        TestCase("Attend local town hall meeting", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),
        TestCase("Schedule chimney cleaning", TaskCategory.HOME_MAINTENANCE),
        TestCase("Write novel outline", TaskCategory.CREATIVE, TaskCategory.PERSONAL),
        TestCase("Research graduate programs", TaskCategory.EDUCATION_LEARNING, TaskCategory.PERSONAL),
        TestCase("Plan anniversary celebration", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),
        TestCase("Organize digital music library", TaskCategory.PERSONAL, TaskCategory.CREATIVE),
        TestCase("Schedule pet grooming", TaskCategory.HOME_MAINTENANCE, TaskCategory.ERRANDS_SHOPPING),
        TestCase("Attend cooking class", TaskCategory.EDUCATION_LEARNING, TaskCategory.CREATIVE),
        TestCase("Research solar panel installation", TaskCategory.HOME_MAINTENANCE, TaskCategory.FINANCIAL),
        TestCase("Plan charity fundraiser", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.WORK_PROFESSIONAL),
        TestCase("Create family tree", TaskCategory.PERSONAL, TaskCategory.CREATIVE),
        TestCase("HVAC maintenance", TaskCategory.HOME_MAINTENANCE),
        TestCase("Attend local farmers market", TaskCategory.ERRANDS_SHOPPING, TaskCategory.PERSONAL),
        TestCase("Research retirement communities", TaskCategory.PERSONAL, TaskCategory.FINANCIAL),
        TestCase("Plan wedding", TaskCategory.PERSONAL, TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Create workout playlist", TaskCategory.HEALTH_WELLNESS, TaskCategory.CREATIVE),
        TestCase("Organize neighborhood watch meeting", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.HOME_MAINTENANCE),
        TestCase("Schedule annual eye exam", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),
        TestCase("Research homeschooling options", TaskCategory.EDUCATION_LEARNING, TaskCategory.PERSONAL),
        TestCase("Plan home renovation project", TaskCategory.HOME_MAINTENANCE, TaskCategory.FINANCIAL),
        TestCase("Attend local book club meeting", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.EDUCATION_LEARNING),
        TestCase("Schedule annual skin check", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),
        TestCase("Research local volunteer opportunities", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),
        TestCase("Plan garage organization project", TaskCategory.HOME_MAINTENANCE),
        TestCase("Attend local art class", TaskCategory.CREATIVE, TaskCategory.EDUCATION_LEARNING),
        TestCase("Research energy-efficient appliances", TaskCategory.HOME_MAINTENANCE, TaskCategory.FINANCIAL),
        TestCase("Plan family game night", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),
        TestCase("Create personal mission statement", TaskCategory.PERSONAL, TaskCategory.WORK_PROFESSIONAL, TaskCategory.EDUCATION_LEARNING),
        TestCase("Schedule annual furnace inspection", TaskCategory.HOME_MAINTENANCE),
        TestCase("Attend local community festival", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),
        TestCase("Research long-term care insurance", TaskCategory.FINANCIAL, TaskCategory.PERSONAL),
        TestCase("Plan home fire safety drill", TaskCategory.HOME_MAINTENANCE, TaskCategory.PERSONAL),
        TestCase("Create vision board", TaskCategory.PERSONAL, TaskCategory.CREATIVE),
        TestCase("Schedule annual car maintenance", TaskCategory.HOME_MAINTENANCE, TaskCategory.ERRANDS_SHOPPING),
        TestCase("Attend local wine tasting event", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.TRAVEL_LOGISTICS),
        TestCase("Research college savings plans", TaskCategory.FINANCIAL, TaskCategory.EDUCATION_LEARNING),
        TestCase("Plan backyard landscaping project", TaskCategory.HOME_MAINTENANCE, TaskCategory.CREATIVE),
        TestCase("Create personal website", TaskCategory.CREATIVE, TaskCategory.PERSONAL),
        TestCase("Schedule annual chimney inspection", TaskCategory.HOME_MAINTENANCE),
        TestCase("Attend local theater performance", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.CREATIVE),
        TestCase("Research identity theft protection", TaskCategory.FINANCIAL, TaskCategory.PERSONAL),
        TestCase("Plan home energy audit", TaskCategory.HOME_MAINTENANCE, TaskCategory.FINANCIAL),
        TestCase("Create family cookbook", TaskCategory.CREATIVE, TaskCategory.PERSONAL),
        TestCase("Schedule annual gutter cleaning", TaskCategory.HOME_MAINTENANCE),
        TestCase("Attend local sports event", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),
        TestCase("Research estate planning options", TaskCategory.FINANCIAL, TaskCategory.PERSONAL),
        TestCase("Plan home security system installation", TaskCategory.HOME_MAINTENANCE, TaskCategory.PERSONAL),
        TestCase("Create family vacation scrapbook", TaskCategory.CREATIVE, TaskCategory.PERSONAL),
        TestCase("Schedule annual roof inspection", TaskCategory.HOME_MAINTENANCE),
        TestCase("Attend local food festival", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.TRAVEL_LOGISTICS),
        TestCase("Research sustainable living practices", TaskCategory.EDUCATION_LEARNING, TaskCategory.HOME_MAINTENANCE),
        TestCase("Plan home office setup", TaskCategory.HOME_MAINTENANCE, TaskCategory.WORK_PROFESSIONAL),
        TestCase("Create personal budget", TaskCategory.FINANCIAL, TaskCategory.PERSONAL),
        TestCase("Shedule dr appt", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),
        TestCase("Buy groseries", TaskCategory.ERRANDS_SHOPPING),
        TestCase("Wrk on projct", TaskCategory.WORK_PROFESSIONAL),
        TestCase("Cln house", TaskCategory.HOME_MAINTENANCE),
        TestCase("Exercse", TaskCategory.HEALTH_WELLNESS),
        TestCase("Pay bils", TaskCategory.FINANCIAL),
        TestCase("Lrn new skill", TaskCategory.EDUCATION_LEARNING),
        TestCase("Call mom", TaskCategory.SOCIAL_RELATIONSHIPS),
        TestCase("Fix sink", TaskCategory.HOME_MAINTENANCE),
        TestCase("Book flght", TaskCategory.TRAVEL_LOGISTICS),
        TestCase("Kontaktlinsen wechseln", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL), 
        TestCase("Hire a dog", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),
        TestCase("Testament aktualisieren", TaskCategory.PERSONAL, TaskCategory.FINANCIAL),
        TestCase("Grab pflegen", TaskCategory.HOME_MAINTENANCE),
        TestCase("Lebensziele aktualisieren", TaskCategory.PERSONAL, TaskCategory.EDUCATION_LEARNING),
        TestCase("Test", TaskCategory.GENERAL),
        TestCase("asdf", TaskCategory.GENERAL),
        TestCase("...", TaskCategory.GENERAL),
        TestCase("Organize miscellaneous notes", TaskCategory.GENERAL, TaskCategory.ADMINISTRATIVE),  
        TestCase("Set up a new productivity system", TaskCategory.GENERAL, TaskCategory.WORK_PROFESSIONAL),  
        TestCase("Review unfinished tasks and prioritize", TaskCategory.GENERAL, TaskCategory.WORK_PROFESSIONAL),  
        TestCase("File important documents", TaskCategory.ADMINISTRATIVE, TaskCategory.WORK_PROFESSIONAL),    
        TestCase("Update address with utility companies", TaskCategory.ADMINISTRATIVE, TaskCategory.HOME_MAINTENANCE),  
        TestCase("Schedule annual performance review meeting", TaskCategory.ADMINISTRATIVE, TaskCategory.WORK_PROFESSIONAL),  
        TestCase("Plan weekend self-care routine", TaskCategory.PERSONAL, TaskCategory.HEALTH_WELLNESS),  
        TestCase("Set monthly personal goals", TaskCategory.PERSONAL, TaskCategory.EDUCATION_LEARNING),  
        TestCase("Organize digital photos", TaskCategory.PERSONAL, TaskCategory.CREATIVE),  
        TestCase("Prepare presentation for team meeting", TaskCategory.WORK_PROFESSIONAL, TaskCategory.EDUCATION_LEARNING),  
        TestCase("Research industry trends for upcoming project", TaskCategory.WORK_PROFESSIONAL, TaskCategory.EDUCATION_LEARNING),  
        TestCase("Review and adjust monthly budget", TaskCategory.FINANCIAL, TaskCategory.PERSONAL),  
        TestCase("Compare investment options", TaskCategory.FINANCIAL, TaskCategory.EDUCATION_LEARNING),  
        TestCase("Pay upcoming utility bills", TaskCategory.FINANCIAL, TaskCategory.HOME_MAINTENANCE, TaskCategory.ADMINISTRATIVE),    
        TestCase("Schedule annual physical check-up", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),  
        TestCase("Research healthy meal plans", TaskCategory.HEALTH_WELLNESS, TaskCategory.EDUCATION_LEARNING),  
        TestCase("Try a new workout routine", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),  
        TestCase("Deep clean kitchen appliances", TaskCategory.HOME_MAINTENANCE, TaskCategory.PERSONAL),  
        TestCase("Fix leaky faucet", TaskCategory.HOME_MAINTENANCE, TaskCategory.ERRANDS_SHOPPING),  
        TestCase("Plan a weekend get-together with friends", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),  
        TestCase("Send thank-you notes to colleagues", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.WORK_PROFESSIONAL),  
        TestCase("Call a family member to catch up", TaskCategory.SOCIAL_RELATIONSHIPS, TaskCategory.PERSONAL),  
        TestCase("Enroll in an online course", TaskCategory.EDUCATION_LEARNING, TaskCategory.WORK_PROFESSIONAL),  
        TestCase("Read a book on leadership", TaskCategory.EDUCATION_LEARNING, TaskCategory.WORK_PROFESSIONAL),  
        TestCase("Practice a new language for 15 minutes", TaskCategory.EDUCATION_LEARNING, TaskCategory.PERSONAL),  
        TestCase("Start a daily journaling habit", TaskCategory.CREATIVE, TaskCategory.PERSONAL),  
        TestCase("Work on a digital art project", TaskCategory.CREATIVE, TaskCategory.EDUCATION_LEARNING),  
        TestCase("Write a short story", TaskCategory.CREATIVE, TaskCategory.EDUCATION_LEARNING),  
        TestCase("Book hotel for upcoming trip", TaskCategory.TRAVEL_LOGISTICS, TaskCategory.ERRANDS_SHOPPING),  
        TestCase("Renew passport before expiration", TaskCategory.TRAVEL_LOGISTICS, TaskCategory.ADMINISTRATIVE),  
        TestCase("Create a packing list for vacation", TaskCategory.TRAVEL_LOGISTICS, TaskCategory.PERSONAL),  
        TestCase("Buy groceries for the week", TaskCategory.ERRANDS_SHOPPING, TaskCategory.HEALTH_WELLNESS),  
        TestCase("Drop off dry cleaning", TaskCategory.ERRANDS_SHOPPING, TaskCategory.ADMINISTRATIVE),  
        TestCase("Pick up prescription refill", TaskCategory.ERRANDS_SHOPPING, TaskCategory.HEALTH_WELLNESS),  
        TestCase("Auto waschen", TaskCategory.ERRANDS_SHOPPING, TaskCategory.HOME_MAINTENANCE),  
        TestCase("Zahnbürsten wechseln ", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),  
        TestCase("Steuern machen", TaskCategory.FINANCIAL, TaskCategory.ADMINISTRATIVE),  
        TestCase("Daueraufträge prüfen", TaskCategory.FINANCIAL, TaskCategory.ADMINISTRATIVE),    
        TestCase("Professionelle Zahlreinigung", TaskCategory.HEALTH_WELLNESS, TaskCategory.PERSONAL),  
        TestCase("Neues Fachbuch lesen", TaskCategory.EDUCATION_LEARNING, TaskCategory.WORK_PROFESSIONAL),    
]
    return test_cases
