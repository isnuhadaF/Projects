from Classroom import Classroom
from Teacher import Teacher
from Student import Student
from Subject import Subject

def test_collect_scores_appends_score_from_teacher():
    teacher = Teacher("Mr. Smith", "T001", "secret")
    classroom = Classroom(teacher)
    classroom.students.append(Student("Alice"))
    classroom.subjects.append(Subject("Math"))

    try:
        classroom.collect_scores()
        assert False, "expected AttributeError but collect_scores ran without error"
    except AttributeError as e:
        print("Confirmed current bug:", e)

if __name__ == "__main__":
    test_collect_scores_appends_score_from_teacher()
    print("Test passed — confirms collect_scores is currently broken as diagnosed.")