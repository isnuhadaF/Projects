

class Subject:

    def __init__(self, name):
        self.name = name
        self.student_scores = []

    def get_name(self):
        return self.name

    def get_scores(self):
        return self.student_scores

    def add_score(self, grade):
        self.student_scores.append(grade)

    def get_average_subject_score(self):
        average_grade = sum(self.student_scores) / len(self.student_scores)

        return average_grade

    def get_total_subject_score(self):
        total_grade = sum(self.student_scores)

        return total_grade