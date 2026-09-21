from Teacher import Teacher


class Classroom:
    def __init__(self, teacher):
        self.teacher = teacher
        self.students = []
        self.subjects = []

    def teacher_login(self, ID, password):
        if ID == Teacher.get_teacher_ID(ID) and password == Teacher.get_teacher_password(password):
            return True
        else:
            return False

    def classroom_setup(self):
        for subject in range(int(input("Enter number of subjects: "))):
            self.subjects.append(input("Enter subject name: "))
        for student in range(int(input("Enter number of students: a"))):
            self.students.append(input("Enter student name: "))

    def collect_scores(self):
        for student in self.students:
            for subject in self.subjects:
                student_score = Teacher.get_score(subject)
                self.students.append(student_score)
                self.subjects.append(student_score)
        #         student.add_score(score)          <- write #1
        #         matching_subject.add_grade(score)  <- write #2 (same score, other collection)

    def print_class_summary(self):
        # for each student: student.print_report_card()
        # for each subject: print subject.get_name() + " average: " + subject.get_average_score()
        pass