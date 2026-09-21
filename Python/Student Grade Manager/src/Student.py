from Subject import Subject

class Student:
    def __init__(self, name):
        self.name = name
        self.scores = []

    def get_name(self):
        return self.name

    def get_student_average_score(self):
        return sum(self.scores) / len(self.scores)

    def print_report_card(self):
        print("Student Name: " + self.name +
              "Average score: " + str(self.get_student_average_score()))

