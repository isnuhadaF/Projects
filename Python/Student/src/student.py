

class Student:
    def __init__(self, name, grade_level):
        self.name = name
        self.grade_level = grade_level

    def introduce(self):
        return f"Hello, Good Day. My name is {self.name}, and i'm in grade {self.grade_level}"

    def promote(self):
        self.grade_level += 1


    def has_passed(self, student_score):
        if student_score >= 80:
            return f"{self.name} has passed!"
        else:
            return f"{self.name} has failed!"

    def update_name(self, new_student_name):
        self.name = new_student_name

    def is_graduating(self):
        if self.grade_level >= 12:
            return f"{self.name} graduated!"
        else:
            return f"{self.name} is yet to graduate"

