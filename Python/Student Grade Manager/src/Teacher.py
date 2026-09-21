
class Teacher:
    def __init__(self, name, ID, password):
        self.name = name
        self.ID = ID
        self.password = password
        self.scores = {}

    def add_score(self, student, subject, score):
        self.scores[(student, subject)] = score

    def get_score(self, student, subject):
        return self.scores[(student, subject)]

    def get_scores(self):
        return self.scores

    def get_average_score(self):
        average_score = float(sum(self.scores) / len(self.scores))

        return average_score

    def get_teacher_ID(self):
        return self.ID

    def get_teacher_password(self):
        return self.password
